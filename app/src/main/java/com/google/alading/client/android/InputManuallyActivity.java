package com.google.alading.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.alading.ee.R;

public class InputManuallyActivity extends Activity implements
		View.OnClickListener {
	private EditText mBarCode;
	private TextView mInputerBits;
	private Button mConfirmButton;
	private ImageView mSampleBar;
	private int mRequiredBits;

	private static final int PRODUCT_BAR_CODE = 0;
	private static final int PREPAID_CARD_NUMBER = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_manually);

		mBarCode = (EditText) findViewById(R.id.code_edit);
		mBarCode.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int inType = mBarCode.getInputType();
				// disable soft input
				mBarCode.setInputType(InputType.TYPE_NULL);
				// call native handler
				mBarCode.onTouchEvent(event);
				// restore input type
				mBarCode.setInputType(inType);
				mBarCode.setSelection(mBarCode.getText().length());
				return true;

			}
		});
		mBarCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mInputerBits.setText(getString(R.string.already_input_bits,
						mBarCode.getText().length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String code = mBarCode.getText().toString();
				if (code == null || code.length() < mRequiredBits) {
					mConfirmButton.setEnabled(false);
				} else {
					mConfirmButton.setEnabled(true);
				}
			}
		});
		mInputerBits = (TextView) findViewById(R.id.inputed_bits);
		mInputerBits.setText(getString(R.string.already_input_bits, 0));
		setupDial();

		mConfirmButton = (Button) findViewById(R.id.confirm_button);
		mConfirmButton.setOnClickListener(this);

		mSampleBar = (ImageView) findViewById(R.id.sample_bar_iamge);
		int inputType = getIntent().getExtras().getInt("input_type");
		if (inputType == PREPAID_CARD_NUMBER) {
			mSampleBar.setImageDrawable(getResources().getDrawable(
					R.drawable.prepaid_card));
			mRequiredBits = 16;
			mBarCode.setHint(R.string.input_prepaid_card_number);
			mBarCode.setFilters(new InputFilter[] { new InputFilter.LengthFilter(mRequiredBits) });
		} else if (inputType == PRODUCT_BAR_CODE) {
			mSampleBar.setImageDrawable(getResources().getDrawable(
					R.drawable.bar_code_sample));
			mRequiredBits = 1;
		}
	}

	private void setupDial() {
		findViewById(R.id.Button1).setOnClickListener(this);
		findViewById(R.id.Button2).setOnClickListener(this);
		findViewById(R.id.Button3).setOnClickListener(this);
		findViewById(R.id.Button4).setOnClickListener(this);
		findViewById(R.id.Button5).setOnClickListener(this);
		findViewById(R.id.Button6).setOnClickListener(this);
		findViewById(R.id.Button7).setOnClickListener(this);
		findViewById(R.id.Button8).setOnClickListener(this);
		findViewById(R.id.Button9).setOnClickListener(this);
		findViewById(R.id.Button10).setOnClickListener(this);
		findViewById(R.id.Button11).setOnClickListener(this);
		findViewById(R.id.Button12).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Button1:
			keyPressed(KeyEvent.KEYCODE_1);
			break;
		case R.id.Button2:
			keyPressed(KeyEvent.KEYCODE_2);
			break;
		case R.id.Button3:
			keyPressed(KeyEvent.KEYCODE_3);
			break;
		case R.id.Button4:
			keyPressed(KeyEvent.KEYCODE_4);
			break;
		case R.id.Button5:
			keyPressed(KeyEvent.KEYCODE_5);
			break;

		case R.id.Button6:
			keyPressed(KeyEvent.KEYCODE_6);
			break;
		case R.id.Button7:
			keyPressed(KeyEvent.KEYCODE_7);
			break;
		case R.id.Button8:
			keyPressed(KeyEvent.KEYCODE_8);
			break;
		case R.id.Button9:
			keyPressed(KeyEvent.KEYCODE_9);
			break;
		case R.id.Button10:
			break;
		case R.id.Button11:
			keyPressed(KeyEvent.KEYCODE_0);
			break;
		case R.id.Button12:
			keyPressed(KeyEvent.KEYCODE_DEL);
			break;
		case R.id.confirm_button:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this.getBaseContext(),
					ScanResultActivity.class.getName());
			intent.putExtra("ScanCode", mBarCode.getText().toString());

			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void keyPressed(int keyCode) {
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		mBarCode.onKeyDown(keyCode, event);

	}
}
