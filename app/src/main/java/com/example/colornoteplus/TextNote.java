package com.example.colornoteplus;

import android.content.Context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Note having a text as its content.
 * @see Note
 */
public class TextNote extends Note<String> implements Serializable {

    public TextNote(Context context){
        setTitle(App.NOTE_PLACEHOLDER);
        setColor(Style.getAppColor(context));
        setContent(null);
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(App.NOTE_TEXT_ID + UUID.randomUUID().toString() + getCreationDate());
    }

    @Override
    public void save(Context context) {
        setModificationDate();
        DatabaseManager.setTextNote(this,context);
    }

    @Override
    public boolean hasChanged(Context context) {

        TextNote original = DatabaseManager.getTextNote(getUid(),context);

        if (getColor() != original.getColor())
            return true;

        if (!getTitle().equals(original.getTitle()))
            return true;

        if (getContent() == null) setContent("");
        if (original.getContent() == null) original.setContent("");

        return !getContent().trim().equals(original.getContent());
    }

    @Override
    public boolean isEqualTo(Note<?> note) {

        if (!note.getUid().equals(this.getUid())) return false;
        if (!note.getTitle().equals(this.getTitle())) return false;
        if (!note.getCreationDate().equals(this.getCreationDate())) return false;
        if (!note.getModificationDate().equals(this.getModificationDate())) return false;
        if (note.getColor() != this.getColor() ) return false;
        return note.getContent().equals(this.getContent());
    }

    @Override
    public Map<String, java.lang.Object> toMap() {

        Map<String, java.lang.Object> map = new HashMap<>();

        map.put(App.DATABASE_OBJECT_UID, getUid());
        map.put(App.DATABASE_OBJECT_CREATION_DATE, getCreationDate());
        map.put(App.DATABASE_OBJECT_MODIFICATION_DATE, getModificationDate());
        map.put(App.DATABASE_NOTE_TITLE, getTitle());
        map.put(App.DATABASE_NOTE_COLOR, ""+getColor());
        map.put(App.DATABASE_NOTE_CONTENT, getContent());

        return map;
    }

    @Override
    public boolean containsString(String string) {
        return getContent().toLowerCase().contains(string.toLowerCase());
    }

}