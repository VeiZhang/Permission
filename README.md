# Android6.0时代权限管理

在Android6.0以后某些权限需要动态申请，对权限进行封装管理，方便使用时申请权限回调。


[![Download][icon_download]][download]

```
compile 'com.excellence:permission:1.0.0'
```

## 示例

```java
/**
 * 申请单个权限
 */
private void singleRequest()
{
    new PermissionRequest(this, new IPermissionListener()
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
            checkDeniedPermission();
        }
    }).request(WRITE_EXTERNAL_STORAGE);
}

/**
 * 如果用户点击“不再提示”，则显示提示框，进入Setting里设置权限
 */
private void checkDeniedPermission()
{
    if (PermissionRequest.hasAlwaysDeniedPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE))
    {
        new SettingDialog(MainActivity.this).show();
    }
}
```

**注意**

* `PermissionRequest`可以在任何地方执行request的权限申请
* `SettingDialog`对Context判断
    * Context是Activity类型时，执行startActivityForResult，在当前Activity中接收权限申请结果
    * Context是其他类型，则执行startActivity

## 修改日志
| 版本 | 描述 |
| --- | ---- |
| [1.0.0][permission1.0.0] | Android6.0动态申请权限 |

<!-- 网站链接 -->

[download]:https://bintray.com/veizhang/maven/permission/_latestVersion "Latest version"

<!-- 图片链接 -->

[icon_download]:https://api.bintray.com/packages/veizhang/maven/permission/images/download.svg

<!-- 版本 -->

[permission1.0.0]:https://bintray.com/veizhang/maven/permission/1.0.0