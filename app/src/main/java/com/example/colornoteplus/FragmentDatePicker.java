package com.example.colornoteplus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FragmentDatePicker extends DialogFragment {

    private int color;

    public FragmentDatePicker(int color){
        this.color = color;
    }

    private OnDateSet listener;

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

    public interface OnDateSet{
        void onDateSetListener(int year,int month,int day);
    }

}
