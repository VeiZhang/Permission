package com.excellence.permission;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/9/29
 *     desc   : 申请权限回调
 * </pre>
 */

public interface IPermissionListener
{
	void onPermissionsGranted();

	void onPermissionsDenied();
}
