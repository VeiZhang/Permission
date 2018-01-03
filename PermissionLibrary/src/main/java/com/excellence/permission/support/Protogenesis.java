package com.excellence.permission.support;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/2
 *     desc   :
 * </pre>
 */

public class Protogenesis extends PermissionPage
{

	public Protogenesis(Context context)
	{
		super(context);
	}

	@Override
	public Intent settingIntent()
	{
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts(PACKAGE_TAG, mContext.getPackageName(), null);
		intent.setData(uri);
		return intent;
	}
}
