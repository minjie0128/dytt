package com.google.alading.client.android;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alading.ee.R;
import com.alading.ee.db.AladingCommonDbHelper;
import com.alading.ee.db.DataModel.TableScanLog;
import com.alading.ee.fusion.FusionField;
import com.alading.ee.util.LogX;
import com.alading.ee.view.PopupWindowView;
import com.alading.ee.view.XListView;
import com.alading.ee.vo.ScanLog;
import com.avantouch.base.AladingWebserviceMethod;
import com.avantouch.base.BaseAsyncTasks;

public class ScanHistoryActivity extends BaseActivity implements
		BaseAsyncTasks.CalResponse {
	private Context mContext;

	private RadioGroup mGroup;
	private RadioButton mNotUploaded;
	private RadioButton mUploaded;

	private ImageView mSortByUser;
	private ImageView mSortByNumber;
	private ImageView mSortByMoney;
	private ImageView mSortByDate;
	private Button mUpload;

	private XListView mListView;
	private LayoutInflater mInflater;

	private int mUserSortStatus = 0;
	private int mNumberSortStatus = 0;
	private int mMoneySortStatus = 0;
	private int mDateSortStatus = 0;

	private String ERRSTR = "";

	private AladingCommonDbHelper mDbHelper;
	/**
	 * Received responses
	 */
	private int mCount;

	/**
	 * Total records to be uploaded
	 */
	private int mTotalRecords;

	private PopupWindowView mPopupView;

	private static HashMap<String, Integer> mSortStatus = new HashMap<String, Integer>();
	static {
		mSortStatus.put(TableScanLog.SCAN_LOG_MOBILE, 0);
		mSortStatus.put(TableScanLog.SCAN_LOG_NUMBER, 0);
		mSortStatus.put(TableScanLog.SCAN_LOG_MONEY_AMOUNT, 0);
		mSortStatus.put(TableScanLog.SCAN_LOG_TIME, 0);
	}

	private static HashMap<String, ImageView> mSortWidgets = new HashMap<String, ImageView>();

	private static HashMap<String, Integer> mUploadStatus = new HashMap<String, Integer>();

	private boolean mShowUploaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_scan_history);
		super.onCreate(savedInstanceState);
		String where = TableScanLog.SCAN_LOG_UPLOAD_STATUS + " ='"
				+ (mShowUploaded ? 1 : 0) + "'";
		Cursor c = getContentResolver().query(TableScanLog.CONTENT_URI, null,
				where, null, null);
		LogAdapter adapter = new LogAdapter(this, c);
		mListView.setAdapter(adapter);

		mDbHelper = new AladingCommonDbHelper(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.i_sort_by_user:
			sortByUser(TableScanLog.SCAN_LOG_MOBILE);
			break;
		case R.id.i_sort_by_number:
			sortByUser(TableScanLog.SCAN_LOG_NUMBER);
			break;
		case R.id.i_sort_by_money:
			sortByUser(TableScanLog.SCAN_LOG_MONEY_AMOUNT);
			break;
		case R.id.i_sort_by_date:
			sortByUser(TableScanLog.SCAN_LOG_TIME);
			break;
		case R.id.b_upload:
			uploadScanHistory();
			break;
		case R.id.b_back:
			 finish();
//			mDbHelper.updateScanLogToNotUploaded();
			break;
		default:
			break;
		}
	}

	private void uploadScanHistory() {
		List<ScanLog> list = mDbHelper.getAllNotUploadedLogs();
		mTotalRecords = list.size();
		LogX.trace(TAG, "to be uploded records: " + list.size());
		for (int i = 0; i < list.size(); i++) {
			ScanLog log = list.get(i);
			mUploadStatus.put(log.getmUniqueId(), 0);

			BaseAsyncTasks asyncTask;
			String[] method = { AladingWebserviceMethod.WSN_UPLOAD_SCAN_LOG };
			String[] paramNames = { "username", "udid", "ordernumber", "mobile",
					"exctime", "excresult", "logstatus" };
			String[] paramValues = { log.getmUserName(),
					FusionField.user.getUdid(), log.getmNumber(),
					log.getmMobile(), log.getmLogTime(),
					log.getmLogResult() + "", log.getmLogStatus() + "" };
			boolean showProgress = i == 0;
			asyncTask = new BaseAsyncTasks(this, this, method[0], showProgress);
			asyncTask.setExtraInfo("unique_id", log.getmUniqueId());
			asyncTask.execute(method, paramNames, paramValues);
		}
	}

	private void sortByUser(String sortField) {
		Iterator iter = mSortStatus.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			if (sortField.equals(key)) {
				int val = (Integer) entry.getValue();
				if (val == 0) {
					val++;
				} else if (val == 1) {
					val++;
				} else {
					val--;
				}
				mSortStatus.put(key, val);
				setSortWidgetStatus(key, val);
			} else {
				mSortStatus.put(key, 0);
				setSortWidgetStatus(key, 0);
			}
		}

		int status = mSortStatus.get(sortField);
		String order = status == 1 ? " DESC" : " ASC";

		String where = TableScanLog.SCAN_LOG_UPLOAD_STATUS + " ='"
				+ (mShowUploaded ? 1 : 0) + "'";
		Cursor c = getContentResolver().query(TableScanLog.CONTENT_URI, null,
				where, null, sortField + order);
		LogAdapter adapter = new LogAdapter(this, c);
		mListView.setAdapter(adapter);
	}

	private void setSortWidgetStatus(String sortField, int status) {
		ImageView sortWidget = mSortWidgets.get(sortField);
		if (status == 0) {
			sortWidget.setImageResource(R.drawable.sort);
		} else if (status == 1) {
			sortWidget.setImageResource(R.drawable.sort_on_down);
		} else if (status == 2) {
			sortWidget.setImageResource(R.drawable.sort_on_up);
		}
	}

	@Override
	protected void initWidgetEvent() {
		super.initWidgetEvent();
		mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (mNotUploaded.getId() == checkedId) {
					mShowUploaded = false;
				} else {
					mShowUploaded = true;
				}
				String where = TableScanLog.SCAN_LOG_UPLOAD_STATUS + " ='"
						+ (mShowUploaded ? 1 : 0) + "'";
				Cursor c = getContentResolver().query(TableScanLog.CONTENT_URI,
						null, where, null, null);
				LogAdapter adapter = new LogAdapter(ScanHistoryActivity.this, c);
				mListView.setAdapter(adapter);
			}
		});
		addWidgetEventListener(mSortByUser);
		addWidgetEventListener(mSortByNumber);
		addWidgetEventListener(mSortByMoney);
		addWidgetEventListener(mSortByDate);
		addWidgetEventListener(mUpload);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				HeaderViewListAdapter headerViewAdapter = (HeaderViewListAdapter) arg0
						.getAdapter();
				Cursor cursor = ((CursorAdapter) headerViewAdapter
						.getWrappedAdapter()).getCursor();
				cursor.moveToPosition(arg2 - 1);
				String orderMoney = cursor.getString(cursor
						.getColumnIndex(TableScanLog.SCAN_LOG_MONEY_AMOUNT));
				String orderNumber = cursor.getString(cursor
						.getColumnIndex(TableScanLog.SCAN_LOG_NUMBER));
				String orderTime = cursor.getString(cursor
						.getColumnIndex(TableScanLog.SCAN_LOG_ORDER_EXCHANGE_TIME));
				String orderExpiration = cursor.getString(cursor
						.getColumnIndex(TableScanLog.SCAN_LOG_ORDER_EXPIRATION_TIME));
				String orderMobile = cursor.getString(cursor
						.getColumnIndex(TableScanLog.SCAN_LOG_MOBILE));
				String orderBarcode = cursor.getString(cursor
						.getColumnIndex(TableScanLog.SCAN_LOG_ORDER_BARCODE));

				Bundle bundle = new Bundle();
				bundle.putString("order_money", orderMoney);
				bundle.putString("order_number", orderNumber);
				bundle.putString("order_time", orderTime);
				bundle.putString("order_expiration", orderExpiration);
				bundle.putString("order_mobile", orderMobile);
				bundle.putString("order_barcode", orderBarcode);

				jumpToPage(PointResultActivity.class, bundle);
			}
		});
	}

	@Override
	protected void initWidgetProperty() {
		super.initWidgetProperty();
		mGroup = (RadioGroup) findViewById(R.id.r_radio);
		mUploaded = (RadioButton) findViewById(R.id.r_uploaded);
		mNotUploaded = (RadioButton) findViewById(R.id.r_unuploaded);
		mListView = (XListView) findViewById(R.id.l_exchange_record);
		mListView.setEmptyView(findViewById(R.id.t_no_record));
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setDivider(getResources().getDrawable(
				R.drawable.bg_light_gray));
		mListView.setDividerHeight(10);
		mListView.setSelector(new StateListDrawable());

		mSortByUser = (ImageView) findViewById(R.id.i_sort_by_user);
		mSortByNumber = (ImageView) findViewById(R.id.i_sort_by_number);
		mSortByMoney = (ImageView) findViewById(R.id.i_sort_by_money);
		mSortByDate = (ImageView) findViewById(R.id.i_sort_by_date);

		mSortWidgets.put(TableScanLog.SCAN_LOG_MOBILE, mSortByUser);
		mSortWidgets.put(TableScanLog.SCAN_LOG_NUMBER, mSortByNumber);
		mSortWidgets.put(TableScanLog.SCAN_LOG_MONEY_AMOUNT, mSortByMoney);
		mSortWidgets.put(TableScanLog.SCAN_LOG_TIME, mSortByDate);

		mUpload = (Button) findViewById(R.id.b_upload);
	}

	private class LogAdapter extends CursorAdapter {

		public LogAdapter(Context context, Cursor c) {
			super(context, c);
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		public LogAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView logMobile = (TextView) view
					.findViewById(R.id.t_log_mobile);
			TextView logNumber = (TextView) view
					.findViewById(R.id.t_log_number);
			TextView logAmount = (TextView) view
					.findViewById(R.id.t_log_money_amount);
			TextView logTime = (TextView) view.findViewById(R.id.t_log_time);

			logMobile.setText(getString(R.string.scan_record_user, cursor
					.getString(cursor
							.getColumnIndex(TableScanLog.SCAN_LOG_MOBILE))));
			logNumber.setText(getString(R.string.scan_record_number, cursor
					.getString(cursor
							.getColumnIndex(TableScanLog.SCAN_LOG_NUMBER))));
			logAmount
					.setText(getString(
							R.string.scan_record_money,
							cursor.getString(cursor
									.getColumnIndex(TableScanLog.SCAN_LOG_MONEY_AMOUNT))));
			logTime.setText(getString(R.string.scan_record_time, cursor
					.getString(cursor
							.getColumnIndex(TableScanLog.SCAN_LOG_TIME))));

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return mInflater.inflate(R.layout.log_item, parent, false);
		}

	}

	private void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private boolean analyzeResponse(Object response, String rightCode,
			boolean tip) {
		Log.d("Alading-ScanResult", response.toString());
		String content = response.toString();
		String[] contentArray = content.split("\\|");
		if (tip)
			this.DisplayToast(contentArray[1]);
		if (contentArray[0].equals(rightCode)) {
			return true;
		} else {
			ERRSTR = contentArray[1];
			Log.d("AladingEE", "ERRSTR:" + ERRSTR);
			return false;
		}
	}

	@Override
	public void setWebserviceResponse(String method, Object response) {

	}

	@Override
	public void setWebserviceResponse(String method, Object response,
			BaseAsyncTasks asyncTask) {
		List<String> list = (List<String>) response;
		if (list.size() > 0) {
			if (method.equals(AladingWebserviceMethod.WSN_UPLOAD_SCAN_LOG)) {
				mCount++;
				String id = asyncTask.getExtraString("unique_id");
				LogX.trace(TAG, "unique id: " + id);
				if (analyzeResponse(list.get(0), "0000", false)) {
					if (id != null && !id.equals("")) {
						mUploadStatus.put(id, 1);
						mDbHelper.updateScanLogToUploaded(id);
					}
				} else {
					if (id != null && !id.equals("")) {
						mUploadStatus.put(id, 0);
					}
				}
				if (mCount == mTotalRecords) {
					asyncTask.dismissProgress();

					int failed = 0;
					Iterator iter = mUploadStatus.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						String key = (String) entry.getKey();
						int result = mUploadStatus.get(key);
						if (result == 0) {
							failed++;
						}
					}

					mPopupView = new PopupWindowView(this, this);
					if (failed == 0) {
						mPopupView.setText("上传日志成功！");
					} else {
						mPopupView.setText("有" + failed + "位用户提交失败，请稍后重试");
					}

					mPopupView.showAtLocation(findViewById(R.id.r_root),
							Gravity.CENTER, 0, 0);

					mUploadStatus.clear();
					mCount = 0;
					mTotalRecords = 0;
				}
			}
		}
	}
}
