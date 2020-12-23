package com.example.colornoteplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PriorityAdapter extends ArrayAdapter<CheckListItem.PRIORITY> {

    private final int color;

    public PriorityAdapter(Context context, CheckListItem.PRIORITY[] list, int color){
        super(context,0,list);
        this.color = color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position,View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_priority,parent,false
            );
        }

        TextView text = convertView.findViewById(R.id.item_priority_text);
        ConstraintLayout bg = convertView.findViewById(R.id.item_background);

        CheckListItem.PRIORITY priority = getItem(position);

        text.setText(CheckListItem.priorityToString(getContext(),priority));
        text.setTextColor(getContext().getResources().getColor(StyleManager.getColorPrimaryAccent(getContext(), color)));

        return convertView;
    }
}
