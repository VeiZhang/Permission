package com.excellence.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.excellence.permission.support.PermissionPageManager;

import static com.excellence.permission.support.PermissionPage.PERMISSION_REQUEST_CODE;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/9/29
 *     desc   : 权限拒绝[不再提示]的默认提示弹框
 * </pre>
 */

public class SettingDialog
{
	private AlertDialog.Builder mBuilder = null;
	private Context mContext = null;
	private OnCancelListener mOnCancelListener = null;

	public SettingDialog(Context context)
	{
		mContext = context;
		mBuilder = new AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog).setCancelable(false).setTitle(R.string.permission_title_permission_failed)
				.setMessage(R.string.permission_message_permission_failed).setPositiveButton(R.string.permission_setting, mListener).setNegativeButton(R.string.permission_cancel, mListener);
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

	public SettingDialog setOnCancelListener(OnCancelListener onCancelListener)
	{
		mOnCancelListener = onCancelListener;
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
				Intent intent = PermissionPageManager.getSettingIntent(mContext);
				if (mContext instanceof Activity)
				{
					((Activity) mContext).startActivityForResult(intent, PERMISSION_REQUEST_CODE);
				}
				else
				{
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				if (mOnCancelListener != null)
					mOnCancelListener.onCancel();
				break;
			}
		}
	};

	public void show()
	{
		mBuilder.show();
	}

	public interface OnCancelListener
	{
		void onCancel();
	}
}
