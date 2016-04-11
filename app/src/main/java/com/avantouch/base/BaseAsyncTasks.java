package com.avantouch.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.alading.ee.util.LogX;

public class BaseAsyncTasks extends AsyncTask<String[], Integer, Object> {
	private static final String TAG = "Alading-BaseAsyncTasks";
	private String method;
	private boolean showProgress;
	private ProgressDialog progressDialog;
	private Activity activity;
	private CalResponse callResponse;
	private String inProcessingText = "请求正在处理中，请稍等！";

	private Bundle mExtraInfo;

	public void setExtraInfo(String key, String value) {
		if (mExtraInfo == null) {
			mExtraInfo = new Bundle();
		}
		mExtraInfo.putString(key, value);
	}

	public String getExtraString(String key) {
		if (!mExtraInfo.containsKey(key)) {
			return "";
		} else {
			return mExtraInfo.getString(key);
		}
	}

	public void dismissProgress() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public BaseAsyncTasks(Activity currentActivity,
			CalResponse callwebResponse, String method, boolean showProgress) {
		super();

		this.method = method;
		this.showProgress = showProgress;
		this.activity = currentActivity;
		callResponse = callwebResponse;
	}

	public BaseAsyncTasks(Activity currentActivity,
			CalResponse callwebResponse, String method, boolean showProgress,
			String processText) {
		super();
		this.method = method;
		this.showProgress = showProgress;
		this.activity = currentActivity;
		callResponse = callwebResponse;
		inProcessingText = processText;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		if (mExtraInfo != null) {
			LogX.e(TAG, "mExtraInfo not null, return this");
			callResponse.setWebserviceResponse(method, result, this);
		} else {
			LogX.e(TAG, "mExtraInfo null, call normal callback");
			callResponse.setWebserviceResponse(method, result);
		}

		// callResponse.setWebserviceResponse(method, result);
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (showProgress)
			progressDialog = ProgressDialog
					.show(activity, "", inProcessingText);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected Object doInBackground(String[]... params) {
		// method, name, values
		Object o = WebServiceUtil.callService(params[0], params[1], params[2]);
		return o;
	}

	public interface CalResponse {
		void setWebserviceResponse(String method, Object response);

		void setWebserviceResponse(String method, Object response,
								   BaseAsyncTasks asyncTask);
	}

}
