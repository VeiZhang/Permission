package com.excellence.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

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
	private static OnRequestPermissionsListener mOnRequestPermissionsListener = null;

	public static void setOnRationaleListener(OnRationaleListener listener)
	{
		mOnRationaleListener = listener;
	}

	public static void setOnRequestPermissionsListener(OnRequestPermissionsListener listener)
	{
		mOnRequestPermissionsListener = listener;
	}

	@Override
	@RequiresApi(api = Build.VERSION_CODES.M)
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String[] permissions = intent.getStringArrayExtra(KEY_PERMISSIONS);
		if (permissions == null || permissions.length == 0)
		{
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
				rationale = shouldShowRequestPermissionRationale(permission);
				if (rationale)
					break;
			}
			mOnRationaleListener.onRationaleResult(rationale);
			mOnRationaleListener = null;
			finish();
			return;
		}

		if (mOnRequestPermissionsListener != null)
			requestPermissions(permissions, 1);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if (mOnRequestPermissionsListener != null)
		{
			mOnRequestPermissionsListener.onRequestPermissionsResult(permissions, grantResults);
			mOnRequestPermissionsListener = null;
		}
		finish();
	}

	public interface OnRationaleListener
	{
		void onRationaleResult(boolean showRationale);
	}

	public interface OnRequestPermissionsListener
	{
		void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults);
	}
}
