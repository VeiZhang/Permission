package com.excellence.permission.support;

import android.os.Build;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.excellence.permission.support.PermissionPageManager.MANUFACTURER_OPPO;
import static com.excellence.permission.support.PermissionPageManager.MANUFACTURER_XIAOMI;
import static com.excellence.permission.support.PermissionPageManager.MANUFACTURER_MEIZU;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/3
 *     desc   :
 * </pre>
 */

public class ManufacturerSupport
{
	private static String[] mForceManufacturers = { MANUFACTURER_XIAOMI, MANUFACTURER_MEIZU };
	private static Set<String> mForceSet = new HashSet<>(Arrays.asList(mForceManufacturers));
	private static String[] mUnderMHasPermissionsRequestManufacturer = { MANUFACTURER_XIAOMI, MANUFACTURER_MEIZU, MANUFACTURER_OPPO };
	private static Set<String> mUnderMSet = new HashSet<>(Arrays.asList(mUnderMHasPermissionsRequestManufacturer));

	/**
	 * manufacturer that need request by some special measures, above {@link android.os.Build.VERSION_CODES#M}
	 *
	 * @return
	 */
	public static boolean isForceManufacturer()
	{
		return mForceSet.contains(PermissionPageManager.getManufacturer());
	}

	/**
	 * manufacturer that need request permissions under {@link android.os.Build.VERSION_CODES#M} and above {@link android.os.Build.VERSION_CODES#LOLLIPOP}
	 *
	 * @return
	 */
	public static boolean isUnderMHasPermissionRequestManufacturer()
	{
		return mUnderMSet.contains(PermissionPageManager.getManufacturer());
	}

	/**
	 * under 6.0 and above 5.0
	 *
	 * @return
	 */
	public static boolean isAndroidL()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
	}
}
