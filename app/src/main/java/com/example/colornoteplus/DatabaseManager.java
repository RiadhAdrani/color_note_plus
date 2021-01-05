package com.example.colornoteplus;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class DatabaseManager {

    public static final String LAST_UPDATE = App.KEY_DATABASE_LAST_UPDATE;

    public static Long getDatabaseLastModificationDate(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LAST_UPDATE,0L);
    }

    public static void setDatabaseLastModificationDate(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        editor.apply();
    }

    public static void setDatabaseLastModificationDate(Context context, Long date){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE, date);
        editor.apply();
    }

    public static void DeleteNote(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        setDatabaseLastModificationDate(context);
    }

    public static void SaveTextNote(TextNote note, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(note);
        editor.putString(note.getUid(),json);
        editor.apply();
        setDatabaseLastModificationDate(context);
    }

    public static TextNote LoadTextNote(String uid, Context context){

        TextNote note;

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<TextNote>() {}.getType();
        note = gson.fromJson(json,type);

        if (note == null){
            return new TextNote(context);
        } else
            return note;
    }

    public static void SaveCheckListNote(CheckListNote note, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(note);
        editor.putString(note.getUid(),json);
        editor.apply();
        setDatabaseLastModificationDate(context);
    }

    public static CheckListNote LoadCheckListNote(String uid, Context context){

        CheckListNote note = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(uid,null);

        Type type = new TypeToken<CheckListNote>() {}.getType();

        try{
            note = gson.fromJson(json,type);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (note == null){
            return new CheckListNote(context);
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
        setDatabaseLastModificationDate(context);
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

    public static void SaveBoolean(boolean bool, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,bool);
        editor.apply();
    }

    public static boolean LoadBoolean(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public static void SaveInteger(int integer, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,integer);
        editor.apply();
    }

    public static ArrayList<Note<?>> LoadAllNotes(Context context){

        ArrayList<Note<?>> notes = new ArrayList<>();

        for (String uid : LoadStringArray(App.KEY_NOTE_LIST,context)){
            switch (Note.getNoteClass(uid)){
                case TEXT_NOTE: notes.add(LoadTextNote(uid,context)); break;
                case CHECK_NOTE: notes.add(LoadCheckListNote(uid,context)); break;
            }
        }

        return notes;
    }

    public static void wipeDatabase(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}