package com.google.alading.client.android;

import android.app.Activity;
import android.os.Bundle;

import com.alading.ee.view.AndroidBarcodeView;

public class AndroidBarcodeTestActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidBarcodeView view = new AndroidBarcodeView(this);

		setContentView(view);
	}
}