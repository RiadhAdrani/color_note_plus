package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

public class CheckListItem extends Object {

    public enum PRIORITY{
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    private String description;
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

    private Long doneDate;
        public Long getDoneDate() { return doneDate; }
        public void setDoneDate(Long doneDate) { this.doneDate = doneDate; }
        public boolean isDone() { return doneDate != -1L;}

    private PRIORITY priority;
        public PRIORITY getPriority() { return priority; }
        public void setPriority(PRIORITY priority) { this.priority = priority; }

    private Long dueDate;
        public Long getDueDate() { return dueDate; }
        public void setDueDate(Long dueDate) { this.dueDate = dueDate; }

    public CheckListItem(String description){
            setDescription(description);
            setDoneDate(-1L);
            setDueDate(-1L);
            setPriority(PRIORITY.MEDIUM);
            setCreationDate(Calendar.getInstance().getTime().getTime());
            setModificationDate(Calendar.getInstance().getTime().getTime());
    }

    public CheckListItem(String description,Long dueDate,PRIORITY p){
            setDescription(description);
            setDoneDate(-1L);
            setPriority(p);
            setDueDate(dueDate);
            setCreationDate(Calendar.getInstance().getTime().getTime());
            setModificationDate(Calendar.getInstance().getTime().getTime());
    }

    public String priorityToString(Context context){

            switch (this.priority){
                case LOW: return context.getResources().getString(R.string.low_priority);
                case MEDIUM: return context.getResources().getString(R.string.medium_priority);
                case HIGH: return context.getResources().getString(R.string.high_priority);
                case URGENT: return context.getResources().getString(R.string.urgent_priority);
            }

        return context.getResources().getString(R.string.medium_priority);
    }

    public static String priorityToString(Context context, PRIORITY p){
        switch (p){
            case LOW: return context.getResources().getString(R.string.low_priority);
            case MEDIUM: return context.getResources().getString(R.string.medium_priority);
            case HIGH: return context.getResources().getString(R.string.high_priority);
            case URGENT: return context.getResources().getString(R.string.urgent_priority);
        }

        return context.getResources().getString(R.string.medium_priority);
    }

    public static ArrayList<String> getPriorities(Context context){
        ArrayList<String> p = new ArrayList<>();
        for (PRIORITY e: PRIORITY.values()) {
            p.add(priorityToString(context,e));
        }
        return p;
    }
}
