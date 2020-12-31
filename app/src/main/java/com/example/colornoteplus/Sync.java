package com.example.colornoteplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

abstract public class Sync {

    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private static final String USERS = App.DATABASE_DATA_USERS;
    private static final String USER_INFO = App.DATABASE_USER_INFO;
    private static final String USER_NOTES = App.DATABASE_USER_NOTES;
    private static final String INFO = App.DATABASE_DATA_INFO;
    private static final String INFO_APP_VERSION = App.DATABASE_DATA_APP_VERSION;
    private static final String OBJECT_UID = App.DATABASE_OBJECT_UID;
    private static final String OBJECT_CREATION_DATE = App.DATABASE_OBJECT_CREATION_DATE;
    private static final String OBJECT_MODIFICATION_DATE = App.DATABASE_OBJECT_MODIFICATION_DATE;
    private static final String NOTE_TITLE = App.DATABASE_NOTE_TITLE;
    private static final String NOTE_COLOR = App.DATABASE_NOTE_COLOR;
    private static final String NOTE_CONTENT = App.DATABASE_NOTE_CONTENT;
    private static final String CL_ITEM_DESCRIPTION = App.DATABASE_CL_ITEM_DESCRIPTION;
    private static final String CL_ITEM_PRIORITY = App.DATABASE_CL_ITEM_PRIORITY;
    private static final String CL_ITEM_DONE_DATE = App.DATABASE_CL_ITEM_DONE_DATE;
    private static final String CL_ITEM_DUE_DATE = App.DATABASE_CL_ITEM_DUE_DATE;

    public interface OnDataRetrieval{
        void onSuccess(Note<?> note, DocumentSnapshot snapshot);
        void onFailure();
    }

    static void uploadNote(Context context, Note<?> note){

        DB.collection(USERS)
                .document(User.getCurrentUser(context)
                .getUsername()).collection(USER_NOTES)
                .document(note.getUid())
                .set(note.toMap())
                .addOnSuccessListener(aVoid -> Log.d("SYNC","Operation success"))
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Note syn failed", Toast.LENGTH_SHORT).show();
                    Log.d("SYNC","Operation failed");
                });

        Log.d("SYNC","Operation Ended");
    }

    static void getNote(Context context, String uid,final Note<?> output, OnDataRetrieval onDataRetrieval){

        DB.collection(USERS)
                .document(User.getCurrentUser(context)
                .getUsername())
                .collection(USER_NOTES).document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (onDataRetrieval != null) onDataRetrieval.onSuccess(output,documentSnapshot);
                })
                .addOnFailureListener(e -> {

                    if (onDataRetrieval != null) onDataRetrieval.onFailure();
                });
    }

}
