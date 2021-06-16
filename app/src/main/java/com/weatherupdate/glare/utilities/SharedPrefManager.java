package com.weatherupdate.glare.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public SharedPrefManager (Context context){
        SharedPrefManager.context = context;
    }


    public static void setPauseTime( String key, String value) {
        SharedPreferences sharedPreferences=context.getSharedPreferences ("MySharedPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString (key, value);
        editor.apply ();
    }
    public static String getPauseTime(String key,String value) {
        SharedPreferences prefs = context.getSharedPreferences("MySharedPref", 0);
        return prefs.getString(key,value);
    }
    public static void setSearchActivity( String key, String value) {
        SharedPreferences mPrefs=context.getSharedPreferences ("MySP", 0);
        SharedPreferences.Editor editorS = mPrefs.edit();
        editorS.putString (key, value);
        editorS.apply ();
    }
    public static String getSearchActivity(String key) {
        SharedPreferences sPrefs = context.getSharedPreferences("MySP", 0);
        return sPrefs.getString(key,"");
    }
}