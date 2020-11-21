package com.example.colornoteplus;

import org.w3c.dom.Text;

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
        setTitle(CONST.NOTE_PLACEHOLDER);
        setColor(CONST.NOTE_DEFAULT_COLOR);
        setContent(null);
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(UUID.randomUUID().toString() + getCreationDate());
    }

}