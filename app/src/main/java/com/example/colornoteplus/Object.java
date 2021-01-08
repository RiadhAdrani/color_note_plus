package com.example.colornoteplus;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Root object of the majority of the classes in the app
 */
public abstract class Object implements Serializable {

    // Root class for all object in the app

    /**
     * Unique identifier
     */
    private String uid;

    /**
     * setter for uid
     * @param uid new uid
     */
    public void setUid(String uid){
        this.uid = uid;
    }

    /**
     * getter for uid
     * @return object uid
     */
    public String getUid(){
        return this.uid;
    }

    /**
     * object creation date
     */
    private Long creationDate;

    /**
     * setter for creation date.
     * update modification date too.
     * @param creationDate new creation date
     */
    public void setCreationDate(Long creationDate){
        this.creationDate = creationDate;
        this.modificationDate = creationDate;
    }

    /**
     * getter for creation date
     * @return object creation date
     */
    public Long getCreationDate(){
        return this.creationDate;
    }

    /**
     * object modification date
     */
    private Long modificationDate;

    /**
     * setter for modification date3
     * @param modificationDate new date
     */
    public void setModificationDate(Long modificationDate){
        Log.d("DEBUG_UNSAVED","Modification Date changes");
        this.modificationDate = modificationDate;
    }

    /**
     * setter for modification date.
     * set it to the exact current time.
     */
    public void setModificationDate(){
        Log.d("DEBUG_UNSAVED","Modification Date changes");
        this.modificationDate = Calendar.getInstance().getTime().getTime();
    }

    /**
     * getter for modification date.
     * @return object modification date
     */
    public Long getModificationDate(){
        return this.modificationDate;
    }
}