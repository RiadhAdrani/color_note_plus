package com.example.colornoteplus;

import android.content.Context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class TextNote extends Note<String> implements Serializable {

    // public constructor
    public TextNote(String name,int color){
        setTitle(name);
        setColor(color);
        setContent(null);
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(UUID.randomUUID().toString() + getCreationDate());
    }

    public TextNote(){
        setTitle(Statics.NOTE_PLACEHOLDER);
        setColor(Statics.NOTE_DEFAULT_COLOR);
        setContent(null);
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(UUID.randomUUID().toString() + getCreationDate());
    }

    @Override
    public void save(Context context) {
        setModificationDate();
        MySharedPreferences.SaveTextNoteToSharedPreferences(this,context);
    }

    @Override
    public void load(Context context) {
        TextNote temp = MySharedPreferences.LoadTextNoteFromSharedPreferences(this.getUid(),context);
        setTitle(temp.getTitle());
        setContent(temp.getContent());
        setColor(temp.getColor());
    }

    @Override
    public void delete(Context context) {
    }

}