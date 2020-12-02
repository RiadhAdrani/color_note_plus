package com.example.colornoteplus;

import android.content.Context;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class CheckListNote extends Note<ArrayList<CheckListItem>> {

    public CheckListNote(){
        setTitle(Statics.NOTE_PLACEHOLDER);
        setColor(Statics.NOTE_DEFAULT_COLOR);
        setContent(new ArrayList<>());
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(UUID.randomUUID().toString() + getCreationDate());
    }

    public CheckListNote(String name,int color){
        setTitle(name);
        setColor(color);
        setContent(new ArrayList<>());
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(UUID.randomUUID().toString() + getCreationDate());
    }

    @Override
    public void save(Context context) {
        MySharedPreferences.SaveCheckListNoteToSharedPreferences(this,context);
    }

    @Override
    public void load(Context context) {
    }

    @Override
    public void delete(Context context) {

    }
}
