package com.example.moodplanet;

import android.content.Context;

public class SharedPref {

    private static final String MY_PREFERENCE_NAME = "MySharedPref";
    public static final String PREF_CHECKED_KEY = "checked";
    public static final String PREF_MINUTE_KEY = "minute";
    public static final String PREF_HOUR_KEY = "hour";

    public static void saveChekedInPref(Context context, boolean isChecked) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_CHECKED_KEY, isChecked);
        editor.apply();
    }

    public static void saveHourInPref(Context context, int hour) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_HOUR_KEY, hour);
        editor.apply();
    }

    public static void saveMinuteInPref(Context context, int minute) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_MINUTE_KEY, minute);
        editor.apply();
    }

    public static boolean loadCheckedFromPref(Context context) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_CHECKED_KEY, false);
    }

    public static int loadHourFromPref(Context context) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PREF_HOUR_KEY, 0);
    }

    public static int loadMinuteFromPref(Context context) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PREF_MINUTE_KEY, 0);
    }

    public static void removeDataFromPref(Context context) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.remove(PREF_CHECKED_KEY);
        editor.remove(PREF_HOUR_KEY);
        editor.remove(PREF_MINUTE_KEY);
        editor.apply();
    }

    public static void registerPref(Context context, android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPref(Context context, android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        pref.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
