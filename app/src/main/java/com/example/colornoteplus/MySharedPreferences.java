package com.example.colornoteplus;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class MySharedPreferences {

    public static void DeleteNote(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String LoadString(String key, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static void SaveString(String string, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,string.trim());
        editor.apply();
    }

    public static Long LoadLong(String key,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,0L);
    }

    public static void SaveTextNote(NoteText note, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(note);

        editor.putString(note.getUid(),json);

        editor.apply();
    }

    public static NoteText LoadTextNote(String uid, Context context){

        NoteText note;

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<NoteText>() {}.getType();
        note = gson.fromJson(json,type);

        if (note == null){
            return new NoteText(context);
        } else
            return note;
    }

    public static void SaveCheckListNote(NoteCheckList note, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(note);

        editor.putString(note.getUid(),json);

        editor.apply();
    }

    public static NoteCheckList LoadCheckListNote(String uid, Context context){

        NoteCheckList note = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<NoteCheckList>() {}.getType();

        try{
            note = gson.fromJson(json,type);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (note == null){
            return new NoteCheckList(context);
        } else
            return note;
    }

    public static void SaveStringArray(ArrayList<String> list, String key, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key,json);

        editor.apply();
    }

    public static ArrayList<String> LoadStringArray(String uid, Context context){

        ArrayList<String> list;

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        list = gson.fromJson(json,type);

        if (list == null){
            return new ArrayList<>();
        } else
            return list;
    }

    public static int LoadInteger(String key, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }

    public static void SaveInteger(int integer, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,integer);
        editor.apply();
    }

}

