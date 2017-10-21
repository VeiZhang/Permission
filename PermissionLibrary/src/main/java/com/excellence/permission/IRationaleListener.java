package com.excellence.permission;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     date   : 2017/10/21
 *     desc   : 默认提示{@link SettingDialog#show}，进入Setting应用设置权限
 *              可以自定义弹框提示用户去设置，请参考{@link SettingDialog}
 * </pre>
 */

public interface IRationaleListener
{
	/**
	 * 使用{@link PermissionActivity}的引用，返回值处理{@link PermissionActivity#onActivityResult}
	 *
	 * @param activity
	 */
	void OnRationale(PermissionActivity activity);
}
