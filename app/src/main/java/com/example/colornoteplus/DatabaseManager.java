package com.example.colornoteplus;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class handling data saving and retrieval to and from the local database.
 * @see App
 */
public abstract class DatabaseManager {

    /**
     * Key of the shared preference data containing the value of the last modification time of
     * the local database
     */
    public static final String LAST_UPDATE = App.KEY_DATABASE_LAST_UPDATE;

    /**
     * retrieve the date of the last modification of the local database as a Long
     * @see App
     * @param context context of calling
     * @return date as Long
     */
    public static Long getModificationDate(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LAST_UPDATE,0L);
    }

    /**
     * update the date of the last modification with the current exact time as Long
     * @see App
     * @param context context of calling
     */
    public static void setModificationDate(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        editor.apply();
    }

    /**
     * update the date of the last modification
     * @see App
     * @param context context of calling
     * @param date new date
     */
    public static void setModificationDate(Context context, Long date){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE, date);
        editor.apply();
    }

    /**
     * delete an existing note from the local database
     * @see App
     * @param key uid of the note
     * @param context context of calling
     */
    public static void deleteNote(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        setModificationDate(context);
    }

    /**
     * save a text note
     * @see App
     * @see TextNote
     * @param note text note to be saved
     * @param context context of calling
     */
    public static void setTextNote(TextNote note, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(note);
        editor.putString(note.getUid(),json);
        editor.apply();
        setModificationDate(context);
    }

    /**
     * load a text note
     * @see App
     * @see TextNote
     * @param uid note uid
     * @param context context of calling
     * @return the wanted text note
     */
    public static TextNote getTextNote(String uid, Context context){

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

    /**
     * save a check list note
     * @see App
     * @see CheckListNote
     * @param note note to be saved
     * @param context calling context
     */
    public static void setCheckListNote(CheckListNote note, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(note);
        editor.putString(note.getUid(),json);
        editor.apply();
        setModificationDate(context);
    }

    /**
     * load a check list note
     * @see App
     * @param uid note id
     * @param context calling context
     * @return return check list note
     */
    public static CheckListNote getCheckListNote(String uid, Context context){

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

    /**
     * Save a string array to the database
     * @see App
     * @param list list to be saved
     * @param key list id
     * @param context calling context
     */
    public static void setStringArray(ArrayList<String> list, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key,json);
        editor.apply();
        setModificationDate(context);
    }

    /**
     * Load a string array from the database
     * @see App
     * @param uid array key
     * @param context calling context
     * @return array list of strings
     */
    public static ArrayList<String> getStringArray(String uid, Context context){

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

    /**
     * Load an int from the local database
     * @see App
     * @param key int id
     * @param context calling context
     * @return an int
     */
    public static int getInteger(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }

    /**
     * Save a boolean in the database
     * @see App
     * @param bool value to be saved
     * @param key value id
     * @param context calling context
     */
    public static void setBoolean(boolean bool, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,bool);
        editor.apply();
    }

    /**
     * @see App
     * @param key value id
     * @param context calling context
     * @return return boolean
     */
    public static boolean getBoolean(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    /**
     * Save a boolean in the database
     * @see App
     * @param string value to be saved
     * @param key value id
     * @param context calling context
     */
    public static void setString(String string, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,string);
        editor.apply();
    }

    /**
     * @see App
     * @param key value id
     * @param context calling context
     * @return return String
     */
    public static String getString(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,context.getString(R.string.error));
    }

    /**
     * Save an integer to the database
     * @see App
     * @param integer value to be saved
     * @param key value id
     * @param context calling context
     */
    public static void setInteger(int integer, String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,integer);
        editor.apply();
    }

    /**
     * get all the notes in the database
     * @see App
     * @see Note
     * @param context calling context
     * @return array list of all the notes
     */
    public static ArrayList<Note<?>> getAllNotes(Context context){

        ArrayList<Note<?>> notes = new ArrayList<>();

        for (String uid : getStringArray(App.KEY_NOTE_LIST,context)){
            switch (Note.getNoteClass(uid)){
                case TEXT_NOTE: notes.add(getTextNote(uid,context)); break;
                case CHECK_NOTE: notes.add(getCheckListNote(uid,context)); break;
            }
        }

        return notes;
    }

    /**
     * erase all data from the database
     * @see App
     * @param context calling context
     */
    public static void wipeDatabase(Context context){

        String currentUser = User.getID(context);
        boolean rememberMe = DatabaseManager.getBoolean(App.KEY_REMEMBER_ME,context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        DatabaseManager.setString(currentUser,App.KEY_CURRENT_USER,context);
        DatabaseManager.setBoolean(rememberMe,App.KEY_REMEMBER_ME,context);
    }

}