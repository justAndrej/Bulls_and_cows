package com.justandrej.study.bullsandcows;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private static SharedPreferences mSettings;

    public static void setUpdate(Context context, boolean value){
        mSettings = context.getSharedPreferences(DBHelper.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(DBHelper.APP_PREFERENCES_LAST_ACCESS, value);
        editor.apply();
    }

    public static boolean isUpdated(Context context){
        mSettings = context.getSharedPreferences(DBHelper.APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getBoolean(DBHelper.APP_PREFERENCES_LAST_ACCESS, false);
    }
}
