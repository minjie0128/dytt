package com.alading.ee.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alading.ee.db.DataModel.TableScanLog;

/**
 * This class is used for the creation and update of application database, as
 * well as some basic database operations.
 */
public class AladingDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "alading.db";

	private static final int DATABASE_VERSION = 1;

	private static AladingDBHelper mDbHelper;

	public static synchronized AladingDBHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new AladingDBHelper(context.getApplicationContext());
		}

		return mDbHelper;
	}

	public AladingDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createScanLogTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private void createScanLogTable(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + TableScanLog.TABLE_NAME
				+ "(" + TableScanLog._ID + " VARCHAR PRIMARY KEY  NOT NULL,"
				+ TableScanLog.SCAN_LOG_NUMBER + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_MONEY_AMOUNT + " FLOAT, "
				+ TableScanLog.SCAN_LOG_TIME + " BOOL, "
				+ TableScanLog.SCAN_LOG_RESULT + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_USER_NAME + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_UDID + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_STATUS + " INTEGER, "
				+ TableScanLog.SCAN_LOG_MOBILE + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_ORDER_EXCHANGE_TIME + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_ORDER_EXPIRATION_TIME + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_ORDER_BARCODE + " VARCHAR, "
				+ TableScanLog.SCAN_LOG_UPLOAD_STATUS + " INTEGER)");
	}
}
