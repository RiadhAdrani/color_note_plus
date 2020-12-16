package com.example.colornoteplus;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

public abstract class Object implements Serializable {

    // Root class for all object in the app

    // Object unique identifier
    private String uid;
            public void setUid(String uid){
                this.uid = uid;
            }
            public String getUid(){
                return this.uid;
            }

    // Object Creation Date
    private Long creationDate;
            public void setCreationDate(Long creationDate){
                this.creationDate = creationDate;
                this.modificationDate = creationDate;
            }
            public Long getCreationDate(){
                return this.creationDate;
            }

    // Object Modification Date
    private Long modificationDate;
            public void setModificationDate(Long modificationDate){
                Log.d("DEBUG_UNSAVED","Modification Date changes");
                this.modificationDate = modificationDate;
            }
            public void setModificationDate(){
                Log.d("DEBUG_UNSAVED","Modification Date changes");
                this.modificationDate = Calendar.getInstance().getTime().getTime();
            }
            public Long getModificationDate(){
                return this.modificationDate;
            }
}