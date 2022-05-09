package com.example.moodplanet;

import android.content.Context;

public class SharedPref {

    private static final String MY_PREFERENCE_NAME = "MySharedPref";
    public static final String PREF_CHECKED_KEY = "checked";

    public static void saveChekedInPref(Context context, boolean isChecked) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_CHECKED_KEY, isChecked);
        editor.apply();
    }

    public static boolean loadCheckedFromPref(Context context) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_CHECKED_KEY, false);
    }

    public static void removeDataFromPref(Context context) {
        android.content.SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.remove(PREF_CHECKED_KEY);
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
