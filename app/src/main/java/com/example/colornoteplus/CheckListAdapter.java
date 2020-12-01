package com.example.colornoteplus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    public CheckListAdapter(Context context,ArrayList<CheckListItem> list, int color) {
        this.list = list;
        this.color = color;
        this.context = context;
    }

    private ArrayList<CheckListItem> list;
    private int color;
    private Context context;

    @NonNull
    @Override
    public CheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CheckListAdapter.MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_check_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.MyViewHolder holder, int position) {

        CheckListItem currentItem = list.get(position);

        if (currentItem == null){
            Log.d("DEBUG_NULL","current item is null");
        }

        holder.background.setBackgroundResource(StyleManager.getBackgroundLight(color));

        holder.title.setTextColor(context.getResources().getColor(StyleManager.getThemeColorDark(color)));
        holder.title.setHintTextColor(context.getResources().getColor(StyleManager.getThemeColor(color)));
        holder.title.setText(currentItem.getDescription().trim());

        holder.dueTimeText.setTextColor(context.getResources().getColor(StyleManager.getThemeColor(color)));
        holder.dueTimeText.setText(currentItem.getDoneDate() != -1 ? DateFormat.getDateInstance().format(new Date(currentItem.getDueDate())) : context.getString(R.string.set_reminder));

        holder.priorityText.setTextColor(context.getResources().getColor(StyleManager.getThemeColor(color)));
        holder.priorityText.setText(currentItem.priorityToString(context));

        holder.delete.setBackgroundResource(StyleManager.getBackground(color));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        ConstraintLayout background;
        EditText title;
        ImageButton delete;
        TextView priorityText,dueTimeText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.item_check_box);
            title = itemView.findViewById(R.id.item_title);
            dueTimeText = itemView.findViewById(R.id.item_due_time_text);
            priorityText = itemView.findViewById(R.id.item_priority_text);
            delete = itemView.findViewById(R.id.item_delete);
            background = itemView.findViewById(R.id.item_background);

        }
    }
}
