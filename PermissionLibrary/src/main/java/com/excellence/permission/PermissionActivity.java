package com.excellence.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.excellence.permission.apply.PermissionsChecker;

import java.util.ArrayList;
import java.util.List;

import static com.excellence.permission.support.PermissionPage.PERMISSION_REQUEST_CODE;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/9/29
 *     desc   : 系统权限申请的弹框界面
 * </pre>
 */

public final class PermissionActivity extends Activity
{
	public static final String TAG = PermissionActivity.class.getSimpleName();

	public static final String KEY_PERMISSIONS = "KEY_PERMISSIONS";

	private static OnRationaleListener mOnRationaleListener = null;
	private static IPermissionListener mRequestPermissionsListener = null;
	private static IRationaleListener mRequestRationaleListener = null;

	private List<String> mDeniedPermissions = new ArrayList<>();

	protected static void setOnRationaleListener(OnRationaleListener listener)
	{
		mOnRationaleListener = listener;
	}

	protected static void setRequestPermissionsListener(IPermissionListener listener)
	{
		mRequestPermissionsListener = listener;
	}

	protected static void setRequestRationaleListener(IRationaleListener listener)
	{
		mRequestRationaleListener = listener;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		List<String> permissions = intent.getStringArrayListExtra(KEY_PERMISSIONS);
		if (permissions == null || permissions.size() == 0)
		{
			mOnRationaleListener = null;
			mRequestPermissionsListener = null;
			finish();
			return;
		}

		if (mOnRationaleListener != null)
		{
			boolean rationale = false;
			for (String permission : permissions)
			{
				/**
				 * 权限拒绝策略
				 *
				 * 在6.0时代，需要在程序运行时获取相关权限，展开一个对话框询问是否授予该程序相应权限。
				 * 从第二次开始运行的时候，会增加一个选项框，“以后不再询问”，如果选择了这个选项，那么以后程序不会再询问是否授予权限了。
				 * 这时候选择了确认倒还好，之后倒方便了。
				 * 如果选择了拒绝，那之后也不会显示对话框，但是权限一直是拒绝的。这样是非常不好的体验，不知道的还以为程序崩溃了。
				 * 所以，我们需要在这个时候也显示相应对话框[自定义的对话框]来告诉用户
				 *
				 * 第一次请求时，返回false
				 * 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
				 * 注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。
				 * 如果设备规范禁止应用具有该权限，此方法也会返回 false。
				 *
				 * 如果想判断是否拒绝权限，需要在请求一次之后的Failure回调里，再次执行{@link #shouldShowRequestPermissionRationale}方法，返回false为拒绝
				 */
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					rationale = shouldShowRequestPermissionRationale(permission);
				}
				if (rationale)
				{
					break;
				}
			}
			mOnRationaleListener.onRationaleResult(rationale);
			mOnRationaleListener = null;
			finish();
			return;
		}

		if (mRequestPermissionsListener != null)
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
			}
			else
			{
				onRequestPermissionsResult(permissions.toArray(new String[permissions.size()]));
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		onRequestPermissionsResult(permissions);
	}

	private void onRequestPermissionsResult(String[] permissions)
	{
		if (mRequestPermissionsListener != null)
		{
			for (int i = 0; i < permissions.length; i++)
			{
				if (!PermissionsChecker.hasPermission(this, permissions[i]))
				{
					mDeniedPermissions.add(permissions[i]);
				}
			}

			if (mDeniedPermissions.isEmpty())
			{
				permissionsGranted();
			}
			else
			{
				/**
				 * 如果用户点击“不再提示”，则显示提示框，进入Setting里设置权限
				 */
				if (PermissionsChecker.hasAlwaysDeniedPermission(this, mDeniedPermissions))
				{
					if (mRequestRationaleListener != null)
					{
						mRequestRationaleListener.onRationale(this);
					}
					else
					{
						new SettingDialog(this).setOnCancelListener(new SettingDialog.OnCancelListener()
						{
							@Override
							public void onCancel()
							{
								/**
								 * 点击取消时，认为请求失败
								 */
								permissionsDenied();
							}
						}).show();
					}
				}
				else
				{
					permissionsDenied();
				}
			}
		}
	}

	public void permissionsGranted()
	{
		if (mRequestPermissionsListener != null)
		{
			mRequestPermissionsListener.onPermissionsGranted();
		}
		mRequestPermissionsListener = null;
		mRequestRationaleListener = null;
		finish();
	}

	public void permissionsDenied()
	{
		if (mRequestPermissionsListener != null)
		{
			mRequestPermissionsListener.onPermissionsDenied();
		}
		mRequestPermissionsListener = null;
		mRequestRationaleListener = null;
		finish();
	}

	/**
	 * 进入Setting的app，申请权限结果回调
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PERMISSION_REQUEST_CODE)
		{
			if (PermissionsChecker.hasPermission(this, mDeniedPermissions))
			{
				permissionsGranted();
			}
			else
			{
				permissionsDenied();
			}
		}
	}

	protected interface OnRationaleListener
	{
		void onRationaleResult(boolean showRationale);
	}
}
