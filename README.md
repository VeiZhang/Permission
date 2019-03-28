# Android权限管理

Android手机权限申请，对权限进行封装管理，方便使用时申请权限回调。不只是Android6.0+，国产机在Android6.0之前的版本也有权限的操作，借助[permissions4m][jokermonn]的思路，对国产机强制申请。


[![Download][icon_download]][download]

```
compile 'com.excellence:permission:_latestVersion'
```

## 说明

Android申请权限，用户点击来确定是否授权，分三种情况：授权、拒绝、拒绝-不再提示。前两种比较好理解，第三种“拒绝-不再提示”，即用户选择了“不再提示”，则再次使用动态权限申请，不弹出授权框，用户无法授权，这种情况在授权失败处，检测是否“不再提示”，选中“不再提示”后下次申请，系统权限申请框没反应，只能自定义弹框：提示用户进入Setting，让用户进入Setting的apk中授权。

* 申请权限-系统弹框
![icon_permission_request][icon_permission_request]

* 拒绝-不再询问
![icon_permission_denied][icon_permission_denied]

* 不在询问时，自定义弹框进入Setting应用
![icon_permission_no_remind][icon_permission_no_remind]

* Setting应用授权权限
![icon_permission_setting][icon_permission_setting]


## 示例

```java
/**
 * 申请单个权限
 */
private void singleRequest()
{
    PermissionRequest.with(this).permission(WRITE_EXTERNAL_STORAGE).request(new IPermissionListener()
    {

        @Override
        public void onPermissionsGranted()
        {
            Toast.makeText(MainActivity.this, "申请单个权限成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionsDenied()
        {
            Toast.makeText(MainActivity.this, "申请单个权限失败", Toast.LENGTH_SHORT).show();
        }
    });
}

/**
 * 申请多个权限
 */
private void multiRequest()
{
    PermissionRequest.with(this).permission(READ_CONTACTS, CAMERA).request(new IPermissionListener()
    {
        @Override
        public void onPermissionsGranted()
        {
            Toast.makeText(MainActivity.this, "申请多个权限成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionsDenied()
        {
            Toast.makeText(MainActivity.this, "申请多个权限失败", Toast.LENGTH_SHORT).show();
        }
    });
}
```

**注意**

* `PermissionRequest`可以在任何地方执行request的权限申请
* `SettingDialog`对Context判断
    * Context是Activity类型时，执行startActivityForResult，在当前Activity中接收权限申请结果
    * Context是其他类型，则执行startActivity

## 国产手机兼容

* 原生的Android6.0权限与**targetSDKVersion**有关，targetSDKVersion < 23 ,使用`PermissionChecker.checkSelfPermission`判断权限；targetSDKVersion >= 23时使用`ContextCompat.checkSelfPermission`判断权限。
* 有些国产机使用原生申请方法无效，因此强制申请：通过触发权限功能+`try catch`的方式，判断是否有权限，然后根据不同的机型跳转到对应的权限管理界面。
* 国产手机6.0以前可以手动关闭权限。


## 修改日志
| 版本 | 描述 |
| --- | ---- |
| [1.0.2][permission1.0.2] | 兼容低版本机型 **2019-03-28** |
| [1.0.1][permission1.0.1] | 增加国产机型权限检测、权限界面跳转 **2018-01-03** |
| [1.0.0][permission1.0.0] | Android6.0原生动态申请权限 **2017-10-21** |

## 感谢

>- [jokermonn][jokermonn]
>- [yanzhenjie][yanzhenjie]

<!-- 网站链接 -->

[download]:https://bintray.com/veizhang/maven/permission/_latestVersion "Latest version"
[jokermonn]:https://github.com/jokermonn/permissions4m
[yanzhenjie]:https://github.com/yanzhenjie/AndPermission

<!-- 图片链接 -->

[icon_download]:https://api.bintray.com/packages/veizhang/maven/permission/images/download.svg
[icon_permission_request]:https://github.com/VeiZhang/Permission/blob/master/imags/%E7%94%B3%E8%AF%B7%E6%9D%83%E9%99%90-%E7%B3%BB%E7%BB%9F%E5%BC%B9%E6%A1%86.png "申请权限-系统弹框"
[icon_permission_denied]:https://github.com/VeiZhang/Permission/blob/master/imags/%E6%8B%92%E7%BB%9D-%E4%B8%8D%E5%86%8D%E8%AF%A2%E9%97%AE.png "拒绝-不再询问"
[icon_permission_no_remind]:https://github.com/VeiZhang/Permission/blob/master/imags/%E4%B8%8D%E5%9C%A8%E8%AF%A2%E9%97%AE%E6%97%B6%EF%BC%8C%E8%87%AA%E5%AE%9A%E4%B9%89%E5%BC%B9%E6%A1%86%E8%BF%9B%E5%85%A5Setting%E5%BA%94%E7%94%A8.png "不在询问时，自定义弹框进入Setting应用"
[icon_permission_setting]:https://github.com/VeiZhang/Permission/blob/master/imags/Setting%E5%BA%94%E7%94%A8%E6%8E%88%E6%9D%83%E6%9D%83%E9%99%90.png "Setting应用授权权限"

<!-- 版本 -->

[permission1.0.2]:https://bintray.com/veizhang/maven/permission/1.0.2
[permission1.0.1]:https://bintray.com/veizhang/maven/permission/1.0.1
[permission1.0.0]:https://bintray.com/veizhang/maven/permission/1.0.0