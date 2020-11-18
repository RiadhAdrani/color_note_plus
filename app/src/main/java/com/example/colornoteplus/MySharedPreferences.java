package com.example.colornoteplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public abstract class MySharedPreferences {

    public static String LoadStringFromSharedPreferences(String key,Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("Load String",Context.MODE_PRIVATE);
        Log.d("DEBUG_SHARED_PREF","Loaded String: "+sharedPreferences.getString(key,""));
        return sharedPreferences.getString(key,"");
    }

    public static void SaveStringToSharedPreferences(String string, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Save String",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,string.trim());
        editor.apply();
    }

    public static Long LoadLongFromSharedPreferences(String key,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Load String",Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,0L);
    }

}

