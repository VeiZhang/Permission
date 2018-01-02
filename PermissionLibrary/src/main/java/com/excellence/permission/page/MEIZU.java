package com.excellence.permission.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/2
 *     desc   :
 * </pre>
 */

public class MEIZU extends PermissionPage
{
	private static final String SETTING_PKG = "com.meizu.safe";
	private static final String N_MANAGER_OUT_CLS = "com.meizu.safe.permission.PermissionMainActivity";
	private static final String L_MANAGER_OUT_CLS = "com.meizu.safe.SecurityMainActivity";

	public MEIZU(Context context)
	{
		super(context);
	}

	@Override
	public Intent settingIntent() throws Exception
	{
		Intent intent = new Intent();
		intent.putExtra(PACKAGE_TAG, mContext.getPackageName());
		ComponentName componentName = new ComponentName(SETTING_PKG, getManagerCls());
		intent.setComponent(componentName);
		return intent;
	}

	private String getManagerCls()
	{
		if (isAndroidL())
		{
			return L_MANAGER_OUT_CLS;
		}
		else
		{
			return N_MANAGER_OUT_CLS;
		}
	}

	private boolean isAndroidL()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
	}
}
