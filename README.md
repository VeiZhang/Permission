# Android6.0时代权限管理

在Android6.0以后某些权限需要动态申请，对权限进行封装管理，方便使用时申请权限回调。


[![Download][icon_download]][download]

```
compile 'com.excellence:permission:1.0.0'
```

## 说明

Android6.0以后，动态申请权限，用户点击来确定是否授权，分三种情况：授权、拒绝、拒绝-不再提示。前两种比较好理解，第三种“拒绝-不再提示”，即用户选择了“不再提示”，则再次使用动态权限申请，不弹出授权框，用户无法授权，这种情况在授权失败处，检测是否“不再提示”，“是”则自定义弹框，让用户进入Setting的apk中授权。

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
        }
    }).request(WRITE_EXTERNAL_STORAGE);
}

/**
 * 申请多个权限
 */
private void multiRequest()
{
    new PermissionRequest(this, new IPermissionListener()
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
    }).request(READ_CONTACTS, CAMERA);
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