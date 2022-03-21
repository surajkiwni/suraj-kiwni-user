package com.kiwni.app.user.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    public static void putPreferences(Context context, String key,
                                      String value) {
        SharedPreferences faves = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = faves.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putPreferences(Context context, String key, boolean value) {

        SharedPreferences faves = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = faves.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putPreferences(Context context, String key,
                                      float value) {

        SharedPreferences faves = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = faves.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void putPreferences(Context context, String key,
                                      int value) {

        SharedPreferences faves = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = faves.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getPreferences(Context context, String key,
                                        String df) {
        SharedPreferences myPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return myPrefs.getString(key, df);
    }

    public static boolean getPreferences(Context context, String key,
                                         boolean df) {
        SharedPreferences myPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return myPrefs.getBoolean(key, df);
    }

    public static int getPreferences(Context context, String key,
                                     int df) {
        SharedPreferences myPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return myPrefs.getInt(key, df);
    }

    public static float getPreferences(Context context, String key,
                                       float df) {
        SharedPreferences myPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return myPrefs.getFloat(key, df);
    }
}
