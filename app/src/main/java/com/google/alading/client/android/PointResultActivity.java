package com.google.alading.client.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alading.ee.R;

public class PointResultActivity extends BaseActivity {

	protected String TAG = "Alading-PointResultActivity";

	private TextView mOrderMoney;
	private TextView mOrderNumber;
	private TextView mOrderTime;
	private TextView mOrderExpiration;
	private TextView mOrderMobile;

	private Button mHome;
	private Button mBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pointresult);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			mOrderMoney.setText(getString(R.string.order_money,
					bundle.getString("order_money")));
			mOrderNumber.setText(getString(R.string.order_number,
					bundle.getString("order_number")));
			mOrderTime.setText(getString(R.string.order_time,
					bundle.getString("order_time")));
			mOrderExpiration.setText(getString(R.string.order_expiration,
					bundle.getString("order_expiration")));
			mOrderMobile.setText(getString(R.string.order_mobile,
					bundle.getString("order_mobile")));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_home:
			jumpToPage(EntranceActivity.class, true);
			break;

		case R.id.b_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void initWidgetEvent() {
		super.initWidgetEvent();
		addWidgetEventListener(mHome);
		addWidgetEventListener(mBack);
	}

	@Override
	protected void initWidgetProperty() {
		super.initWidgetProperty();

		mOrderMoney = (TextView) findViewById(R.id.t_order_money);
		mOrderNumber = (TextView) findViewById(R.id.t_order_number);
		mOrderTime = (TextView) findViewById(R.id.t_order_time);
		mOrderExpiration = (TextView) findViewById(R.id.t_order_expiration);
		mOrderMobile = (TextView) findViewById(R.id.t_order_mobile);
		mHome = (Button) findViewById(R.id.b_home);
		mBack = (Button) findViewById(R.id.b_back);
	}
}
