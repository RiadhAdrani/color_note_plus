package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Blueprint class for the check list item object.
 * @see Object
 * @see CheckListNote
 */
public class CheckListItem extends Object {

    /**
     * Contains possible priorities for a given check list item
     */
    public enum PRIORITY{
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    /**
     * Description of the item
     */
    private String description;

    /**
     * getter for CheckListItem.description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter for CheckListItem.description.
     * update modification date automatically
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
        setModificationDate();
    }

    /**
     * the date of the achievement of this item
     */
    private Long doneDate;

    /**
     * getter for CheckListItem.doneDate
     * @return doneDate
     */
    public Long getDoneDate() {
        return doneDate;
    }

    /**
     * setter for CheckListItem.doneDate
     * @param doneDate new done date
     */
    public void setDoneDate(Long doneDate) {
        this.doneDate = doneDate;
    }

    /**
     * Custom setter for CheckListItem.doneDate.
     * set the doneDate to the exact current time
     */
    public void setDone() {
        doneDate = Calendar.getInstance().getTime().getTime();
        setModificationDate();
    }

    /**
     * Custom setter for CheckListItem.doneDate.
     * set the doneDate to undone
     */
    public void setUnDone() {
        doneDate = -1L;
        setModificationDate();
    }

    /**
     * return the status of the item, if doneDate is equal to -1L : return false,
     * otherwise return true
     * @return whether the item is done or not
     */
    public boolean isDone() {
        return doneDate != -1L;
    }

    /**
     * priority of the item
     * @see PRIORITY
     */
    private PRIORITY priority;

    /**
     * getter of CheckListItem.priority
     * @return priority
     * @see PRIORITY
     */
    public PRIORITY getPriority() {
        return priority;
    }

    /**
     * setter for CheckListItem.priority.
     * Update modification date too.
     * @see PRIORITY
     * @param priority new priority value
     */
    public void setPriority(PRIORITY priority) {
        this.priority = priority;
        setModificationDate();
    }

    /**
     * the date in which the item is due
     */
    private Long dueDate;

    /**
     * getter of CheckListItem.dueDate
     * @return dueDate
     */
    public Long getDueDate() {
        return dueDate;
    }

    /**
     * setter for CheckListItem.dueDate.
     * Update modification date too.
     * @param dueDate new due date value
     */
    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
        setModificationDate();
    }

    /**
     * Custom constructor
     * @param description item description value
     * @param dueDate due date value
     * @param p priority value
     * @see PRIORITY
     */
    public CheckListItem(String description,Long dueDate,PRIORITY p){
            setDescription(description);
            setUnDone();
            setPriority(p);
            setDueDate(dueDate);
            setCreationDate(Calendar.getInstance().getTime().getTime());
            setUid(UUID.randomUUID().toString()+ getCreationDate().toString() + UUID.randomUUID().toString());
            setModificationDate();
    }

    /**
     * convert a given priority to string
     * @see PRIORITY
     * @param context context in which the function is called
     * @param p priority to be converted
     * @return priority as String
     */
    public static String priorityToString(Context context, PRIORITY p){
        switch (p){
            case LOW: return context.getResources().getString(R.string.low_priority);
            case MEDIUM: return context.getResources().getString(R.string.medium_priority);
            case HIGH: return context.getResources().getString(R.string.high_priority);
            case URGENT: return context.getResources().getString(R.string.urgent_priority);
        }

        return context.getResources().getString(R.string.medium_priority);
    }

    /**
     * @deprecated
     * return all priorities as an Array list of strings
     * @param context context in which the function is called
     * @return Array list of Strings of all priorities
     */
    public static ArrayList<String> getPriorities(Context context){
        ArrayList<String> p = new ArrayList<>();
        for (PRIORITY e: PRIORITY.values()) {
            p.add(priorityToString(context,e));
        }
        return p;
    }

    /**
     * Compare the current item to another item
     * @param item item to be compared with
     * @return true if both items are identical, false otherwise.
     */
    public boolean isEqualTo(CheckListItem item){

            if (!item.getUid().equals(this.getUid())) return false;
            if (!item.getDescription().equals(this.getDescription())) return false;
            if (!item.getCreationDate().equals(this.getCreationDate())) return false;
            if (!item.getModificationDate().equals(this.getModificationDate())) return false;
            if (!item.getDoneDate().equals(this.getDoneDate())) return false;
            if (!item.getPriority().equals(this.getPriority())) return false;
            return item.getDueDate().equals(this.getDueDate());

    }
}