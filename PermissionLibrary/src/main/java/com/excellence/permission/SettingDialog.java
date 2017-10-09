package com.excellence.permission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/9/29
 *     desc   : 权限[拒绝]的默认提示弹框，可以自定义在接口{@link PermissionRequest#setOnRationaleListener}
 * </pre>
 */

public class SettingDialog
{
	private AlertDialog.Builder mBuilder = null;
	private Context mContext = null;

	protected SettingDialog(Context context)
	{
		mContext = context;
		mBuilder = new AlertDialog.Builder(mContext).setCancelable(false).setTitle(R.string.permission_title_permission_failed).setMessage(R.string.permission_message_permission_failed)
				.setPositiveButton(R.string.permission_setting, mListener).setNegativeButton(R.string.permission_cancel, mListener);
	}

	private DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case DialogInterface.BUTTON_POSITIVE:
				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
				intent.setData(uri);
				mContext.startActivity(intent);
				break;

			case DialogInterface.BUTTON_NEGATIVE:

				break;
			}
		}
	};

	public void show()
	{
		mBuilder.show();
	}
}
