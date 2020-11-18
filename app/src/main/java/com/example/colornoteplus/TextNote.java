package com.example.colornoteplus;

import java.util.Calendar;

public class TextNote extends Note<String> {

    // public constructor
    public TextNote(String name,int color){
        setTitle(name);
        setColor(color);
        setContent(null);
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
    }

}