package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Blueprint class for the Check Note List object.
 * a check list note contains a list of list of check list items as content.
 * @see App
 * @see Note
 * @see Object
 * @see CheckListItem
 * @see Style
 */
public class CheckListNote extends Note<ArrayList<CheckListItem>> {

    /**
     * Constructor of the CheckListNote object
     * @param context context in which the note is created
     */
    public CheckListNote(Context context){
        setTitle(App.NOTE_PLACEHOLDER);
        setColor(Style.getAppColor(context));
        setContent(new ArrayList<>());
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(App.NOTE_CHECK_ID + UUID.randomUUID().toString() + getCreationDate());
    }

    /**
     * Save note to the local database manager.
     * update modification date of the object
     * @param context context in which the function is called
     * @see DatabaseManager
     * @see Note
     */
    @Override
    public void save(Context context) {
        setModificationDate();
        DatabaseManager.setCheckListNote(this,context);
    }

    /**
     * indicate if the note has changed comparing it to the saved instance in the database.
     * @see DatabaseManager
     * @see Note
     * @param context context in which the function is called
     * @return if the note has changed or not
     */
    @Override
    public boolean hasChanged(Context context) {

        CheckListNote original = DatabaseManager.getCheckListNote(getUid(),context);

        if (getColor() != original.getColor()) {
            return true;
        }

        if (!getTitle().equals(original.getTitle())) {
            return true;
        }

        if (getContent().size() != original.getContent().size()) {
            return true;
        }

        for (int i = 0; i < getContent().size(); i++){

            if (getContent().get(i).getPriority() != original.getContent().get(i).getPriority()) {
                return true;
            }

            if (!getContent().get(i).getDueDate().equals(original.getContent().get(i).getDueDate())) {
                return true;
            }

            if (!getContent().get(i).getCreationDate().equals(original.getContent().get(i).getCreationDate())) {
                return true;
            }

            if (!getContent().get(i).getDoneDate().equals(original.getContent().get(i).getDoneDate())) {
                return true;
            }

            if (!getContent().get(i).getDescription().trim().equals(original.getContent().get(i).getDescription())) {
                return true;
            }
        }

        return false;

    }

    /**
     * Compare the current note to a given one.
     * @param note note to be compared with
     * @return the result of the comparison
     * @see Note
     */
    @Override
    public boolean isEqualTo(Note<?> note) {

        if (!note.getUid().equals(this.getUid())) return false;
        if (!note.getTitle().equals(this.getTitle())) return false;
        if (!note.getCreationDate().equals(this.getCreationDate())) return false;
        if (!note.getModificationDate().equals(this.getModificationDate())) return false;
        if (note.getColor() != this.getColor() ) return false;

        CheckListNote checkNote = (CheckListNote) note;

        if (checkNote.getContent().size() != this.getContent().size()) return false;

        for (int i = 0; i < checkNote.getContent().size(); i++){
            if (!checkNote.getContent().get(i).isEqualTo(this.getContent().get(i))) return false;
        }

        return true;

    }

    /**
     * Convert the current note to a map, for it later to be uploaded to cloud database.
     * @see DatabaseManager
     * @see Note
     * @see Sync
     * @return the resulting hash map
     */
    @Override
    public Map<String, java.lang.Object> toMap() {
        Map<String, java.lang.Object> map = new HashMap<>();

        map.put(App.DATABASE_OBJECT_UID, getUid());
        map.put(App.DATABASE_OBJECT_CREATION_DATE, getCreationDate());
        map.put(App.DATABASE_OBJECT_MODIFICATION_DATE, getModificationDate());
        map.put(App.DATABASE_NOTE_TITLE, getTitle());
        map.put(App.DATABASE_NOTE_COLOR, ""+getColor());

        ArrayList<Map<String, java.lang.Object>> content = new ArrayList<>();
        for (CheckListItem item : getContent()){

            Map<String, java.lang.Object> mappedItem = new HashMap<>();

            mappedItem.put(App.DATABASE_OBJECT_UID, item.getUid());
            mappedItem.put(App.DATABASE_CL_ITEM_DESCRIPTION, item.getDescription());
            mappedItem.put(App.DATABASE_OBJECT_CREATION_DATE, item.getCreationDate());
            mappedItem.put(App.DATABASE_OBJECT_MODIFICATION_DATE, item.getModificationDate());
            mappedItem.put(App.DATABASE_CL_ITEM_DONE_DATE, item.getDoneDate());
            mappedItem.put(App.DATABASE_CL_ITEM_DUE_DATE, item.getDueDate());
            mappedItem.put(App.DATABASE_CL_ITEM_PRIORITY, item.getPriority());

            content.add(mappedItem);
        }

        map.put(App.DATABASE_NOTE_CONTENT, content);

        return map;
    }

    /**
     * check if the current note contains a given string in its title or content
     * @param string wanted string
     * @return the result of the research
     */
    @Override
    public boolean containsString(String string) {

        if (getTitle().toLowerCase().contains(string)) return true;

        for (CheckListItem item : getContent()){
            if (item.getDescription().toLowerCase().contains(string.toLowerCase()))
                return true;
        }

        return false;
    }

}