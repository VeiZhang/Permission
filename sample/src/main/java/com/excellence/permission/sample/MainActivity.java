package com.excellence.permission.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excellence.permission.IPermissionListener;
import com.excellence.permission.PermissionRequest;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

	private Button mSingleRequest = null;
	private Button mMultiRequest = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSingleRequest = (Button) findViewById(R.id.request_single);
		mMultiRequest = (Button) findViewById(R.id.request_multi);

		mSingleRequest.setOnClickListener(this);
		mMultiRequest.setOnClickListener(this);
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

		}
	}

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

}
