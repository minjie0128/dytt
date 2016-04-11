package com.google.alading.client.android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alading.ee.AladingEEApp;
import com.alading.ee.fusion.FusionCode;
import com.alading.ee.util.LogX;

import android.content.Context;
import android.content.SharedPreferences;

public class AladingUser {

	private String mMemberID;
	private String mMobile;
	private String mUdid;
	private String mRealName;
	private String mTelephone;
	private String mBirthday;
	private String mEmail;
	private String mSex;
	private float mMoney;

	private boolean mUserEdmBound;
	private boolean mUserEdmPushOn;
	private String mUserEdmEmail;

	private SharedPreferences mUserPreferences;

	public static final String USER_ID_KEY = "user_id";
	public static final String USER_MOBILE_KEY = "user_mobile";
	public static final String USER_UDID_KEY = "user_udid";
	public static final String USER_NAME_KEY = "user_name";
	public static final String USER_PHONE_KEY = "user_phone";
	public static final String USER_BIRTHDAY_KEY = "user_birthday";
	public static final String USER_EMAIL_KEY = "user_email";
	public static final String USER_SEX_KEY = "user_sex";
	public static final String USER_MONEY_KEY = "user_money";
	public static final String USER_PHOTO_KEY = "user_photo_url";
	public static final String USER_EMAIL_PUSH_BOUND_KEY = "user_push_email_bound";
	public static final String USER_EMAIL_PUSH_ON_KEY = "user_email_push_on";
	public static final String USER_EDM_EMAIL_KEY = "user_edm_email";

	public static final String USER_POINT_KEY_BOUND_PREFIX = "user_point_bound_";
	public static final String USER_POINT_KEY_ID_PREFIX = "user_point_id_";
	public static final String USER_POINT_KEY_ACCOUNT_PREFIX = "user_point_mobile_";
	public static final String USER_POINT_KEY_BALANCE_PREFIX = "user_point_balance_";

	public AladingUser() {
		AladingEEApp app = AladingEEApp.getApplication();
		if (app != null) {
			Context mContext = app.getApplicationContext();
			mUserPreferences = mContext.getSharedPreferences("user",
					Context.MODE_PRIVATE);
		} else {
			LogX.trace("Alading", "AladingApp is null");
		}

	}

	public void clear() {
		mMemberID = FusionCode.EMPTY_STRING;
		mMobile = FusionCode.EMPTY_STRING;
		mUdid = FusionCode.EMPTY_STRING;
		mRealName = FusionCode.EMPTY_STRING;
		mTelephone = FusionCode.EMPTY_STRING;
		mBirthday = FusionCode.EMPTY_STRING;
		mEmail = FusionCode.EMPTY_STRING;
		mSex = FusionCode.EMPTY_STRING;
		mMoney = 0;

		mUserPreferences.edit().clear().commit();
	}

	public void setUserEdmEmail(String emdMail) {
		mUserEdmEmail = emdMail;
		mUserPreferences.edit().putString(USER_EDM_EMAIL_KEY, emdMail).commit();
	}

