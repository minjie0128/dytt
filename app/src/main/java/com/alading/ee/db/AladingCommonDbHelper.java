package com.alading.ee.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.alading.ee.db.DataModel.TableScanLog;
import com.alading.ee.vo.ScanLog;

public class AladingCommonDbHelper {

	private AladingDBHelper db = null;

	private ContentResolver mContentResolver;

	private Context mContext;

	private static final String TAG = "Alading-AladingCommonDbHelper";

	public AladingCommonDbHelper(Context context) {
		if (db == null) {
			db = AladingDBHelper.getInstance(context);
		}
		mContext = context;
		mContentResolver = mContext.getContentResolver();
	}

	/**
	 * Add an exchange record.
	 * 
	 * @param id
	 *            - This record's unique id.
	 * @param mobile
	 *            - The owner's mobile
	 * @param number
	 *            - The scanned bar code.
	 * @param amount
	 *            - The face value of the voucher
	 * @param exchangeTime
	 *            - The time when operator click the exchange button.
	 * @param result
	 *            - Exchange result, either success or failure.
	 * @param userName
	 *            - The user name correspond to store.
	 * @param udid
	 *            - The cell phone's unique id.
	 * @param logStatus
	 *            - If the log was successfully recorded.
	 * @param uploadStatus
	 *            - Indicate if the log is uploaded.
	 */
	public void addScanLog(String id, String mobile, String number,
			float amount, String exchangeTime, int result, String userName,
			String udid, int logStatus, int uploadStatus, String orderTime,
			String expirationTime, String barcode) {
		try {
			ContentValues values = new ContentValues();
			values.put(TableScanLog._ID, id);
			values.put(TableScanLog.SCAN_LOG_MOBILE, mobile);
			values.put(TableScanLog.SCAN_LOG_NUMBER, number);
			values.put(TableScanLog.SCAN_LOG_MONEY_AMOUNT, amount);
			values.put(TableScanLog.SCAN_LOG_TIME, exchangeTime);
			values.put(TableScanLog.SCAN_LOG_RESULT, result);
			values.put(TableScanLog.SCAN_LOG_USER_NAME, userName);
			values.put(TableScanLog.SCAN_LOG_UDID, udid);
			values.put(TableScanLog.SCAN_LOG_STATUS, logStatus);
			values.put(TableScanLog.SCAN_LOG_UPLOAD_STATUS, uploadStatus);
			values.put(TableScanLog.SCAN_LOG_ORDER_EXCHANGE_TIME, orderTime);
			values.put(TableScanLog.SCAN_LOG_ORDER_EXPIRATION_TIME,
					expirationTime);
			values.put(TableScanLog.SCAN_LOG_ORDER_BARCODE, barcode);

			mContentResolver.insert(TableScanLog.CONTENT_URI, values);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	/**
	 * Update scan log's fields
	 * 
	 * @param id
	 *            - Log's unique id.
	 * @param logResult
	 *            - If the voucher is successfully used.
	 * @param logStatus
	 *            - If the log is successfully recorded.
	 */
	public void updateScanLog(String id, int logResult, int logStatus) {
		String where = TableScanLog._ID + " = '" + id + "'";

		try {
			ContentValues values = new ContentValues();
			values.put(TableScanLog.SCAN_LOG_RESULT, logResult);
			values.put(TableScanLog.SCAN_LOG_STATUS, logStatus);

			mContentResolver.update(TableScanLog.CONTENT_URI, values, where,
					null);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	/**
	 * Update the log's upload status to to uploaded
	 * 
	 * @param id
	 */
	public void updateScanLogToUploaded(String id) {
		String where = TableScanLog._ID + " = '" + id + "'";

		try {
			ContentValues values = new ContentValues();
			values.put(TableScanLog.SCAN_LOG_UPLOAD_STATUS, 1);

			mContentResolver.update(TableScanLog.CONTENT_URI, values, where,
					null);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	/**
	 * Update the log's upload status to to uploaded
	 * 
	 * @param id
	 */
	public void updateScanLogToNotUploaded() {
		try {
			ContentValues values = new ContentValues();
			values.put(TableScanLog.SCAN_LOG_UPLOAD_STATUS, 0);

			mContentResolver.update(TableScanLog.CONTENT_URI, values, null,
					null);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	public List<ScanLog> getAllNotUploadedLogs() {
		List<ScanLog> list = new ArrayList<ScanLog>();

		String where = TableScanLog.SCAN_LOG_UPLOAD_STATUS + " = 0";
		Cursor cursor = null;
		cursor = mContentResolver.query(TableScanLog.CONTENT_URI, null, where,
				null, null);

		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			ScanLog log;
			do {
				log = createLogFromCursor(cursor);
				if (log != null) {
					list.add(log);
				}
			} while (cursor.moveToNext());

			cursor.close();
		}

		return list;
	}

	private ScanLog createLogFromCursor(Cursor cursor) {
		ScanLog order = new ScanLog();

		order.setmAmount(cursor.getFloat(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_MONEY_AMOUNT)));
		order.setmLogResult(cursor.getInt(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_RESULT)));
		order.setmLogStatus(cursor.getInt(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_STATUS)));
		order.setmLogTime(cursor.getString(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_TIME)));
		order.setmMobile(cursor.getString(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_MOBILE)));
		order.setmNumber(cursor.getString(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_NUMBER)));
		order.setmUdid(cursor.getString(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_UDID)));
		order.setmUniqueId(cursor.getString(cursor
				.getColumnIndex(TableScanLog._ID)));
		order.setmUploadStatus(cursor.getInt(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_UPLOAD_STATUS)));
		order.setmUserName(cursor.getString(cursor
				.getColumnIndex(TableScanLog.SCAN_LOG_USER_NAME)));

		return order;
	}
}
