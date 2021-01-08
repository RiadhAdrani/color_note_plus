package com.example.colornoteplus;

import android.content.Context;
import android.renderscript.RenderScript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Abstract class representing a note object in the app
 * @see NoteAdapter
 * @see TextNote
 * @see CheckListNote
 * @param <T> content of the note
 */
public abstract class Note<T> extends Object implements Serializable {

    /**
     * Title of the note
     */
    private String title;

    /**
     * getter for the title
     * @return note title
     */
    public String getTitle() { return title; }

    /**
     * setter for the title
     * @param title new title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Note color id
     * @see Style
     */
    private int color;

    /**
     * getter for the color
     * @return note color
     */
    public int getColor() { return color; }

    /**
     * setter for the color
     * @param color new color id
     */
    public void setColor(int color) { this.color = color; }

    /**
     * Content
     */
    private T content;

    /**
     * getter for the content
     * @return note content
     */
    public T getContent() { return content; }

    /**
     * setter for the content
     * @param content new content
     */
    public void setContent(T content) { this.content = content; }

    /**
     * save the note in the local database
     * @see DatabaseManager
     * @param context calling context
     */
    public abstract void save(Context context);

    /**
     * Check whether the note has changed or not compared to the stored
     * one in the local database
     * @see DatabaseManager
     * @param context calling context
     * @return true if it changed, else false
     */
    public abstract boolean hasChanged(Context context);

    /**
     * @deprecated
     * Compare the current note to the other
     * @param note note to be compared with
     * @return true if are the same, else false
     */
    public abstract boolean isEqualTo(Note<?> note);

    /**
     * convert the note to a map
     * @see Sync
     * @return a Map<String, java.lang.Object> : String -> Field, java.lang.Object -> Content of the field
     */
    public abstract Map<String, java.lang.Object> toMap();

    /**
     * Check if the note contains a string
     * @param string string to find
     * @return true if it contains string, else false
     */
    public abstract boolean containsString(String string);

    /**
     * Convert a map (Map<String, java.lang.Object>) to a Note
     * @see Sync
     * @param context calling context
     * @param map map to convert
     * @return a note
     */
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

    /**
     * Return the type of the note
     * @see TYPE
     * @param uid note uid
     * @return note type
     */
    public static TYPE getNoteClass(String uid){
        if (uid.startsWith(App.NOTE_TEXT_ID)) return TYPE.TEXT_NOTE;
        if (uid.startsWith(App.NOTE_CHECK_ID)) return TYPE.CHECK_NOTE;

        return TYPE.TEXT_NOTE;
    }

    /**
     * @deprecated
     * Return the type of the note
     * @see TYPE
     * @param note given note
     * @return note type
     */
    public static TYPE getNoteClass(Note<?> note){
        if (note.getUid().startsWith(App.NOTE_TEXT_ID)) return TYPE.TEXT_NOTE;
        if (note.getUid().startsWith(App.NOTE_CHECK_ID)) return TYPE.CHECK_NOTE;

        return TYPE.TEXT_NOTE;
    }

    /**
     * Types of the notes extending Note class
     * @see Note
     */
    public enum TYPE{

        /**
         * Text note type
         * @see TextNote
         */
        TEXT_NOTE,

        /**
         * Check list note
         * @see CheckListNote
         */
        CHECK_NOTE
    }
}