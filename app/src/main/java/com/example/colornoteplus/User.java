package com.example.colornoteplus;

import android.content.Context;
import android.util.Log;

public abstract class User {

    /**
     * @deprecated
     * @param username set local user name
     * @param context calling context
     */
    static public void setCurrentUsername(String username, Context context){

        DatabaseManager.SaveString(username,App.KEY_CURRENT_USER, context);
    }

    /**
     * Get the current user name
     * @param context calling context
     * @return current username
     */
    static public String getCurrentUsername(Context context){

        String temp = DatabaseManager.LoadString(App.KEY_CURRENT_USER,context);

        if (temp.equals(context.getString(R.string.error))) {
            Log.d("USER", "current user is " + App.TEST_USER);
            return App.TEST_USER;
        }
        else
            return temp;

    }
}
