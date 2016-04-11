package com.google.alading.client.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alading.ee.R;
import com.alading.ee.util.LogX;
import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class BaseActivity extends FragmentActivity implements OnClickListener {

	protected Button mBack;
	protected Button mXFunc;
	protected TextView mServiceTitle;

	protected ProgressDialog progressDialog;

	protected String TAG = "Alading-BaseActivity";

	private static final int MSG_DISMISS_PROGRESS_DIALOG = 0;

	private static final int MSG_SHOW_TOAST = 1;
	private static final int MSG_SHOW_PROGRESS_DIALOG = 2;

	public String getClassName() {
		String clazzName = getClass().getName();
		return clazzName;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_back:
			finish();
			break;

		default:
			break;
		}
	}

	private boolean isDeviceServiceLogined = false;

	protected boolean isDeviceServiceLogined() {
		return isDeviceServiceLogined;
	}

	public void 	bindDeviceService() {
		try {
			isDeviceServiceLogined = false;
			DeviceService.login(this);
			isDeviceServiceLogined = true;
		} catch (RequestException e) {
			// Rebind after a few milliseconds,
			// If you want this application keep the right of the device service


			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					bindDeviceService();
				}
			});
			e.printStackTrace();
		} catch (ServiceOccupiedException e) {
			e.printStackTrace();
		} catch (ReloginException e) {
			e.printStackTrace();
		} catch (UnsupportMultiProcess e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initCaughtException();
		initWidgetProperty();
		initWidgetEvent();
		LogX.trace(TAG, "Page: " + getClassName());
	}


	public void unbindDeviceService() {
		DeviceService.logout();
		isDeviceServiceLogined = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initCaughtException() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {

				Writer result = new StringWriter();
				PrintWriter printWriter = new PrintWriter(result);
				ex.printStackTrace(printWriter);
				String stacktrace = result.toString();

				Log.e(TAG, "_____" + ex.toString() + " " + stacktrace);

				Log.d(TAG, ex.getLocalizedMessage());
				System.out.println("uncaughtException");

				new Thread() {

					@Override
					public void run() {
						Looper.prepare();
						Builder builder = new Builder(
								BaseActivity.this);
						builder.setMessage("阿拉订企业版已崩溃");
						builder.setTitle("提示");
						builder.setNegativeButton(
								"确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										android.os.Process
												.killProcess(android.os.Process
														.myPid());
									}
								});
						builder.create().show();
						Looper.loop();
					}
				}.start();

			}

		});
	}

	protected void addWidgetEventListener(View v) {
		if (v != null)
			v.setOnClickListener(this);
	}

	protected void dismissProgressBar() {
		Message message = Message.obtain();
		message.what = MSG_DISMISS_PROGRESS_DIALOG;
		mBaseHandler.sendMessage(message);
	}

	protected void initWidgetEvent() {

		if (mBack != null) {
			mBack.setOnClickListener(this);
		}

		if (mXFunc != null) {
			mXFunc.setOnClickListener(this);
		}
	}

	protected void initWidgetProperty() {
		mBack = (Button) findViewById(R.id.b_back);
	}

	protected void setSingleValueOfReceiptDetail(int componentId, int stringId,
			Object value, int color) {
		TextView receiptTitle = (TextView) findViewById(componentId);
		String text4 = String.format(getResources().getString(stringId), value);
		int index;
		index = text4.indexOf(value.toString());

		SpannableStringBuilder style2 = new SpannableStringBuilder(text4);
		style2.setSpan(new ForegroundColorSpan(color), index, index
				+ value.toString().length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		receiptTitle.setText(style2);
	}

	protected void showProgressDialog(String title) {
		Message message = Message.obtain();
		message.what = MSG_SHOW_PROGRESS_DIALOG;
		message.obj = title;
		mBaseHandler.sendMessage(message);
	}

	protected void showProgressDialog() {
		showProgressDialog("");
	}

	protected void showToast(String text) {
		Message message = Message.obtain();
		message.what = MSG_SHOW_TOAST;
		message.obj = text;
		mBaseHandler.sendMessage(message);
	}

	/**
	 * General method for starting a new activity either for result or not.
	 * 
	 * @param activityClass
	 *            The activity to start
	 * @param bundle
	 *            Extra information with this intent.
	 * @param isReturn
	 *            If start for result or not
	 * @param requestCode
	 *            The request code.
	 * @param isFinish
	 *            If finish self after start.
	 */
	public void jumpToPage(Class<?> activityClass, Bundle bundle,
			boolean isReturn, int requestCode, boolean isFinish) {
		if (activityClass == null) {
			return;
		}

		Intent intent = new Intent();
		intent.setClass(this, activityClass);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		if (isReturn) {
			startActivityForResult(intent, requestCode);
		} else {
			startActivity(intent);
		}

		if (isFinish) {
			finish();
		}
	}

	public void jumpToPage(Class<?> activityClass) {
		jumpToPage(activityClass, null, false, 0, false);
	}

	public void jumpToPage(Class<?> activityClass, boolean isFinish) {
		jumpToPage(activityClass, null, false, 0, isFinish);
	}

	public void jumpToPage(Class<?> activityClass, Bundle bundle) {
		jumpToPage(activityClass, bundle, false, 0, false);
	}

	public void jumpToPage(Class<?> activityClass, Bundle bundle,
			boolean isFinish) {
		jumpToPage(activityClass, bundle, false, 0, isFinish);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (progressDialog != null) {
			mBaseHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	protected Handler mBaseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DISMISS_PROGRESS_DIALOG:
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				break;
			case MSG_SHOW_TOAST:
				String text = (String) msg.obj;
				Toast toast = Toast.makeText(BaseActivity.this, text,
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			case MSG_SHOW_PROGRESS_DIALOG:
				if (progressDialog == null) {
					String title = (String) msg.obj;
					progressDialog = ProgressDialog.show(BaseActivity.this,
							title, getString(R.string.general_in_process));
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
}
