
package com.alading.ee;

import android.content.Context;
import android.content.SharedPreferences;

import com.alading.ee.fusion.FusionCode;

public class AladingPreferences {

    private Context mContext;

    private SharedPreferences mSharedPref;

    private static AladingPreferences sInstance;

    public static AladingPreferences getInstance() {
        if (null == sInstance) {
            synchronized (AladingPreferences.class) {
                if (null == sInstance) {
                    sInstance = new AladingPreferences();
                }
            }
        }
        return sInstance;
    }

    private AladingPreferences() {
        mContext = AladingEEApp.getApplication().getApplicationContext();
        mSharedPref = mContext.getSharedPreferences(FusionCode.ALADING_PROPERTIES,
                Context.MODE_PRIVATE);
    }

    public Context getContext() {
        return mContext;
    }

    public void setSharedPref(SharedPreferences preferences) {
        mSharedPref = preferences;
    }

    public SharedPreferences getSp() {
        return mSharedPref;
    }

    public void putBooleanKey(String key, boolean setBool) {
        mSharedPref.edit().putBoolean(key, setBool).commit();
    }

    public boolean getBooleanKey(String key) {
        return mSharedPref.getBoolean(key, false);
    }

    public void putFloatKey(String key, Float setFloat) {
        mSharedPref.edit().putFloat(key, setFloat).commit();
    }

    public float getFloatKey(String key) {
        return mSharedPref.getFloat(key, 0);
    }

    public void putIntKey(String key, int setInt) {
        mSharedPref.edit().putInt(key, setInt).commit();
    }

    public int getIntKey(String key) {
        return mSharedPref.getInt(key, 0);
    }

    public void putLongKey(String key, Long setLong) {
        mSharedPref.edit().putLong(key, setLong).commit();
    }

    public long getLongKey(String key) {
        return mSharedPref.getInt(key, 0);
    }

    public void putStringKey(String key, String setString) {
        mSharedPref.edit().putString(key, setString).commit();
    }

    public String getStringKey(String key) {
        return mSharedPref.getString(key, FusionCode.EMPTY_STRING);
    }
}
