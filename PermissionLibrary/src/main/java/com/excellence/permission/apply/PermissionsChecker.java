package com.excellence.permission.apply;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.excellence.permission.support.ManufacturerSupport;
import com.excellence.permission.support.PermissionPageManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/3
 *     desc   : 强制权限检测
 *     			https://github.com/jokermonn/permissions4m/blob/master/permissions4m-api/src/main/java/com/joker/api/apply/PermissionsChecker.java
 * </pre>
 */

@SuppressLint("NewApi")
public class PermissionsChecker
{
	private static final String TAG = PermissionsChecker.class.getSimpleName();
	private static final String TAG_NUMBER = "1";
	private static boolean isGranted = false;

	/**
	 *
	 * @param context
	 * @param permission
	 * @return {@code true}:granted<br>{@code false}:denied
	 */
	public static boolean isPermissionGranted(Context context, @NonNull String permission)
	{
		try
		{
			switch (permission)
			{
			case READ_CONTACTS:
				return checkReadContacts(context);

			case WRITE_CONTACTS:
				return checkWriteContacts(context);

			case READ_CALL_LOG:
				return checkReadCallLog(context);

			case READ_PHONE_STATE:
				return checkReadPhoneState(context);

			case WRITE_CALL_LOG:
				return checkWriteCallLog(context);

			case READ_CALENDAR:
				return checkReadCalendar(context);

			case BODY_SENSORS:
				return checkBodySensors(context);

			case ACCESS_COARSE_LOCATION:
			case ACCESS_FINE_LOCATION:
				return checkLocation(context);

			case READ_EXTERNAL_STORAGE:
				return checkReadStorage(context);

			case WRITE_EXTERNAL_STORAGE:
				return checkWriteStorage(context);

			case RECORD_AUDIO:
				return checkRecordAudio(context);

			case READ_SMS:
				return checkReadSms(context);

			default:
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * read sms {@link android.Manifest.permission#READ_SMS}
	 * in MEIZU 5.0~6.0, just according normal phone request
	 * in XIAOMI 6.0~, need force judge
	 * in XIAOMI 5.0~6.0, not test!!!
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	private static boolean checkReadSms(Context context) throws Exception
	{
		Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
		if (cursor != null)
		{
			if (ManufacturerSupport.isForceManufacturer())
			{
				if (isNumberIndexInfoIsNull(cursor, cursor.getColumnIndex(Telephony.Sms.DATE)))
				{
					cursor.close();
					return false;
				}
			}
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * record audio {@link android.Manifest.permission#RECORD_AUDIO}, it will consume some resource!!
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static boolean checkRecordAudio(Context context) throws Exception
	{
		AudioRecordManager recordManager = new AudioRecordManager();
		recordManager.startRecord(new File(context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES), TAG + ".3gp").getPath());
		recordManager.stopRecord();
		return recordManager.getSuccess();
	}

	/**
	 * write storage {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE}
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static boolean checkWriteStorage(Context context) throws Exception
	{
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(), TAG);
		if (!file.exists())
		{
			try
			{
				return file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			return file.delete();
		}
	}

	/**
	 * read storage {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static boolean checkReadStorage(Context context) throws Exception
	{
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
		File[] files = file.listFiles();
		return files != null;
	}

	/**
	 * use location {@link android.Manifest.permission#ACCESS_FINE_LOCATION}, {@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("MissingPermission")
	private static boolean checkLocation(Context context) throws Exception
	{
		final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER))
		{
			return true;
		}
		else if (providerList.contains(LocationManager.NETWORK_PROVIDER))
		{
			return true;
		}
		else
		{
			isGranted = false;
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener()
				{
					@Override
					public void onLocationChanged(Location location)
					{
						locationManager.removeUpdates(this);
					}

					@Override
					public void onStatusChanged(String provider, int status, Bundle extras)
					{
						locationManager.removeUpdates(this);
						isGranted = true;
					}

					@Override
					public void onProviderEnabled(String provider)
					{
						locationManager.removeUpdates(this);
					}

					@Override
					public void onProviderDisabled(String provider)
					{
						locationManager.removeUpdates(this);
					}
				});
			}
			return isGranted;
		}
	}

	/**
	 * use sensors {@link android.Manifest.permission#BODY_SENSORS}
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static boolean checkBodySensors(Context context) throws Exception
	{
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener listener = new SensorEventListener()
		{
			@Override
			public void onSensorChanged(SensorEvent event)
			{

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy)
			{

			}
		};
		sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.unregisterListener(listener, sensor);
		return true;
	}

	/**
	 * read calendar {@link android.Manifest.permission#READ_CALENDAR}
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("MissingPermission")
	private static boolean checkReadCalendar(Context context) throws Exception
	{
		Cursor cursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
		if (cursor != null)
		{
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * write or delete call log {@link android.Manifest.permission#WRITE_CALL_LOG}
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("MissingPermission")
	private static boolean checkWriteCallLog(Context context) throws Exception
	{
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
		values.put(CallLog.Calls.NUMBER, TAG_NUMBER);
		values.put(CallLog.Calls.DATE, 20180101);
		values.put(CallLog.Calls.NEW, "0");
		/**
		 * write log
		 */
		contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
		/**
		 * delete log
		 */
		contentResolver.delete(CallLog.Calls.CONTENT_URI, "number = ?", new String[] { TAG_NUMBER });
		return true;
	}

	/**
	 * read phone state, {@link android.Manifest.permission#READ_PHONE_STATE}
	 * <p>
	 * in {@link com.excellence.permission.support.XIAOMI} or
	 * {@link com.excellence.permission.support.OPPO}          :
	 * -> {@link TelephonyManager#getDeviceId()} will be null if deny permission
	 * <p>
	 * in {@link com.excellence.permission.support.MEIZU}      :
	 * -> {@link TelephonyManager#getSubscriberId()} will be null if deny permission
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("MissingPermission")
	private static boolean checkReadPhoneState(Context context) throws Exception
	{
		TelephonyManager service = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (PermissionPageManager.isMEIZU())
		{
			return !TextUtils.isEmpty(service.getSubscriberId());
		}
		else if (PermissionPageManager.isXIAOMO() || PermissionPageManager.isOPPO())
		{
			return !TextUtils.isEmpty(service.getDeviceId());
		}
		else
		{
			return !TextUtils.isEmpty(service.getDeviceId()) || !TextUtils.isEmpty(service.getSubscriberId());
		}
	}

	/**
	 * read call log {@link android.Manifest.permission#READ_CALL_LOG}
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("MissingPermission")
	private static boolean checkReadCallLog(Context context) throws Exception
	{
		Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
		if (cursor != null)
		{
			if (ManufacturerSupport.isForceManufacturer())
			{
				if (isNumberIndexInfoIsNull(cursor, cursor.getColumnIndex(CallLog.Calls.NUMBER)))
				{
					cursor.close();
					return false;
				}
			}
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * write and delete contacts info {@link android.Manifest.permission#WRITE_CONTACTS}, should get read contacts permission first
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static boolean checkWriteContacts(Context context) throws Exception
	{
		if (checkReadContacts(context))
		{
			/**
			 * write info
			 */
			ContentValues values = new ContentValues();
			ContentResolver addResolver = context.getContentResolver();
			Uri rawContactUri = addResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);
			values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
			values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
			values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, TAG);
			values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, TAG_NUMBER);
			addResolver.insert(ContactsContract.Data.CONTENT_URI, values);

			/**
			 * delete info
			 */
			Uri uri = ContactsContract.RawContacts.CONTENT_URI;
			ContentResolver deleteResolver = context.getContentResolver();
			Cursor cursor = deleteResolver.query(uri, new String[] { ContactsContract.Contacts.Data._ID }, "display_name=?", new String[] { TAG }, null);
			if (cursor != null)
			{
				if (cursor.moveToFirst())
				{
					int id = cursor.getInt(0);
					deleteResolver.delete(uri, "display_name=?", new String[] { TAG });
					uri = ContactsContract.Data.CONTENT_URI;
					deleteResolver.delete(uri, "raw_contact_id=?", new String[] { String.valueOf(id) });
				}
				cursor.close();
			}
			return true;
		}
		return false;
	}

