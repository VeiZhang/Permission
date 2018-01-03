package com.excellence.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;

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
	private Handler mHandler = new Handler();

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
		public void onRationaleResult(boolean showRationale)
		{
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					resume();
				}
			});
		}
	}
}
