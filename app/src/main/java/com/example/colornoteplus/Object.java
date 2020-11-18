package com.example.colornoteplus;

public abstract class Object {

    // Root class for all object in the app

    // Object unique identifier
    private String uid;
            public void setUid(String uid){ this.uid = uid; }
            public String getUid(){ return this.uid;}

    // Object Creation Date
    private Long creationDate;
            public void setCreationDate(Long creationDate){ this.creationDate = creationDate;
                                                            this.modificationDate = creationDate;}
            public Long getCreationDate(){ return this.creationDate;}

    // Object Modification Date
    private Long modificationDate;
            public void setModificationDate(Long modificationDate){ this.modificationDate = modificationDate; }
            public Long getModificationDate(){ return this.modificationDate;}
}