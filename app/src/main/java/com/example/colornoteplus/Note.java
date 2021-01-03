package com.example.colornoteplus;

import android.content.Context;
import android.renderscript.RenderScript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

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

    public abstract boolean isEqualTo(Note<?> note);

    public abstract Map<String, java.lang.Object> toMap();

    public abstract boolean containsString(String string);

    public static Note<?> fromMap(Context context, Map<String, java.lang.Object> map){

        String uid = (String) map.get(App.DATABASE_OBJECT_UID);
        String title = (String) map.get(App.DATABASE_NOTE_TITLE);
        Long creationDate = (Long) map.get(App.DATABASE_OBJECT_CREATION_DATE);
        Long modificationDate = (Long) map.get(App.DATABASE_OBJECT_MODIFICATION_DATE);

        if (uid == null) return null;

        String color = (String) map.get(App.DATABASE_NOTE_COLOR);

        switch (getNoteClass(uid)){
            case TEXT_NOTE:

                String content = (String) map.get(App.DATABASE_NOTE_CONTENT);

                TextNote textNote = new TextNote(context);

                textNote.setUid(uid);
                textNote.setTitle(title);
                textNote.setCreationDate(creationDate);
                textNote.setModificationDate(modificationDate);
                textNote.setContent(content);
                textNote.setColor(color != null ? Integer.parseInt(color) : Style.getAppColor(context));

                return textNote;

            case CHECK_NOTE:

                ArrayList<CheckListItem> items = new ArrayList<>();
                ArrayList<Map<String, java.lang.Object>> contentMap = (ArrayList<Map<String, java.lang.Object>>) map.get(App.DATABASE_NOTE_CONTENT);

                if (contentMap == null ) return null;

                for (Map<String, java.lang.Object> item : contentMap){

                    String itemUID = (String) item.get(App.DATABASE_OBJECT_UID);
                    String itemDescription = (String) item.get(App.DATABASE_CL_ITEM_DESCRIPTION);
                    Long itemCreationDate = (Long) item.get(App.DATABASE_OBJECT_CREATION_DATE);
                    Long itemModificationDate = (Long) item.get(App.DATABASE_OBJECT_MODIFICATION_DATE);
                    Long itemDoneDate = (Long) item.get(App.DATABASE_CL_ITEM_DONE_DATE);

                    Long itemDueDate ;
                    RenderScript.Priority itemPriority;

                    CheckListItem checkItem = new CheckListItem("",-1L, CheckListItem.PRIORITY.LOW);
                    checkItem.setUid(itemUID);
                    checkItem.setDescription(itemDescription);
                    checkItem.setCreationDate(itemCreationDate);
                    checkItem.setModificationDate(itemModificationDate);
                    checkItem.setDoneDate(itemDoneDate);

                    items.add(checkItem);

                }

                CheckListNote checkNote = new CheckListNote(context);
                checkNote.setUid(uid);
                checkNote.setTitle(title);
                checkNote.setCreationDate(creationDate);
                checkNote.setModificationDate(modificationDate);
                checkNote.setColor(color != null ? Integer.parseInt(color) : Style.getAppColor(context));
                checkNote.setContent(items);

                return checkNote;
        }

        return null;
    }

    public static TYPE getNoteClass(String uid){
        if (uid.startsWith(App.NOTE_TEXT_ID)) return TYPE.TEXT_NOTE;
        if (uid.startsWith(App.NOTE_CHECK_ID)) return TYPE.CHECK_NOTE;

        return TYPE.TEXT_NOTE;
    }

    public static TYPE getNoteClass(Note<?> note){
        if (note.getUid().startsWith(App.NOTE_TEXT_ID)) return TYPE.TEXT_NOTE;
        if (note.getUid().startsWith(App.NOTE_CHECK_ID)) return TYPE.CHECK_NOTE;

        return TYPE.TEXT_NOTE;
    }

    public enum TYPE{
        TEXT_NOTE,
        CHECK_NOTE
    }
}