package com.example.colornoteplus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    private ArrayList<CheckListItem> list;
    private int color;

    @NonNull
    @Override
    public CheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CheckListAdapter.MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_check_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        EditText title;
        ImageButton dueTime,priority,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.findViewById(R.id.item_add).setVisibility(View.GONE);
            checkBox = itemView.findViewById(R.id.item_check_box);
            title = itemView.findViewById(R.id.item_title);
            dueTime = itemView.findViewById(R.id.item_due_time);
            priority = itemView.findViewById(R.id.item_priority);
            delete = itemView.findViewById(R.id.item_delete);
        }
    }
}
