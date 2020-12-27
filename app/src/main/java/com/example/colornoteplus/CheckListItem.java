package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class CheckListItem extends Object {

    // enumerated list containing the possible priorities
    public enum PRIORITY{
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    public CheckListItem() {
    }

    // description of the item
    private String description;
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
            setModificationDate();
        }

    // date in which the item was done
    private Long doneDate;
        public Long getDoneDate() {
            return doneDate; }
        public void setDoneDate(Long doneDate) {
            this.doneDate = doneDate;}
        public void setDone() {
            doneDate = Calendar.getInstance().getTime().getTime();
            setModificationDate(); }
        public void setUnDone() {
            doneDate = -1L;
            setModificationDate(); }
        public boolean isDone() {
            return doneDate != -1L; }

    // priority of the item
    private PRIORITY priority;
        public PRIORITY getPriority() {
            return priority;
        }
        public void setPriority(PRIORITY priority) {
            this.priority = priority;
            setModificationDate();
        }

    // the date before which the item should be done
    private Long dueDate;
        public Long getDueDate() { return dueDate; }
        public void setDueDate(Long dueDate) {
            this.dueDate = dueDate;
            setModificationDate();
        }

    // (UNUSED)
    // default constructor
    public CheckListItem(String description){
            setDescription(description);
            setUnDone();
            setDueDate(-1L);
            setPriority(PRIORITY.MEDIUM);
            setCreationDate(Calendar.getInstance().getTime().getTime());
            setModificationDate();
    }

    // custom constructor
    public CheckListItem(String description,Long dueDate,PRIORITY p){
            setDescription(description);
            setUnDone();
            setPriority(p);
            setDueDate(dueDate);
            setCreationDate(Calendar.getInstance().getTime().getTime());
            setUid(UUID.randomUUID().toString()+ getCreationDate().toString() + UUID.randomUUID().toString());
            setModificationDate();
    }

    // convert a given priority to a string
    public static String priorityToString(Context context, PRIORITY p){
        switch (p){
            case LOW: return context.getResources().getString(R.string.low_priority);
            case MEDIUM: return context.getResources().getString(R.string.medium_priority);
            case HIGH: return context.getResources().getString(R.string.high_priority);
            case URGENT: return context.getResources().getString(R.string.urgent_priority);
        }

        return context.getResources().getString(R.string.medium_priority);
    }

    // (UNUSED)
    // convert the list of priorities to an ArrayList
    public static ArrayList<String> getPriorities(Context context){
        ArrayList<String> p = new ArrayList<>();
        for (PRIORITY e: PRIORITY.values()) {
            p.add(priorityToString(context,e));
        }
        return p;
    }
}
