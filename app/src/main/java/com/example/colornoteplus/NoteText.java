package com.example.colornoteplus;

import android.content.Context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class NoteText extends Note<String> implements Serializable {

    // public constructor
    public NoteText(String name, int color){
        setTitle(name);
        setColor(color);
        setContent(null);
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(UUID.randomUUID().toString() + getCreationDate());
    }

    public NoteText(){
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
    public boolean hasChanged(Context context) {

        NoteText original = MySharedPreferences.LoadTextNoteFromSharedPreferences(getUid(),context);

        if (getColor() != original.getColor())
            return true;

        if (!getTitle().equals(original.getTitle()))
            return true;

        return !getContent().equals(original.getContent());
    }

    @Override
    public void load(Context context) {
        NoteText temp = MySharedPreferences.LoadTextNoteFromSharedPreferences(this.getUid(),context);
        setTitle(temp.getTitle());
        setContent(temp.getContent());
        setColor(temp.getColor());
    }

    @Override
    public void delete(Context context) {
    }

}