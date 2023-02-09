package com.example.myonlinebookself.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myonlinebookself.MOBApplication;

public class PreferenceUtils {

    @SuppressWarnings("unused")
    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(Constants.Preferences.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences(){
        return MOBApplication.getContext().getSharedPreferences(Constants.Preferences.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static String getLogin(){
        final SharedPreferences prefs = getSharedPreferences();
        return prefs.getString(Constants.Preferences.PREF_LOGIN, null);
    }

    public static void setLogin(String login){
        final SharedPreferences prefs = getSharedPreferences();
        prefs.edit().putString(Constants.Preferences.PREF_LOGIN, login).commit();
    }
}
