package com.excellence.permission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.StringRes;

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

	public SettingDialog(Context context)
	{
		mContext = context;
		mBuilder = new AlertDialog.Builder(mContext).setCancelable(false).setTitle(R.string.permission_title_permission_failed).setMessage(R.string.permission_message_permission_failed)
				.setPositiveButton(R.string.permission_setting, mListener).setNegativeButton(R.string.permission_cancel, mListener);
	}

	public SettingDialog setTitle(@StringRes int titleId)
	{
		mBuilder.setTitle(titleId);
		return this;
	}

	public SettingDialog setTitle(CharSequence title)
	{
		mBuilder.setTitle(title);
		return this;
	}

	public SettingDialog setMessage(@StringRes int messageId)
	{
		mBuilder.setMessage(messageId);
		return this;
	}

	public SettingDialog setMessage(CharSequence message)
	{
		mBuilder.setMessage(message);
		return this;
	}

	public SettingDialog setPositiveButton(@StringRes int textId)
	{
		mBuilder.setPositiveButton(textId, mListener);
		return this;
	}

	public SettingDialog setPositiveButton(CharSequence text)
	{
		mBuilder.setPositiveButton(text, mListener);
		return this;
	}

	public SettingDialog setNegativeButton(@StringRes int textId)
	{
		mBuilder.setNegativeButton(textId, mListener);
		return this;
	}

	public SettingDialog setNegativeButton(CharSequence text)
	{
		mBuilder.setNegativeButton(text, mListener);
		return this;
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
