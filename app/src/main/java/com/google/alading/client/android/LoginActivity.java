package com.google.alading.client.android;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alading.ee.R;
import com.alading.ee.fusion.FusionField;
import com.avantouch.base.AladingWebserviceMethod;
import com.avantouch.base.BaseAsyncTasks;

import java.util.List;

public class LoginActivity extends Activity implements OnClickListener,
		BaseAsyncTasks.CalResponse {

	private SharedPreferences.Editor mEditor;
	private SharedPreferences mPreferences;

	private Button mLoginButton;
	private EditText mEditPassword;
	private EditText mEditUser;
	private String ERRSTR = "";
	protected String TAG = "Alading-LoginActivity";

	private static final int MSG_USE_SUCCESS_REDIRECT = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_USE_SUCCESS_REDIRECT:
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, EntranceActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {

		String mobile = mEditUser.getText().toString().trim();
		String password = mEditPassword.getText().toString().trim();
		switch (v.getId()) {
		case R.id.login_button:
			if (validateAccountInfo(mobile, password)) {
				loginHandle(mobile, password);
			}
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);

		mLoginButton = (Button) findViewById(R.id.login_button);

		mEditUser = (EditText) findViewById(R.id.v_user);
		mEditPassword = (EditText) findViewById(R.id.v_password);
		mLoginButton.setOnClickListener(this);
	}

	private void clearUserInfo() {
		mPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		mEditor = mPreferences.edit();

		mEditor.clear();
		mEditor.commit();
	}

	private void loginHandle(String userName, String password) {
		BaseAsyncTasks asyncTask;
		String[] method = { AladingWebserviceMethod.WS_UserLogin };
		String[] paramNames = { "username", "password" };
		String[] paramValues = { userName, password };
		asyncTask = new BaseAsyncTasks(LoginActivity.this, LoginActivity.this,
				method[0], true, "用户登录中...");
		asyncTask.execute(method, paramNames, paramValues);
	}

	private boolean validateAccountInfo(String mobile, String password) {
		if (/* !ValidateUtil.validateMoblie(mobile) || */TextUtils
				.isEmpty(mobile)) {
			this.showToast(getString(R.string.page_login_alert_input_correct_account));
			return false;
		} else if (TextUtils.isEmpty(password)) {
			this.showToast(getString(R.string.page_login_alert_input_password));
			return false;
		}/*
		 * else if (mobile.length() != 11) { this.showToast(getString(R.string.
		 * page_login_alertMessage_login_and_registe)); return false; } else if
		 * (password.length() < 6) {
		 * this.showToast(getString(R.string.page_login_password_insufficient));
		 * return false; }
		 */else
			return true;
	}

	protected void showToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void setWebserviceResponse(String method, Object response) {
		List<String> list = (List<String>) response;
		Log.d("Alading", "list: " + list.toString());
		if (list.size() > 0) {
			if (method.equals(AladingWebserviceMethod.WS_UserLogin)) {
				if (this.analyzeResponse(list.get(0), "0000", false)) {
					analyzeResponseOfLogin(list.get(1));
					// this.invalidOrderDialog("使用成功");
					Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
					mHandler.sendEmptyMessageDelayed(MSG_USE_SUCCESS_REDIRECT,
							100);
					FusionField.user
							.setMemberID(mEditUser.getText().toString());
				} else {
					this.invalidOrderDialog(ERRSTR);
				}
			} else {
				this.invalidOrderDialog(getString(R.string.backend_no_result));
			}
		}
	}

	private void analyzeResponseOfLogin(String string) {
		Log.d("Alading", "analyzeResponseOfLogin: " + string);
		String[] contentArray = string.split("\\|");
	}

	private boolean analyzeResponse(Object response, String rightCode,
			boolean tip) {
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
	public void setWebserviceResponse(String method, Object response,
			BaseAsyncTasks asyncTask) {
		// TODO Auto-generated method stub
		
	}
}
