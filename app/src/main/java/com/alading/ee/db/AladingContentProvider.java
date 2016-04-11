package com.alading.ee.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import com.alading.ee.fusion.FusionCode;

public class AladingContentProvider extends ContentProvider {

	private final int MAX_TABLES_COUNT = 40;

	private SQLiteOpenHelper mOpenHelper;

	private static final UriMatcher mUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	private static final String[] TABLE_NAMES = new String[] { DataModel.TableScanLog.TABLE_NAME };

	// make sure that these match the index of TABLE_NAMES
	private static final int URI_MATCH_SCAN_HISTORY = 0;

	// (id % MAX_TABLES_COUNT) should match the table name index
	private static final int URI_MATCH_SCAN_HISTORY_ID = 40;

	static {
		final UriMatcher matcher = mUriMatcher;
		matcher.addURI(FusionCode.DB_AUTHORITY,
				TABLE_NAMES[URI_MATCH_SCAN_HISTORY], URI_MATCH_SCAN_HISTORY);
		matcher.addURI(FusionCode.DB_AUTHORITY,
				TABLE_NAMES[URI_MATCH_SCAN_HISTORY] + "/#",
				URI_MATCH_SCAN_HISTORY_ID);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = mUriMatcher.match(uri);

		RuntimeException exception = null;

		switch (match) {
		case UriMatcher.NO_MATCH:
			exception = new IllegalArgumentException("Unknown URL");
			break;
		default:
			break;
		}

		if (null != exception) {
			throw exception;
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentResolver cr = getContext().getContentResolver();

		int count = db.delete(TABLE_NAMES[match % MAX_TABLES_COUNT], selection,
				selectionArgs);
		cr.notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		int match = mUriMatcher.match(uri);

		RuntimeException exception = null;

		switch (match) {
		case UriMatcher.NO_MATCH:
			exception = new IllegalArgumentException("Unknown URL");
			break;
		default:
			break;
		}

		if (null != exception) {
			throw exception;
		}

		Uri rtUri = null;
		long rowId = 0;
		rowId = db.insert(TABLE_NAMES[match % MAX_TABLES_COUNT], null, values);
		if (rowId > 0) {
			rtUri = ContentUris.withAppendedId(uri, rowId);
		}
		if (null != rtUri) {
			getContext().getContentResolver().notifyChange(rtUri, null);
		}
		return rtUri;
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = AladingDBHelper.getInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int match = mUriMatcher.match(uri);

		String tableName = null;

		if (UriMatcher.NO_MATCH != match) {
			tableName = TABLE_NAMES[match % MAX_TABLES_COUNT];
		}

		RuntimeException exception = null;

		Cursor cursor = null;
		boolean isVtable = false;

		switch (match) {
		case UriMatcher.NO_MATCH:
			exception = new IllegalArgumentException("Unknown URL");
			break;
		default:
			break;
		}

		if (null != exception) {
			throw exception;
		}

		if (isVtable) {
			return cursor;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		StringBuilder whereClause = new StringBuilder();

		if (match >= MAX_TABLES_COUNT) {
			String id = uri.getPathSegments().get(1);
			whereClause.append(BaseColumns._ID + " = ");
			whereClause.append(id);
		}
		if (selection != null && selection.length() > 0) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			}

			whereClause.append('(');
			whereClause.append(selection);
			whereClause.append(')');
		}

		cursor = db.query(tableName, projection, whereClause.toString(),
				selectionArgs, null, null, sortOrder, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		int match = mUriMatcher.match(uri);

		RuntimeException exception = null;

		switch (match) {
		case UriMatcher.NO_MATCH:
			exception = new IllegalArgumentException("Unknown URL");
			break;
		default:
			break;
		}

		if (null != exception) {
			throw exception;
		}

		if (match >= MAX_TABLES_COUNT) {
			StringBuilder sb = new StringBuilder();
			if (selection != null && selection.length() > 0) {
				sb.append("( ");
				sb.append(selection);
				sb.append(" ) AND ");
			}
			String id = uri.getPathSegments().get(1);
			sb.append(BaseColumns._ID + " = ");
			sb.append(id);
			selection = sb.toString();
		}

		ContentResolver cr = getContext().getContentResolver();
		int rt = db.update(TABLE_NAMES[match % MAX_TABLES_COUNT], values,
				selection, selectionArgs);
		cr.notifyChange(uri, null);
		return rt;
	}

}
