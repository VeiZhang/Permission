package com.excellence.permission.page;

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

public abstract class PermissionPage
{

	public static final String PACKAGE_TAG = "package";
	public static final String EXTRA_PACKAGE_NAME = "extra_pkgname";

	public static final int PERMISSION_REQUEST_CODE = 1024;

	protected Context mContext = null;

	public PermissionPage(Context context)
	{
		mContext = context;
	}

	/**
	 *
	 * @return
	 * @throws Exception {@link android.content.ActivityNotFoundException}
	 */
	abstract Intent settingIntent() throws Exception;

}
