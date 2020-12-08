package com.example.colornoteplus;

import android.content.Context;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class CheckListNote extends Note<ArrayList<CheckListItem>> {

    // default constructor
    public CheckListNote(){
        setTitle(Statics.NOTE_PLACEHOLDER);
        setColor(Statics.NOTE_DEFAULT_COLOR);
        setContent(new ArrayList<>());
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(Statics.NOTE_CHECK_ID + UUID.randomUUID().toString() + getCreationDate());
    }

    // save the note
    @Override
    public void save(Context context) {
        setModificationDate();
        MySharedPreferences.SaveCheckListNoteToSharedPreferences(this,context);
    }

    // USELESS
    // (UNUSED)
    // load the note
    @Override
    public void load(Context context) {
    }

    // (UNUSED)
    // delete the note
    @Override
    public void delete(Context context) {

    }

}
