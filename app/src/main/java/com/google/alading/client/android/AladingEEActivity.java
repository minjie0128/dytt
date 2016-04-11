package com.google.alading.client.android;

import com.alading.ee.R;
import com.alading.ee.fusion.FusionField;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AladingEEActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alading_ee);

		if (FusionField.user.isUserLogin()) {
			Intent intent = new Intent();
			intent.setClass(this, EntranceActivity.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

	}
}
