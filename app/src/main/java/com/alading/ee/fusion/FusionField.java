package com.alading.ee.fusion;

import java.util.ArrayList;
import java.util.List;

import com.google.alading.client.android.AladingUser;

/**
 * Global static variables.
 */
public class FusionField {

	public enum SvcState {
		IS_STARTING, LOADING_COMPLETE, NO_STATE
	}

	public static String currentVersion;

	public static float deviceDensity;
	public static int devicePixelsHeight;
	public static int devicePixelsWidth;;

	public static boolean networkConntected;

	public static SvcState state = SvcState.NO_STATE;

	public static boolean updateCheck = false;

	public static AladingUser user;

	public static long timestamp;

	public static int cityVersion = -1;
	public static int cityAreaVersion = -1;
	public static int channelVersion = -1;
	public static int storeVersion = -1;
	public static int adVersion = -1;
	public static int basicProvinceVersion = -1;
	public static int basicCityVersion = -1;
	public static int basicCityAreaVersion = -1;
	public static int rechargeAmountVersion = -1;
	public static int gameInfoVersion = -1;
	public static int gameProductVersion = -1;
	public static int plateNumberVersion = -1;

	public static String DATA_BASE_FILE_NAME;

	public static int sHotGameNum = 0;
}
