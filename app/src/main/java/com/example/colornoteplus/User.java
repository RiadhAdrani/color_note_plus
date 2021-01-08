package com.example.colornoteplus;

import android.content.Context;

public class User {

    /**
     * user name
     */
    private String username;

    /**
     * user email
     */
    private String email;


    /**
     * Public constructor
     * @param username name
     * @param email email
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * @deprecated
     * public empty constructor
     */
    public User(){

    }

    /**
     * getter for username
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * @deprecated
     * setter for username
     * @param username new name
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * @deprecated
     * getter for email
     * @return email
     */
    public String getEmail() { return email; }

    /**
     * @deprecated
     * setter for email
     * @param email new email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Get the current username info
     * @param context calling context
     * @return current user
     */
    static public User getCurrentUser(Context context){
        return new User("test_user","user@email.com");
    }
}
