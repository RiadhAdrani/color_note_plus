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

    public abstract void load(Context context);

    public abstract void delete(Context context);
}