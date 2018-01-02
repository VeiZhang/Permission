package com.excellence.permission.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/2
 *     desc   :
 * </pre>
 */

public class XIAOMI extends PermissionPage
{
	private static final String SETTING_PKG = "com.miui.securitycenter";
	private static final String MIUI_7 = "7";
	private static final String MIUI_6 = "6";
	private static final String MIUI_7_INTENT = "miui.intent.action.APP_PERM_EDITOR";
	private static final String MIUI_7_MANAGER_OUT_CLS = "com.miui.permcenter.permissions.AppPermissionsEditorActivity";
	private static final String MIUI_8_MANAGER_OUT_CLS = "com.miui.securityscan.MainActivity";
	private static final String GET_PROP = "getprop ro.miui.ui.version.name";
	private static final int BUF_SIZE = 1024;

	public XIAOMI(Context context)
	{
		super(context);
	}

	@Override
	public Intent settingIntent() throws Exception
	{
		Intent intent = new Intent();
		String miuiInfo = getSystemProperty();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (miuiInfo.contains(MIUI_6) || miuiInfo.contains(MIUI_7))
		{
			intent = new Intent(MIUI_7_INTENT);
			intent.setClassName(SETTING_PKG, MIUI_7_MANAGER_OUT_CLS);
			intent.putExtra(EXTRA_PACKAGE_NAME, mContext.getPackageName());
		}
		else
		{
			/**
			 * miui 8
			 */
			intent.putExtra(PACKAGE_TAG, mContext.getPackageName());
			ComponentName componentName = new ComponentName(SETTING_PKG, MIUI_8_MANAGER_OUT_CLS);
			intent.setComponent(componentName);
		}
		return intent;
	}

	private String getSystemProperty()
	{
		String line = "";
		BufferedReader reader = null;
		try
		{
			Process process = Runtime.getRuntime().exec(GET_PROP);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()), BUF_SIZE);
			line = reader.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return line;
	}
}
