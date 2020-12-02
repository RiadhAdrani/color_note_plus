package com.example.colornoteplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    final private ArrayList<CheckListItem> list;
    final private int color;
    final private Context context;
    private OnItemClickListener listener;


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

        holder.background.setBackgroundResource(StyleManager.getBackgroundLight(color));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) listener.onChecked(position);
                else listener.onUnchecked(position);
            }
        });

        holder.title.setTextColor(context.getResources().getColor(StyleManager.getThemeColorDark(color)));
        holder.title.setHintTextColor(context.getResources().getColor(StyleManager.getThemeColor(color)));
        assert currentItem != null;
        holder.title.setText(currentItem.getDescription().trim());

        holder.dueTimeText.setTextColor(context.getResources().getColor(StyleManager.getThemeColor(color)));
        holder.dueTimeText.setText(currentItem.getDoneDate() != -1 ? DateFormat.getDateInstance().format(new Date(currentItem.getDueDate())) : context.getString(R.string.set_reminder));
        holder.dueTimeText.setOnClickListener(view -> listener.onSetReminder(position));

        holder.priorityText.setTextColor(context.getResources().getColor(StyleManager.getThemeColor(color)));
        holder.priorityText.setText(currentItem.priorityToString(context));
        holder.priorityText.setOnClickListener(view -> listener.onSetPriority(position));

        holder.delete.setBackgroundResource(StyleManager.getBackground(color));
        holder.delete.setOnClickListener(view -> listener.onDelete(position));
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

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onChecked(int position);
        void onUnchecked(int position);
        void onSetPriority(int position);
        void onSetReminder(int position);
        void onDelete(int position);
    }
}
