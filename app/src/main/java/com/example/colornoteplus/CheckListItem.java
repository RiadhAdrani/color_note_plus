package com.example.colornoteplus;

import java.util.Calendar;

public class CheckListItem extends Object {

    public enum PRIORITY{
        VERY_LOW,
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
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
}
