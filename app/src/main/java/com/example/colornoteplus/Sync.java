package com.example.colornoteplus;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;

abstract public class Sync {

    private static final DatabaseReference ref = FirebaseDatabase.getInstance(Statics.DATABASE_URL).getReference();
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

    // class responsible for syncing data to and from cloud data base
    
    // update database info
    static public void syncInfo(Context context){
    }

    // get app version
    static public String getAppVersion(){

        final String[] version = {"1.0.0"};

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                version[0] = Objects.requireNonNull(snapshot.child(INFO).child(INFO_APP_VERSION).getValue()).toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return version[0];

    }

    static public Note<?> testGetNote(String uid, String userName,Context context){

        final Note<?>[] finalNote = {null};

        ref.child(USERS).child(userName).child(USER_NOTES).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("EXEC", "Started Execution");

                Note<?> snapNote = null;

                // get note uid
                String uid = Objects.requireNonNull(snapshot.child(OBJECT_UID).getValue()).toString();
                Log.d("SYNC","UID : "+uid);

                // get note creation date
                long creationDate = Long.parseLong(Objects.requireNonNull(snapshot.child(OBJECT_CREATION_DATE).getValue()).toString());
                Log.d("SYNC","Creation Date : "+creationDate);

                // get note modification date
                long modificationDate = Long.parseLong(Objects.requireNonNull(snapshot.child(OBJECT_MODIFICATION_DATE).getValue()).toString());
                Log.d("SYNC","Modification Date : "+modificationDate);

                // get note title
                String title = Objects.requireNonNull(snapshot.child(NOTE_TITLE).getValue()).toString();
                Log.d("SYNC","Title : "+title);

                // get note color
                int color = Integer.parseInt(Objects.requireNonNull(snapshot.child(NOTE_COLOR).getValue()).toString());
                Log.d("SYNC","Color : "+color);

                switch (Note.getNoteClass(uid)){
                    case TEXT_NOTE:

                        // get text Note content
                        String content = Objects.requireNonNull(snapshot.child(NOTE_CONTENT).getValue()).toString();
                        Log.d("SYNC","UID : "+content);

                        NoteText textNote = new NoteText();
                        textNote.setUid(uid);
                        textNote.setTitle(title);
                        textNote.setCreationDate(creationDate);
                        textNote.setContent(content);
                        textNote.setColor(color);
                        textNote.setModificationDate(modificationDate);

                        snapNote = textNote;
                        Log.d("SYNC", "Title => "+snapNote.getTitle());
                        break;

                    case CHECK_NOTE:

                        ArrayList<CheckListItem> items = new ArrayList<>();

                        for (DataSnapshot checkListItem : snapshot.child(NOTE_CONTENT).getChildren()) {

                            Log.d("SYNC","ITEM UID : "+ checkListItem.getKey());

                            String iUid = null;
                            try {
                                iUid = Objects.requireNonNull(checkListItem.child(OBJECT_UID).getValue()).toString();
                                Log.d("SYNC","ITEM UID : "+iUid);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("SYNC","ITEM UID : NULL");
                            }


                            String iDescription = Objects.requireNonNull(checkListItem.child(CL_ITEM_DESCRIPTION).getValue()).toString();
                            Log.d("SYNC","ITEM DESCRIPTION : "+iDescription);

                            CheckListItem.PRIORITY iPriority = Statics.stringToPriority(context, Objects.requireNonNull(checkListItem.child(CL_ITEM_PRIORITY).getValue()).toString());
                            Log.d("SYNC","ITEM PRIORITY : "+iPriority);

                            Long iDoneDate = Long.parseLong(Objects.requireNonNull(checkListItem.child(CL_ITEM_DONE_DATE).getValue()).toString());
                            Log.d("SYNC","DONE DATE : "+iDoneDate);

                            Long iDueDate = Long.parseLong(Objects.requireNonNull(checkListItem.child(CL_ITEM_DUE_DATE).getValue()).toString());
                            Log.d("SYNC","DUE DATE : "+iDueDate);

                            Long iCreationDate = Long.parseLong(Objects.requireNonNull(checkListItem.child(OBJECT_CREATION_DATE).getValue()).toString());
                            Log.d("SYNC","CREATION DATE : "+iCreationDate);

                            Long iModificationDate = Long.parseLong(Objects.requireNonNull(checkListItem.child(OBJECT_MODIFICATION_DATE).getValue()).toString());
                            Log.d("SYNC","MODIFICATION : "+iModificationDate);

                            CheckListItem item = new CheckListItem();
                            item.setDoneDate(iDoneDate);
                            item.setCreationDate(iCreationDate);
                            item.setUid(iUid == null ? UUID.randomUUID().toString()+ iCreationDate + UUID.randomUUID().toString() : iUid);
                            item.setDueDate(iDueDate);
                            item.setDescription(iDescription);
                            item.setPriority(iPriority);
                            item.setModificationDate(iModificationDate);

                            items.add(item);
                        }

                        NoteCheckList noteCheckList = new NoteCheckList();

                        noteCheckList.setUid(uid);
                        noteCheckList.setCreationDate(creationDate);
                        noteCheckList.setTitle(title);
                        noteCheckList.setColor(color);
                        noteCheckList.setContent(items);
                        noteCheckList.setModificationDate(modificationDate);

                        snapNote = noteCheckList;
                        Log.d("SYNC", "Title => "+snapNote.getTitle());
                        break;
                }

                finalNote[0] = snapNote;
                Log.d("EXEC", "Completed Data retrieval");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
            Log.d("SYNC", "FINAL Title => "+ finalNote[0].getTitle());
        } catch (Exception e) {
            Log.d("SYNC", "FINAL Title => null");
            e.printStackTrace();
        }

        Log.d("EXEC", "Completed Execution");
        return finalNote[0];
    }

    static public void uploadNote(String uid, String userName, Context context){

        Note<?> note = null;

        switch (Note.getNoteClass(uid)){
            case TEXT_NOTE: note = MySharedPreferences.LoadTextNote(uid,context); break;
            case CHECK_NOTE: note = MySharedPreferences.LoadCheckListNote(uid, context); break;
        }

        ref.child(USERS).child(userName).child(USER_NOTES).child(uid).setValue(note);
    }

}
