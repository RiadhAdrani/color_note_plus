package com.example.colornoteplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class MySharedPreferences {

    public static String LoadStringFromSharedPreferences(String key,Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("Load String",Context.MODE_PRIVATE);
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

    public static void SaveTextNoteToSharedPreferences(TextNote note,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONST.SAVE_TEXT_NOTE_TO_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(note);

        editor.putString(note.getUid(),json);

        editor.apply();
    }

    public static TextNote LoadTextNoteFromSharedPreferences(String uid,Context context){

        TextNote note;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<TextNote>() {}.getType();
        note = gson.fromJson(json,type);

        if (note == null){
            return new TextNote();
        } else
            return note;
    }

    public static void SaveStringArrayToSharedPreferences(ArrayList<String> list,String key,Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONST.SAVE_STRING_ARRAY_TO_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key,json);

        editor.apply();
    }

    public static ArrayList<String> LoadStringArrayToSharedPreferences(String uid,Context context){

        ArrayList<String> list;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<TextNote>() {}.getType();
        list = gson.fromJson(json,type);

        if (list == null){
            return new ArrayList<>();
        } else
            return list;
    }

}

