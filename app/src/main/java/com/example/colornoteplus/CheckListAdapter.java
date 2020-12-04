package com.example.colornoteplus;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

    // public constructor
    public CheckListAdapter(Context context,ArrayList<CheckListItem> list, int color) {
        this.list = list;
        this.color = color;
        this.context = context;
    }

    // list of objects
    final private ArrayList<CheckListItem> list;

    // the current theme of the activity
    final private int color;

    // current context
    final private Context context;

    // listener
    private OnItemClickListener listener;


    // get the desired view for the current item
    @NonNull
    @Override
    public CheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CheckListAdapter.MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_check_list,parent,false));
    }

    // bind data and display them in the desired view
    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.MyViewHolder holder, int position) {

        // current item
        CheckListItem currentItem = list.get(position);

        // change background color
        holder.background.setBackgroundResource(StyleManager.getBackgroundLight(color));

        // initializing the checkbox
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) listener.onChecked(holder.getAdapterPosition());
            else listener.onUnchecked(holder.getAdapterPosition());
        });

        // initializing the description
        holder.title.setTextColor(context.getResources().getColor(StyleManager.getColorPrimary(color)));
        holder.title.setHintTextColor(context.getResources().getColor(StyleManager.getColorMain(color)));
        assert currentItem != null;
        holder.title.setText(currentItem.getDescription().trim());
        holder.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onDescriptionChanged(holder.getAdapterPosition(),editable.toString());
            }
        });

        // initializing the due time
        holder.dueTimeText.setTextColor(context.getResources().getColor(StyleManager.getColorMain(color)));

        // TODO: show remaining time instead of the due date
        //  if due date is less than 24 hours, display remaining hours and minutes
        //  if due date is more than 24 hours and less than 14 days, display remaining days
        //  if due date is more than 14 days, simply display the due date.
        holder.dueTimeText.setText(currentItem.getDueDate() != -1 ? DateFormat.getDateInstance().format(new Date(currentItem.getDueDate())) : context.getString(R.string.set_reminder));
        holder.dueTimeText.setOnClickListener(view -> listener.onSetReminder(holder.getAdapterPosition()));

        // initializing the priority
        holder.priorityText.setTextColor(context.getResources().getColor(StyleManager.getColorMain(color)));
        holder.priorityText.setText(currentItem.priorityToString(context));
        holder.priorityText.setOnClickListener(view -> listener.onSetPriority(holder.getAdapterPosition()));

        // initializing the delete button
        holder.delete.setBackgroundResource(StyleManager.getBackground(color));
        holder.delete.setOnClickListener(view -> listener.onDelete(holder.getAdapterPosition()));
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return list.size();
    }

    // initializing elements in the view
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

    // add item to the list
    // and play the animation
    public void addItem(CheckListItem item,int position){
        if (position < 0){
            list.add(item);
            notifyItemInserted(list.size());
        }
        else {
            list.add(position,item);
            notifyItemInserted(position);
        }
    }

    // delete item from the list
    // and play a simple animation
    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    // override the current listener
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // public interface
    public interface OnItemClickListener{
        void onChecked(int position);
        void onUnchecked(int position);
        void onSetPriority(int position);
        void onSetReminder(int position);
        void onDelete(int position);
        void onDescriptionChanged(int position,String description);
    }
}
