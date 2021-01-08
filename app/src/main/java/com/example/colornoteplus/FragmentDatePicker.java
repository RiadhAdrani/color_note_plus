package com.example.colornoteplus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Allow to pick a date for the addition of a check list item
 * @see CheckListItem
 * @see FragmentAddCheckListItem
 * @see CheckListNoteActivity
 */
public class FragmentDatePicker extends DialogFragment {

    private int color;

    /**
     * Public constructor
     * @param color activity theme
     */
    public FragmentDatePicker(int color){
        this.color = color;
    }

    private OnDateSet listener;

    /**
     * Override the event happening after picking a new date
     * @param listener new listener
     */
    public void setOnDateSet(OnDateSet listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> listener.onDateSetListener(i,i1,i2), year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
    }

    /**
     * Allow the overriding of actions made after picking a date
     */
    public interface OnDateSet{

        /**
         * Allow the use and manipulation of the picked date time
         * @param year picked year
         * @param month picked month
         * @param day picked day
         */
        void onDateSetListener(int year,int month,int day);
    }

}
