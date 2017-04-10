package com.example.ryan.gamenight.util;

import android.util.Log;

public class UtilLog {

    private static boolean debugMode = false;

    public static void setDebugMode(boolean b) {
        debugMode = b;
    }

    public static void logD(String key, String value) {
        if (debugMode) {
            Log.d(key, value);
        }
    }
}