	/**
	 * read contacts {@link android.Manifest.permission#READ_CONTACTS}
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static boolean checkReadContacts(Context context) throws Exception
	{
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		if (cursor != null)
		{
			if (ManufacturerSupport.isForceManufacturer())
			{
				if (isNumberIndexInfoIsNull(cursor, cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
				{
					cursor.close();
					return false;
				}
			}
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * in {@link com.excellence.permission.support.XIAOMI}
	 * 1.denied {@link android.Manifest.permission#READ_CONTACTS} permission
	 * ---->cursor.getCount == 0
	 * 2.granted {@link android.Manifest.permission#READ_CONTACTS} permission
	 * ---->cursor.getCount return real count in contacts
	 * <p>
	 * so when there are no user or permission denied, it will return 0
	 *
	 * @param cursor
	 * @param columnIndex
	 * @return {@code true}:can't get info<br>{@code false}:info is not null
	 */
	private static boolean isNumberIndexInfoIsNull(Cursor cursor, int columnIndex)
	{
		if (cursor.getCount() > 0)
		{
			while (cursor.moveToNext())
			{
				return TextUtils.isEmpty(cursor.getString(columnIndex));
			}
			return false;
		}
		return true;
	}

	/**
	 * 检测权限
	 *
	 * @param context
	 * @param permissions
	 * @return {@code true}:授权<br>{@code false}:未授权
	 */
	public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions)
	{
		for (String permission : permissions)
		{
			boolean isGranted = isPermissionGranted(context, permission);
			if (!isGranted)
				return false;
		}
		return true;
	}

	/**
	 * 检测权限
	 *
	 * @param context
	 * @param permissions
	 * @return {@code true}:授权<br>{@code false}:未授权
	 */
	public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions)
	{
		return hasPermission(context, Arrays.asList(permissions));
	}

	/**
	 * 在{@link com.excellence.permission.IPermissionListener#onPermissionsDenied()}中，即在权限申请失败的时候，判断权限是否被拒绝-不再提示
	 *
	 * @param activity
	 * @param deniedPermissions
	 * @return {@code true}:选择“不再提示”<br>{@code false}:未选择“不再提示”
	 */
	public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions)
	{
		for (String permission : deniedPermissions)
		{
			boolean rationale = activity.shouldShowRequestPermissionRationale(permission);
			if (!rationale)
				return true;
		}
		return false;
	}

	/**
	 * 在{@link com.excellence.permission.IPermissionListener#onPermissionsDenied()}中，即在权限申请失败的时候，判断权限是否被拒绝-不再提示
	 *
	 * @param activity
	 * @param deniedPermissions
	 * @return {@code true}:选择“不再提示”<br>{@code false}:未选择“不再提示”
	 */
	public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull String... deniedPermissions)
	{
		return hasAlwaysDeniedPermission(activity, Arrays.asList(deniedPermissions));
	}
}
