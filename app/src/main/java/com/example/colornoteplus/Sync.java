package com.example.colornoteplus;

abstract public class Sync {

    // class responsible for syncing data to and from cloud data base

    // return the modification date of the local data base
    static public Long getLocalDataBaseModificationDate(){
        return 0L;
    }

    // return the modification date of the cloud data base
    // currently Firebase
    static public Long getCloudDataBaseModificationDate(){
        return 0L;
    }

    // perform syncing and make sure that data is up to date
    static public boolean sync(){
        return true;
    }

}
