package com.excellence.permission.page;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/2
 *     desc   : 根据国产定制ROM，跳转对应的应用管理界面
 *              https://github.com/jokermonn/permissions4m
 * </pre>
 */

public class PermissionPageManager
{

	public static final String TAG = PermissionPageManager.class.getSimpleName();

	/**
	 * @see android.os.Build#MANUFACTURER
	 */
	private static final String MANUFACTURER = Build.MANUFACTURER;

	private static final String MANUFACTURER_HUAWEI = "HUAWEI";
	private static final String MANUFACTURER_XIAOMI = "XIAOMI";
	private static final String MANUFACTURER_OPPO = "OPPO";
	private static final String MANUFACTURER_VIVO = "VIVO";
	private static final String MANUFACTURER_MEIZU = "MEIZU";

	public static Intent getSettingIntent(Context context)
	{
		Log.i(TAG, "****手机型号****" + MANUFACTURER);
		PermissionPage permissionPage;
		try
		{
			switch (MANUFACTURER.toUpperCase())
			{
			case MANUFACTURER_HUAWEI:
				permissionPage = new HUAWEI(context);
				break;

			case MANUFACTURER_XIAOMI:
				permissionPage = new XIAOMI(context);
				break;

			case MANUFACTURER_OPPO:
				permissionPage = new OPPO(context);
				break;

			case MANUFACTURER_VIVO:
				permissionPage = new VIVO(context);
				break;

			case MANUFACTURER_MEIZU:
				permissionPage = new MEIZU(context);
				break;

			default:
				permissionPage = new Protogenesis(context);
				break;
			}
			return permissionPage.settingIntent();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			permissionPage = new Protogenesis(context);
			return ((Protogenesis) permissionPage).settingIntent();
		}
	}
}
