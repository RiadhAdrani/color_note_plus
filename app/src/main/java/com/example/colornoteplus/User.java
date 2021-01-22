package com.example.colornoteplus;

import android.content.Context;
import android.util.Log;

public abstract class User {

    /**
     * @param username set local user name
     * @param context calling context
     */
    static public void setUsername(String username, Context context){
        DatabaseManager.SaveString(username,App.KEY_CURRENT_USER, context);
    }

    /**
     * Get the current user name
     * @param context calling context
     * @return current username
     */
    static public String getUsername(Context context){

        String temp = DatabaseManager.LoadString(App.KEY_CURRENT_USER,context);

        if (temp.equals(context.getString(R.string.error))) {
            Log.d("USER", "current user is " + App.TEST_USER);
            return App.TEST_USER;
        }
        else
            return temp;

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
