package com.fd.alertplaces;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by frog on 05/01/15.
 */
public class Settings {

    public static final String PREFS_NAME = "NotifyTopPlaces";
    private static Context context;

    public Settings(Context ctx) {
        this.context = ctx;
    }

    private static void setNotify(String key, String value){
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, value);

        // Commit the edits!
        editor.commit();
    }

    private static String getNotify(String key){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, "").toString();
    }
}