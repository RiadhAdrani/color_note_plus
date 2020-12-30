package com.example.colornoteplus;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

abstract public class Sync {

    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private static final String USERS = Statics.DATABASE_DATA_USERS;
    private static final String USER_INFO = Statics.DATABASE_USER_INFO;
    private static final String USER_NOTES = Statics.DATABASE_USER_NOTES;
    private static final String INFO = Statics.DATABASE_DATA_INFO;
    private static final String INFO_APP_VERSION = Statics.DATABASE_DATA_APP_VERSION;
    private static final String OBJECT_UID = Statics.DATABASE_OBJECT_UID;
    private static final String OBJECT_CREATION_DATE = Statics.DATABASE_OBJECT_CREATION_DATE;
    private static final String OBJECT_MODIFICATION_DATE = Statics.DATABASE_OBJECT_MODIFICATION_DATE;
    private static final String NOTE_TITLE = Statics.DATABASE_NOTE_TITLE;
    private static final String NOTE_COLOR = Statics.DATABASE_NOTE_COLOR;
    private static final String NOTE_CONTENT = Statics.DATABASE_NOTE_CONTENT;
    private static final String CL_ITEM_DESCRIPTION = Statics.DATABASE_CL_ITEM_DESCRIPTION;
    private static final String CL_ITEM_PRIORITY = Statics.DATABASE_CL_ITEM_PRIORITY;
    private static final String CL_ITEM_DONE_DATE = Statics.DATABASE_CL_ITEM_DONE_DATE;
    private static final String CL_ITEM_DUE_DATE = Statics.DATABASE_CL_ITEM_DUE_DATE;

    public interface OnDataRetrieval{
        void onDataRetrieval(Note<?> note);
    }

    static void uploadNote(Context context, Note<?> note){

        Map<String, Object> map = new HashMap<>();
        map.put(note.getUid(),note);
        DB.collection(USERS).document("test_user").set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Note syn success", Toast.LENGTH_SHORT).show();
                        Log.d("SYNC","Operation success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Note syn failed", Toast.LENGTH_SHORT).show();
                        Log.d("SYNC","Operation failed");
                    }
                });

        Log.d("SYNC","Operation Ended");
    }

    static void getNote(Context context, String uid,Note<?> output, OnDataRetrieval onDataRetrieval){

        DB.collection(USERS).document("test_user").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){

                        int color = Integer.parseInt(Objects.requireNonNull(documentSnapshot.get(NOTE_COLOR)).toString());
                        String title = (String) documentSnapshot.get(NOTE_TITLE);
                        Long creationDate = documentSnapshot.getLong(OBJECT_CREATION_DATE);

                        output.setColor(color);
                        output.setTitle(title);
                        output.setCreationDate(creationDate);

                        if (onDataRetrieval != null) onDataRetrieval.onDataRetrieval(output);

                    }
                    else {
                        Log.d("SYNC","Does not exist");
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

}
