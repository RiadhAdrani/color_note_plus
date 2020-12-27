package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class NoteCheckList extends Note<ArrayList<CheckListItem>> {

    // default constructor
    public NoteCheckList(Context context){
        setTitle(Statics.NOTE_PLACEHOLDER);
        setColor(StyleManager.getAppColor(context));
        setContent(new ArrayList<>());
        setCreationDate(Calendar.getInstance().getTime().getTime());
        setModificationDate(Calendar.getInstance().getTime().getTime());
        setUid(Statics.NOTE_CHECK_ID + UUID.randomUUID().toString() + getCreationDate());
    }

    public NoteCheckList() {
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

    // USELESS
    // (UNUSED)
    // load the note
    @Override
    public void load(Context context) {
    }

    // (UNUSED)
    // delete the note
    @Override
    public void delete(Context context) {

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
