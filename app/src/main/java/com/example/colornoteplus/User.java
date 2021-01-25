package com.example.colornoteplus;

import android.content.Context;
import android.util.Log;

public abstract class User {

    /**
     * Reset local user info to (no-user) : null values.
     * @param context calling context.
     */
    static public void resetUserData(Context context){
        DatabaseManager.setDatabaseLastModificationDate(context,0L);
        setID(App.NO_ID,context);
        setUsername(App.NO_ID, context);
        setEmail(context, App.NO_ID);
    }

    /**
     * @param id set local user name
     * @param context calling context
     */
    static public void setID(String id, Context context){
        DatabaseManager.SaveString(id,App.KEY_CURRENT_USER, context);
    }

    /**
     * Get the current user name
     * @param context calling context
     * @return current username
     */
    static public String getID(Context context){

        String temp = DatabaseManager.LoadString(App.KEY_CURRENT_USER,context);

        if (temp.equals(context.getString(R.string.error))) {
            Log.d("USER", "current user is " + App.TEST_USER);
            return App.TEST_USER;
        }
        else
            return temp;

    }

    /**
     * Update the current local username
     * @param name new username
     * @param context calling context
     */
    static public void setUsername(String name, Context context){
        DatabaseManager.SaveString(name,App.KEY_CURRENT_USERNAME,context);
    }

    /**
     * Get the currently active user username
     * @param context calling context
     * @return username as string
     */
    static public String getUsername(Context context){
        return DatabaseManager.LoadString(App.KEY_CURRENT_USERNAME,context);
    }

    // Hello
    /**
     * Update current user email
     * @param context calling context
     * @param email new email
     */
    static public void setEmail(Context context, String email){
        DatabaseManager.SaveString(email,App.KEY_CURRENT_EMAIL,context);
    }

    /**
     * Get current user email
     * @param context calling context
     * @return email as a string
     */
    static public String getEmail(Context context){
        String temp = DatabaseManager.LoadString(App.KEY_CURRENT_EMAIL,context);

        if (temp.equals(context.getString(R.string.error))) {
            Log.d("USER", "current user is " + App.TEST_EMAIL);
            return App.TEST_EMAIL;
        }
        else
            return temp;
    }
}
