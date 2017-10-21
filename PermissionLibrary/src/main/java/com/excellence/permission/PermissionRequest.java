package com.excellence.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.excellence.permission.PermissionActivity.OnRationaleListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/9/29
 *     desc   : 权限申请管理者，针对API23（包括23）以上，{@link Build.VERSION_CODES#M}
 *     			from https://github.com/yanzhenjie/AndPermission
 * </pre>
 */

public class PermissionRequest
{
	public static final String TAG = PermissionRequest.class.getSimpleName();

	private Context mContext = null;
	private IPermissionListener mPermissionListener = null;
	private IRationaleListener mRationaleListener = null;
	private List<String> mRequestPermissions = null;
	private List<String> mDeniedPermissions = null;

	@NonNull
	public static PermissionRequest with(Context context)
	{
		return new PermissionRequest(context);
	}

	public PermissionRequest(Context context)
	{
		mContext = context;
		mRequestPermissions = new ArrayList<>();
	}

	/**
	 * 设置待授权的权限
	 *
	 * @param permissions 待授权的权限
	 */
	public PermissionRequest permission(@Size(min = 1) List<String> permissions)
	{
		mRequestPermissions.addAll(permissions);
		return this;
	}

	/**
	 * 设置待授权的权限
	 *
	 * @param permissions 待授权的权限
	 */
	public PermissionRequest permission(@Size(min = 1) String... permissions)
	{
		return permission(Arrays.asList(permissions));
	}

	/**
	 * 默认提示{@link SettingDialog#show}，进入Setting应用设置权限
	 * 可以自定义弹框提示用户去设置，请参考{@link SettingDialog}
	 * 
	 * @param listener
	 * @return
	 */
	public PermissionRequest rationale(IRationaleListener listener)
	{
		mRationaleListener = listener;
		return this;
	}

	/**
	 * 开始申请，判断权限是否拒绝过-“不再提示”
	 *
	 * @param listener
	 */
	public void request(IPermissionListener listener)
	{
		mPermissionListener = new RequestListener(listener);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		{
			mPermissionListener.onPermissionsGranted();
		}
		else
		{
			mDeniedPermissions = getDeniedPermissions(mContext, mRequestPermissions);
			if (mDeniedPermissions.size() > 0)
			{
				PermissionActivity.setOnRationaleListener(new RationaleListener());
				startPermissionActivity();
			}
			else
				mPermissionListener.onPermissionsGranted();
		}
	}

	/**
	 * 申请权限
	 */
	@RequiresApi(api = Build.VERSION_CODES.M)
	public void resume()
	{
		PermissionActivity.setRequestPermissionsListener(mPermissionListener);
		PermissionActivity.setRequestRationaleListener(mRationaleListener);
		startPermissionActivity();
	}

	private void startPermissionActivity()
	{
		Intent intent = new Intent(mContext, PermissionActivity.class);
		intent.putStringArrayListExtra(PermissionActivity.KEY_PERMISSIONS, (ArrayList<String>) mDeniedPermissions);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	/**
	 * 检测未授权的权限
	 *
	 * @param context
	 * @param permissions
	 * @return 返回未授权的权限
	 */
	public static List<String> getDeniedPermissions(@NonNull Context context, @NonNull List<String> permissions)
	{
		List<String> deniedPermissions = new ArrayList<>();
		for (String permission : permissions)
		{
			if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
				deniedPermissions.add(permission);
		}
		return deniedPermissions;
	}

	/**
	 * 检测权限
	 *
	 * @param context
	 * @param permissions
	 * @return {@code true}:授权<br>{@code false}:未授权
	 */
	public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return true;
		for (String permission : permissions)
		{
			String op = AppOpsManagerCompat.permissionToOp(permission);
			if (TextUtils.isEmpty(op))
				continue;
			int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
			if (result == AppOpsManagerCompat.MODE_IGNORED)
				return false;
			result = ContextCompat.checkSelfPermission(context, permission);
			if (result != PackageManager.PERMISSION_GRANTED)
				return false;
		}
		return true;
	}

	/**
	 * 检测权限
	 * 
	 * @param context
	 * @param permissions
	 * @return {@code true}:授权<br>{@code false}:未授权
	 */
	public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions)
	{
		return hasPermission(context, Arrays.asList(permissions));
	}

	/**
	 * 在{@link IPermissionListener#onPermissionsDenied()}中，即在权限申请失败的时候，判断权限是否被拒绝-不再提示
	 *
	 * @param activity
	 * @param deniedPermissions
	 * @return {@code true}:选择“不再提示”<br>{@code false}:未选择“不再提示”
	 */
	public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return false;

		for (String permission : deniedPermissions)
		{
			boolean rationale = activity.shouldShowRequestPermissionRationale(permission);
			if (!rationale)
				return true;
		}
		return false;
	}

	/**
	 * 在{@link IPermissionListener#onPermissionsDenied()}中，即在权限申请失败的时候，判断权限是否被拒绝-不再提示
	 * 
	 * @param activity
	 * @param deniedPermissions
	 * @return {@code true}:选择“不再提示”<br>{@code false}:未选择“不再提示”
	 */
	public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull String... deniedPermissions)
	{
		return hasAlwaysDeniedPermission(activity, Arrays.asList(deniedPermissions));
	}

	private class RequestListener implements IPermissionListener
	{
		private IPermissionListener mListener = null;

		public RequestListener(IPermissionListener listener)
		{
			mListener = listener;
		}

		@Override
		public void onPermissionsGranted()
		{
			if (mListener != null)
				mListener.onPermissionsGranted();
		}

		@Override
		public void onPermissionsDenied()
		{
			if (mListener != null)
				mListener.onPermissionsDenied();
		}
	}

	private class RationaleListener implements OnRationaleListener
	{
		@Override
		@RequiresApi(api = Build.VERSION_CODES.M)
		public void onRationaleResult(boolean showRationale)
		{
			resume();
		}
	}
}
