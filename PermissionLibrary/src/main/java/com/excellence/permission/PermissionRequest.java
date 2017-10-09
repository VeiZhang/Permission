package com.excellence.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;

import com.excellence.permission.PermissionActivity.OnRationaleListener;
import com.excellence.permission.PermissionActivity.OnRequestPermissionsListener;

import java.util.ArrayList;
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
	private OnRationaleListener mOnRationaleListener = null;
	private String[] mDeniedPermissions = null;

	public PermissionRequest(@NonNull Context context, IPermissionListener listener)
	{
		mContext = context;
		mPermissionListener = new PermissionListener(listener);
	}

	public PermissionRequest setOnRationaleListener(OnRationaleListener onRationaleListener)
	{
		mOnRationaleListener = onRationaleListener;
		return this;
	}

	public void request(@Size(min = 1) String... permissions)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		{
			mPermissionListener.onPermissionsGranted();
		}
		else
		{
			mDeniedPermissions = getDeniedPermissions(mContext, permissions);
			if (mDeniedPermissions.length > 0)
			{
				PermissionActivity.setOnRationaleListener(new RationaleListener());
				startPermissionActivity();
			}
			else
				mPermissionListener.onPermissionsGranted();
		}
	}

	public void request(@Size(min = 1) List<String> permissions)
	{
		request(permissions.toArray(new String[permissions.size()]));
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public void resume()
	{
		PermissionActivity.setOnRequestPermissionsListener(new RequestPermissionsListener());
		startPermissionActivity();
	}

	private void startPermissionActivity()
	{
		Intent intent = new Intent(mContext, PermissionActivity.class);
		intent.putExtra(PermissionActivity.KEY_PERMISSIONS, mDeniedPermissions);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	private String[] getDeniedPermissions(Context context, String[] permissions)
	{
		List<String> deniedPermissions = new ArrayList<>();
		for (String permission : permissions)
		{
			if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
				deniedPermissions.add(permission);
		}
		return deniedPermissions.toArray(new String[deniedPermissions.size()]);
	}

	private class PermissionListener implements IPermissionListener
	{
		private IPermissionListener mListener = null;

		public PermissionListener(IPermissionListener listener)
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

	private class RequestPermissionsListener implements OnRequestPermissionsListener
	{
		@Override
		public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults)
		{
			List<String> deniedPermissions = new ArrayList<>();
			for (int i = 0; i < permissions.length; i++)
			{
				if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
					deniedPermissions.add(permissions[i]);
			}

			if (deniedPermissions.isEmpty())
				mPermissionListener.onPermissionsGranted();
			else
				mPermissionListener.onPermissionsDenied();
		}
	}

	private class RationaleListener implements OnRationaleListener
	{
		@Override
		@RequiresApi(api = Build.VERSION_CODES.M)
		public void onRationaleResult(boolean showRationale)
		{
			if (showRationale)
			{
				if (mOnRationaleListener != null)
					mOnRationaleListener.onRationaleResult(showRationale);
				else
					new SettingDialog(mContext).show();
			}
			else
				resume();
		}
	}
}
