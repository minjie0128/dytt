package com.alading.ee;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.alading.ee.fusion.FusionCode;
import com.alading.ee.fusion.FusionField;
import com.alading.ee.util.LogX;
import com.google.alading.client.android.AladingUser;

import java.util.UUID;

public class AladingEEApp extends Application {

	private static AladingEEApp sAladingApp;
	private final String TAG = "AladingApp";

	synchronized public static AladingEEApp getApplication() {
		return sAladingApp;
	}

	private static Handler mAppHandler = new AppHandler();

	static class AppHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (sAladingApp == null || msg == null) {
				return;
			}

			switch (msg.what) {
			case FusionCode.MSG_NO_SDCARD:
				break;
			default:
				break;
			}

		}
	}

	public static synchronized Handler getAppHandler() {
		if (mAppHandler == null) {
			mAppHandler = new AppHandler();
		}

		return mAppHandler;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		sAladingApp = this;

		initSystemParams();
		initNetworkState();

		initUser();
	}

	private void initUser() {
		FusionField.user = new AladingUser();
		FusionField.user.setUdid(getUdid());
	}

	private String getUdid() {
		final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice;
		final String androidId;
		tmDevice = tm.getDeviceId();
		androidId = android.provider.Settings.Secure.getString(
				getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				tmDevice.hashCode() << 32);
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}

	private void initSystemParams() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		FusionField.devicePixelsWidth = dm.widthPixels;
		FusionField.devicePixelsHeight = dm.heightPixels;
		FusionField.deviceDensity = dm.density;

		LogX.trace(TAG, "Device Density: " + FusionField.deviceDensity);
		LogX.trace(TAG, "Device Dimension in Px: "
				+ FusionField.devicePixelsWidth + "*"
				+ FusionField.devicePixelsHeight);

		FusionField.currentVersion = getCurrentAppVersion(this);
		FusionField.DATA_BASE_FILE_NAME = getDatabasePath(
				FusionCode.DB_FILE_NAME).getAbsolutePath();
		LogX.trace(TAG, "External storage path: " + FusionCode.SD_CARD_DIR);
	}

	private void initNetworkState() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();

		if (activeNetworkInfo != null) {
			FusionField.networkConntected = activeNetworkInfo.isConnected();
			LogX.trace(TAG, "current active network = "
					+ FusionField.networkConntected);
		}
	}

	private String getCurrentAppVersion(Context context) {
		String pName = FusionCode.PKG_NAME;
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					pName, PackageManager.GET_CONFIGURATIONS);
			String versionName = pinfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public boolean checkDbFileExists() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(
					FusionField.DATA_BASE_FILE_NAME, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

}
