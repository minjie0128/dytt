
package com.alading.ee.util;

import com.alading.ee.fusion.ServerInfo;

import android.util.Log;

public class LogX {

    private static boolean needRecord = ServerInfo.LOG_ENABLE_ON_RELEASE ? true
            : (ServerInfo.IS_RELEASE ? false : true);

    public static void d(String tag, String message) {
        if (needRecord) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void trace(String tag, String message) {
        if (needRecord) {
            Log.i(tag, message);
        }
    }

    public LogX(String path) {
    }
}
