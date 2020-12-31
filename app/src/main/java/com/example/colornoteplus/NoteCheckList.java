package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoteCheckList extends Note<ArrayList<CheckListItem>> {

    /// default constructor
    public NoteCheckList(Context context){
        setTitle(App.NOTE_PLACEHOLDER);
        setColor(Style.getAppColor(context));
        setContent(new ArrayList<>());
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(App.NOTE_CHECK_ID + UUID.randomUUID().toString() + getCreationDate());
    }

    // save the note
    @Override
    public void save(Context context) {
        setModificationDate();
        MySharedPreferences.SaveCheckListNote(this,context);
    }

    @Override
    public boolean hasChanged(Context context) {

        NoteCheckList original = MySharedPreferences.LoadCheckListNote(getUid(),context);

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

    @Override
    public boolean containsString(String string) {
        for (CheckListItem item : getContent()){
            if (item.getDescription().toLowerCase().contains(string.toLowerCase()))
                return true;
        }

        return false;
    }

}
