package com.example.colornoteplus;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Adapter managing how items (CheckListItems) are display in a recycler view.
 * @see CheckListItem
 * @see CheckListNote
 * @see CheckListNoteActivity
 */
public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    /**
     * Public constructor : Initialize a checkListAdapter
     * @param context context in which the object is created
     * @param list content to be displayed
     * @param color color theme of the adapter. In most cases, it is the color of note
     */
    public CheckListAdapter(Context context,ArrayList<CheckListItem> list, int color) {
        this.list = list;
        this.color = color;
        this.context = context;
    }

    /**
     * List of items to be displayed
     * @see CheckListItem
     */
    final private ArrayList<CheckListItem> list;

    /**
     * Color theme of the adapter
     * @see Style
     */
    final private int color;

    /**
     * Context
     */
    final private Context context;

    /**
     * Sorting type of the items
     * @see App.SORT_ITEM
     */
    private App.SORT_ITEM sortBy;

    /**
     * getter of CheckListAdapter.sortBy
     * @see App.SORT_ITEM
     * @return sortBy
     */
    public App.SORT_ITEM getSortBy(){
        return sortBy;
    }

    /**
     * setter for CheckListAdapter.sortBy
     * @param sort new sorting type
     * @see App.SORT_ITEM
     */
    public void setSortBy(App.SORT_ITEM sort){
        this.sortBy = sort;
    }

    /**
     * Listener for even and action made to the displayed items
     */
    private OnItemClickListener listener;


    /**
     *
     * @param parent parent of the viewHolder
     * @param viewType viewType
     * @return the layout that will represent every single item
     */
    @NonNull
    @Override
    public CheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CheckListAdapter.MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_check_list_v2,parent,false));
    }

    /**
     * Fill the appropriate holder with the correspondent object data from the list items
     * @param holder currently drawn layout
     * @param position current position of the holder
     */
    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.MyViewHolder holder, int position) {

        // current item
        CheckListItem currentItem = list.get(position);

        // change background color
        holder.background.setBackgroundResource(Style.getBackgroundSecondary(context,color));

        // initializing the checkbox
        holder.checkBox.setChecked(currentItem.isDone());
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {

            // if checked
            if (b) {
                listener.onChecked(holder.getAdapterPosition());
                holder.title.setPaintFlags(holder.title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }

            // if unchecked
            else {
                listener.onUnchecked(holder.getAdapterPosition());
                holder.title.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });

        // initializing the description
        holder.title.setTextColor(context.getResources().getColor(Style.getNeutralTextColor(context)));
        holder.title.setHintTextColor(context.getResources().getColor(Style.getColorMain(context,color)));
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
                // save the current item when the user add/remove
                // anything from the EditText
                listener.onDescriptionChanged(holder.getAdapterPosition(),editable.toString());
            }
        });

        // if the current item is done
        // set the description paint flag
        if (currentItem.isDone()){
            holder.title.setPaintFlags(holder.title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // initializing the due time
        holder.dueTimeText.setTextColor(context.getResources().getColor(Style.getColorPrimaryAccent(context, color)));
        holder.dueTimeText.setVisibility(View.GONE);

        // TODO: show remaining time instead of the due date
        //  if due date is less than 24 hours, display remaining hours and minutes
        //  if due date is more than 24 hours and less than 14 days, display remaining days
        //  if due date is more than 14 days, simply display the due date.
        holder.dueTimeText.setText(currentItem.getDueDate() != -1 ? DateFormat.getDateInstance().format(new Date(currentItem.getDueDate())) : context.getString(R.string.set_reminder));
        holder.dueTimeText.setOnClickListener(view -> listener.onSetReminder(holder.getAdapterPosition()));

        // initializing the priority
        PriorityAdapter adapter = new PriorityAdapter(context, CheckListItem.PRIORITY.values(),color);
        holder.priorityText.setVisibility(View.GONE);
        holder.priorityText.setAdapter(adapter);
        holder.priorityText.setSelection(currentItem.getPriority().ordinal());
        holder.priorityText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onSetPriority(holder.getAdapterPosition(),i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO: Useless
            }
        });

        // initializing the delete button
        holder.delete.setBackgroundResource(Style.getBackground(context,color));
        holder.delete.setOnClickListener(view -> listener.onDelete(holder.getAdapterPosition()));
    }

    /**
     * get the number of items in the list
     * @return number of items in the given list
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Custom class for the viewHolder that will be displayed
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        ConstraintLayout background;
        EditText title;
        ImageButton delete;
        TextView dueTimeText;
        Spinner priorityText;

        // initializing the view
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

    /**
     * Add item to the list and refresh the display
     * @param item element to be added
     * @param position the index in which the item will be inserted
     */
    public void addItem(CheckListItem item,int position){

        // if the position is negative, add the item at the bottom of the list
        if (position < 0){
            list.add(item);
            notifyItemInserted(list.size());
        }

        // add the item at the desired position
        else {
            list.add(position,item);
            notifyItemInserted(position);
        }
    }

    /**
     * Remove an item from the list and refresh the display
     * @param position the index of the item that will be deleted
     */
    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Sort items by their description
     * @see CheckListItem
     * @param isDescending indicate whether the sorting is descending or not
     */
    public void sortByDescription(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getDescription().compareTo(i2.getDescription()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort items by their status (done or not)
     * @see CheckListItem
     * @param isDescending indicate whether the sorting is descending or not
     */
    public void sortByStatus(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getDoneDate().compareTo(i2.getDoneDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort items by creation time
     * @see CheckListItem
     * @param isDescending indicate whether the sorting is descending or not
     */
    public void sortByCreation(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getCreationDate().compareTo(i2.getCreationDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort items by modification time
     * @see CheckListItem
     * @param isDescending indicate whether the sorting is descending or not
     */
    public void sortByModification(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getModificationDate().compareTo(i2.getModificationDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }


    /**
     * Sort items by due time
     * @see CheckListItem
     * @param isDescending indicate whether the sorting is descending or not
     */
    public void sortByDue(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getDueDate().compareTo(i2.getDueDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort items by their priority
     * @see CheckListItem
     * @param isDescending indicate whether the sorting is descending or not
     */
    public void sortByPriority(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getPriority().compareTo(i2.getPriority()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * override the action of the listener
     * @see OnItemClickListener
     * @param listener new listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * interface containing useful data that could be implemented to handle and edit check list items
     */
    public interface OnItemClickListener{
        void onChecked(int position);
        void onUnchecked(int position);
        void onSetPriority(int position,int priority);
        void onSetReminder(int position);
        void onDelete(int position);
        void onDescriptionChanged(int position,String description);
    }
}