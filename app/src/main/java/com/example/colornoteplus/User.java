package com.example.colornoteplus;

import android.content.Context;

public class User {

    private String username; // username of the user
    private String email; // email of the user
    private Long dataBaseLastModified; // the last time the local database has been updated or modified
    private Long lastSynced; // the last time the local database has been synced with the cloud db

    public User(String username, String email, Long dataBaseLastModified, Long lastSynced) {
        this.username = username;
        this.email = email;
        this.dataBaseLastModified = dataBaseLastModified;
        this.lastSynced = lastSynced;
    }

    public User(){

    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Long getDataBaseLastModified() { return dataBaseLastModified; }

    public void setDataBaseLastModified(Long dataBaseLastModified) { this.dataBaseLastModified = dataBaseLastModified; }

    public Long getLastSynced() { return lastSynced; }

    public void setLastSynced(Long lastSynced) { this.lastSynced = lastSynced; }

    // return the current user
    static public User getCurrentUser(Context context){
        return new User();
    }
}
