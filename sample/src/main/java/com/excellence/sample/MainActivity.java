package com.excellence.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excellence.permission.IPermissionListener;
import com.excellence.permission.PermissionActivity.OnRationaleListener;
import com.excellence.permission.PermissionRequest;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

	private Button mSingleRequest = null;
	private Button mMultiRequest = null;
	private Button mDefaultRetry = null;
	private Button mCustomRetry = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSingleRequest = (Button) findViewById(R.id.request_single);
		mMultiRequest = (Button) findViewById(R.id.request_multi);
		mDefaultRetry = (Button) findViewById(R.id.request_default_refuse);
		mCustomRetry = (Button) findViewById(R.id.request_custom_retry);

		mSingleRequest.setOnClickListener(this);
		mMultiRequest.setOnClickListener(this);
		mDefaultRetry.setOnClickListener(this);
		mCustomRetry.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.request_single:
			singleRequest();
			break;

		case R.id.request_multi:
			multiRequest();
			break;

		case R.id.request_default_refuse:
			defaultRetry();
			break;

		case R.id.request_custom_retry:
			customRetry();
			break;
		}
	}

	/**
	 * 申请权限失败后重新申请自定义的弹框
	 */
	private void customRetry()
	{
		new PermissionRequest(this, new IPermissionListener()
		{
			@Override
			public void onPermissionsGranted()
			{
				Toast.makeText(MainActivity.this, "申请权限重新自定义申请成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPermissionsDenied()
			{
				Toast.makeText(MainActivity.this, "申请权限重新自定义申请失败", Toast.LENGTH_SHORT).show();
			}
		}).setOnRationaleListener(new OnRationaleListener()
		{
			@Override
			public void onRationaleResult(boolean showRationale)
			{
				// 自定义弹框
			}
		}).request(ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION);
	}

	/**
	 * 申请权限失败后重新申请默认的弹框
	 */
	private void defaultRetry()
	{
		new PermissionRequest(this, new IPermissionListener()
		{
			@Override
			public void onPermissionsGranted()
			{
				Toast.makeText(MainActivity.this, "申请权限重新默认申请成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPermissionsDenied()
			{
				Toast.makeText(MainActivity.this, "申请权限重新默认申请失败", Toast.LENGTH_SHORT).show();
			}
		}).setOnRationaleListener(null).request(RECORD_AUDIO, WRITE_CALL_LOG);
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

}
