package com.excellence.permission.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/2
 *     desc   :
 * </pre>
 */

public class OPPO extends PermissionPage
{
	private static final String SETTING_PKG = "com.coloros.safecenter";
	private static final String MANAGER_OUT_CLS = "com.coloros.safecenter.permission.singlepage.PermissionSinglePageActivity";

	public OPPO(Context context)
	{
		super(context);
	}

	@Override
	public Intent settingIntent() throws Exception
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(PACKAGE_TAG, mContext.getPackageName());
		ComponentName componentName = new ComponentName(SETTING_PKG, MANAGER_OUT_CLS);
		intent.setComponent(componentName);
		return intent;
	}
}