	public String getUserEdmEmail() {
		if (mUserEdmEmail == null
				|| mUserEdmEmail.equals(FusionCode.EMPTY_STRING)) {
			mUserEdmEmail = mUserPreferences.getString(USER_EDM_EMAIL_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mUserEdmEmail;
	}

	public void setUserEmailPushBound(boolean bound) {
		mUserEdmBound = bound;
		mUserPreferences.edit().putBoolean(USER_EMAIL_PUSH_BOUND_KEY, bound)
				.commit();
	}

	public boolean getUserEmailPushBound() {
		mUserEdmBound = mUserPreferences.getBoolean(USER_EMAIL_PUSH_BOUND_KEY,
				false);

		return mUserEdmBound;
	}

	public void setUserEmailPushOn(boolean on) {
		mUserEdmPushOn = on;
		mUserPreferences.edit().putBoolean(USER_EMAIL_PUSH_ON_KEY, on).commit();
	}

	public boolean getUserEmailPushOn() {
		mUserEdmPushOn = mUserPreferences.getBoolean(USER_EMAIL_PUSH_ON_KEY,
				false);

		return mUserEdmPushOn;
	}

	public String getMemberID() {
		if (mMemberID == null || mMemberID.equals(FusionCode.EMPTY_STRING)) {
			mMemberID = mUserPreferences.getString(USER_ID_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mMemberID;
	}

	public void setMemberID(String mMemberID) {
		this.mMemberID = mMemberID;
		mUserPreferences.edit().putString(USER_ID_KEY, mMemberID).commit();
	}

	public String getMobile() {
		if (mMobile == null || mMobile.equals(FusionCode.EMPTY_STRING)) {
			mMobile = mUserPreferences.getString(USER_MOBILE_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mMobile;
	}

	public void setMobile(String mMobile) {
		this.mMobile = mMobile;
		mUserPreferences.edit().putString(USER_MOBILE_KEY, mMobile).commit();
	}

	public String getUdid() {
		if (mUdid == null || mUdid.equals(FusionCode.EMPTY_STRING)) {
			mUdid = mUserPreferences.getString(USER_UDID_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mUdid;
	}

	public void setUdid(String mUdid) {
		this.mUdid = mUdid;
		mUserPreferences.edit().putString(USER_UDID_KEY, mUdid).commit();
	}

	public String getRealName() {
		if (mRealName == null || mRealName.equals(FusionCode.EMPTY_STRING)) {
			mRealName = mUserPreferences.getString(USER_NAME_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mRealName;
	}

	public void setRealName(String mRealName) {
		this.mRealName = mRealName;
		mUserPreferences.edit().putString(USER_NAME_KEY, mRealName).commit();
	}

	public String getTelephone() {
		if (mTelephone == null || mTelephone.equals(FusionCode.EMPTY_STRING)) {
			mTelephone = mUserPreferences.getString(USER_PHONE_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mTelephone;
	}

	public void setTelephone(String mTelephone) {
		this.mTelephone = mTelephone;
		mUserPreferences.edit().putString(USER_PHONE_KEY, mTelephone).commit();
	}

	public String getBirthday() {
		if (mBirthday == null || mBirthday.equals(FusionCode.EMPTY_STRING)) {
			mBirthday = mUserPreferences.getString(USER_BIRTHDAY_KEY,
					FusionCode.EMPTY_STRING);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Date date = null;
		try {
			date = dateFormat.parse(mBirthday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String formatedDate = FusionCode.EMPTY_STRING;
		if (date != null) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			formatedDate = dateFormat.format(date);
		}

		return formatedDate;
	}

	public void setBirthday(String mBirthday) {
		this.mBirthday = mBirthday;
		mUserPreferences.edit().putString(USER_BIRTHDAY_KEY, mBirthday)
				.commit();
	}

	public String getEmail() {
		if (mEmail == null || mEmail.equals(FusionCode.EMPTY_STRING)) {
			mEmail = mUserPreferences.getString(USER_EMAIL_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
		mUserPreferences.edit().putString(USER_EMAIL_KEY, mEmail).commit();
	}

	public String getSex() {
		if (mSex == null || mSex.equals(FusionCode.EMPTY_STRING)) {
			mSex = mUserPreferences.getString(USER_SEX_KEY,
					FusionCode.EMPTY_STRING);
		}

		return mSex;
	}

	public void setSex(String sex) {
		this.mSex = sex;
		mUserPreferences.edit().putString(USER_SEX_KEY, mSex).commit();
	}

	public void setMoney(float money) {
		this.mMoney = money;
		mUserPreferences.edit().putFloat(USER_MONEY_KEY, mMoney).commit();
	}

	public float getMoney() {
		if (mMoney == 0) {
			mMoney = mUserPreferences.getFloat(USER_MONEY_KEY, 0);
		}

		return mMoney;
	}

	public boolean isUserLogin() {
		return !getMemberID().equals(FusionCode.EMPTY_STRING);
	}
}
