package com.example.colornoteplus;

import android.content.Context;

import java.io.Serializable;

public abstract class Note<T> extends Object implements Serializable {

    private String title;
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }

    private int color;
            public int getColor() { return color; }
            public void setColor(int color) { this.color = color; }

    private T content;
            public T getContent() { return content; }
            public void setContent(T content) { this.content = content; }

    public abstract void save(Context context);

    public abstract boolean hasChanged(Context context);

    public abstract void load(Context context);

    public abstract void delete(Context context);

    public static TYPE getNoteClass(String uid){
        if (uid.startsWith(Statics.NOTE_TEXT_ID)) return TYPE.TEXT_NOTE;
        if (uid.startsWith(Statics.NOTE_CHECK_ID)) return TYPE.CHECK_NOTE;

        return TYPE.TEXT_NOTE;
    }

    public enum TYPE{
        TEXT_NOTE,
        CHECK_NOTE
    }
}