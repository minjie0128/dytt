package com.google.alading.client.android;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alading.ee.R;
import com.alading.ee.db.AladingCommonDbHelper;
import com.alading.ee.fusion.FusionField;
import com.alading.ee.util.LogX;
import com.avantouch.base.AladingWebserviceMethod;
import com.avantouch.base.BaseAsyncTasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ScanResultActivity extends Activity implements
		BaseAsyncTasks.CalResponse {

	protected static final int SHOWVIEW100 = 0;
	protected static final int SHOWVIEW9999 = 1;
	private Button mScanNotNow;
	private Button mScanAnother;
	private Button mUserAtOnce;
	private RelativeLayout StatusPicLayout;
	private LinearLayout viewContentLayout;
	private LinearLayout controlUseLayout;
	private LinearLayout controlGoBackLayout;
	private ImageView mOrderStatusImage;

	private AladingCommonDbHelper mDbHelper;
	private String mUniqueId;

	private static final int MSG_USE_SUCCESS_REDIRECT = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_USE_SUCCESS_REDIRECT:
				Intent intent = new Intent();
				intent.setClass(ScanResultActivity.this, EntranceActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}

	};

	private final OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// finish();
			mHandler.sendEmptyMessage(MSG_USE_SUCCESS_REDIRECT);
		}
	};

	private final OnClickListener useListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// 记状态
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String date = df.format(new Date());
			mUniqueId = UUID.randomUUID().toString();
			mDbHelper.addScanLog(mUniqueId, mOrderMobile, mOrderNo,
					mOrderMoneyAmount, date, 0, FusionField.user.getMemberID(),
					FusionField.user.getUdid(), 0, 0, mOrderCreateTime,
					mOrderExpireTime, mOrderBarcode);

			BaseAsyncTasks asyncTask;
			String[] method = { AladingWebserviceMethod.WS_UseIntegralByBarCode };
			String[] paramNames = { "barcode", "username", "udid" };
			String[] paramValues = { mScanCode, FusionField.user.getMemberID(),
					FusionField.user.getUdid() };

			asyncTask = new BaseAsyncTasks(ScanResultActivity.this,
					ScanResultActivity.this, method[0], true, "订单正在处理...");
			asyncTask.execute(method, paramNames, paramValues);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanresult);
		txtScanCode = (TextView) findViewById(R.id.TxtScanCode);
		txtAmount = (TextView) findViewById(R.id.txtOrderAmount);
		txtDate = (TextView) findViewById(R.id.txtOrderDate);
		Intent intent = getIntent();
		mScanCode = intent.getStringExtra("ScanCode");
		LogX.trace("Alading-ScanResultActivity", "scan code: " + mScanCode);

		mScanNotNow = (Button) findViewById(R.id.b_user_not_now);
		mScanNotNow.setOnClickListener(backListener);

		mScanAnother = (Button) findViewById(R.id.b_scan_another);
		mScanAnother.setOnClickListener(backListener);

		mUserAtOnce = (Button) findViewById(R.id.b_user_at_once);
		mUserAtOnce.setOnClickListener(useListener);
		StatusPicLayout = (RelativeLayout) findViewById(R.id.layoutViewStatus);
		viewContentLayout = (LinearLayout) findViewById(R.id.layoutViewContent);
		controlUseLayout = (LinearLayout) findViewById(R.id.layoutUserControl);
		controlGoBackLayout = (LinearLayout) findViewById(R.id.layoutGoBackControl);
		viewContentLayout.setVisibility(View.INVISIBLE);
		StatusPicLayout.setVisibility(View.GONE);
		mOrderStatusImage = (ImageView) findViewById(R.id.imageView_status);
		mDbHelper = new AladingCommonDbHelper(this);
		searchBarCodeHandle(mScanCode);
	}

	private void searchBarCodeHandle(String barCode) {
		BaseAsyncTasks asyncTask;


        Log.i("aaa","searchBarCodeHandle="+barCode);

		String[] method = { AladingWebserviceMethod.WS_GetOrderInfoByBarCode };
		String[] paramNames = { "barcode", "username" };
		String[] paramValues = { barCode, FusionField.user.getMemberID() };
		asyncTask = new BaseAsyncTasks(this, this, method[0], true);
		asyncTask.execute(method, paramNames, paramValues);
	}

	@Override
	public void setWebserviceResponse(String method, Object response) {
		List<String> list = (List<String>) response;
		Log.d("Alading-ScanResult", list.toString());
		if (list.size() > 0) {
			if (method.equals(AladingWebserviceMethod.WS_GetOrderInfoByBarCode)) {
				if (this.analyzeResponse(list.get(0), "0000", false)) {
					analyzeResponseOfOrderInfo(list.get(1));
					txtScanCode.setText(mOrderNo);
					txtAmount.setText(mOrderMoneyAmount + " 元");
					txtDate.setText(mOrderExpireTime);

					if (mOrderStatus.equals("100")) {
						viewContentLayout.setVisibility(View.VISIBLE);
						controlGoBackLayout.setVisibility(View.VISIBLE);
						mScanAnother.setVisibility(View.VISIBLE);
						StatusPicLayout.setVisibility(View.VISIBLE);
						controlUseLayout.setVisibility(View.GONE);
						viewContentLayout.invalidate();
						// controlGoBackLayout.invalidate();
						// StatusPicLayout.invalidate();
						this.onRestart();
					} else if (mOrderStatus.equals("9999")
							|| mOrderStatus.equals("999")) {
						viewContentLayout.setVisibility(View.VISIBLE);
						controlUseLayout.setVisibility(View.VISIBLE);
						mScanNotNow.setVisibility(View.VISIBLE);
						mUserAtOnce.setVisibility(View.VISIBLE);
						controlGoBackLayout.setVisibility(View.GONE);
						StatusPicLayout.setVisibility(View.GONE);
						viewContentLayout.invalidate();
						this.onRestart();
						// viewContentLayout.getParent().requestTransparentRegion(viewContentLayout);
					} else if (mOrderStatus.equals("101")) {
						analyzeResponseOfOrderInfo(list.get(1));
						txtScanCode.setText(mOrderNo);
						txtAmount.setText(mOrderMoneyAmount + " 元");
						txtDate.setText(mOrderExpireTime);
						viewContentLayout.setVisibility(View.VISIBLE);
						controlGoBackLayout.setVisibility(View.VISIBLE);
						mScanAnother.setVisibility(View.VISIBLE);
						StatusPicLayout.setVisibility(View.VISIBLE);
						mOrderStatusImage.setImageResource(R.drawable.expired);
						controlUseLayout.setVisibility(View.GONE);
						viewContentLayout.invalidate();
					} else {
						invalidOrderDialog("无法识别状态：" + mOrderStatus);
					}

				} /*
				 * else if (analyzeResponse(list.get(0), "0003", false)) {
				 * analyzeResponseOfOrderInfo(list.get(1));
				 * txtScanCode.setText(mOrderNo);
				 * txtAmount.setText(mOrderMoneyAmount + " 元");
				 * txtDate.setText(mOrderExpireTime);
				 * viewContentLayout.setVisibility(View.VISIBLE);
				 * controlGoBackLayout.setVisibility(View.VISIBLE);
				 * mScanAnother.setVisibility(View.VISIBLE);
				 * StatusPicLayout.setVisibility(View.VISIBLE);
				 * mOrderStatusImage.setImageResource(R.drawable.expired);
				 * controlUseLayout.setVisibility(View.GONE);
				 * viewContentLayout.invalidate(); }
				 */else {
					invalidOrderDialog(ERRSTR);
				}
			} else if (method
					.equals(AladingWebserviceMethod.WS_UseIntegralByBarCode)) {
				if (this.analyzeResponse(list.get(0), "0000", false)) {
					Toast.makeText(this, "使用成功", Toast.LENGTH_SHORT).show();
					mHandler.sendEmptyMessageDelayed(MSG_USE_SUCCESS_REDIRECT,
							2000);
					mDbHelper.updateScanLog(mUniqueId, 1, 1);
				} else {
					// 使用失败
					invalidOrderDialog(ERRSTR);
					mDbHelper.updateScanLog(mUniqueId, 0, 1);
				}
			}
		} else {
			this.invalidOrderDialog(getString(R.string.backend_no_result));
		}
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

	// Order information
	private String mOrderNo = "";
	private String mOrderCreateTime = "";
	private String mOrderExpireTime = "";
	private float mOrderMoneyAmount = 0.0f;
	private String mOrderStatus = "";
	private String mOrderMobile = "";
	private String mOrderBarcode = "";

	private String ERRSTR = "";

	// Widgets showing order status
	private TextView txtScanCode;
	private TextView txtAmount;
	private TextView txtDate;

	private String mScanCode = "";

	private void analyzeResponseOfOrderInfo(String string) {
		// Order information: 888889131120124|2013-11-20 16:30:24|2013-11-21
		// 00:00:00|999|15|18000000000

		LogX.trace("Alading", "Order information: " + string);
		String[] contentArray = string.split("\\|");
		mOrderNo = contentArray[0];
		mOrderCreateTime = contentArray[1];
		mOrderExpireTime = contentArray[2];
		mOrderStatus = contentArray[3];
		mOrderMoneyAmount = Float.valueOf(contentArray[4]);
		mOrderMobile = contentArray[5];
		mOrderBarcode = contentArray[6];
	}

	private void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void invalidOrderDialog(String msg) {
		Builder builder = new Builder(this);
		builder.setMessage(msg);
		// builder.setTitle("提示");
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});

		builder.setNegativeButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});

		builder.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public void setWebserviceResponse(String method, Object response,
			BaseAsyncTasks asyncTask) {
		// TODO Auto-generated method stub

	}
}
