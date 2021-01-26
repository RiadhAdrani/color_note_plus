package com.example.colornoteplus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter allowing the display of notes
 * @see Note
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> implements Filterable {

    // display notes in a recycler view

    /**
     * Adapter selection modes
     */
    enum SelectionMode{

        /**
         * Normal mode
         */
        NO_SELECTION,

        /**
         * Allow selection of multiple items in the adapter
         */
        SELECTION
    }

    /**
     * Selection mode
     * @see SelectionMode
     */
    private SelectionMode selectionMode = SelectionMode.NO_SELECTION;

    /**
     * getter for selection mode
     * @return current selection mode
     */
    public SelectionMode getSelectionMode() { return selectionMode; }

    /**
     * setter for selection mode
     * @param selectionMode new selection mode
     */
    public void setSelectionMode(SelectionMode selectionMode) { this.selectionMode = selectionMode; }

    /**
     * selected items
     */
    private ArrayList<String> selectedItems = new ArrayList<>();

    /**
     * getter for selected items
     * @return selected items
     */
    public ArrayList<String> getSelectedItems() { return selectedItems; }

    /**
     * setter for selected items
     * @param selectedItems new selected items
     */
    public void setSelectedItems(ArrayList<String> selectedItems) { this.selectedItems = selectedItems; }

    /**
     * check if an item (note) is selected
     * @param uid note uid in question
     * @return true if it is selected, else false
     */
    public boolean isSelected(String uid){
            for (String item : selectedItems){
                if (item.equals(uid)){
                    return true;
                }
            }

            return false;
    }

    /**
     * content to be displayed
     */
    private final ArrayList<Note<?>> list;

    /**
     * get the list of displayed content
     * @return note list
     */
    public ArrayList<Note<?>> getList() {
        return list;
    }

    /**
     * get the adapter listener
     * @see DeletedNoteAdapter
     * @return listener
     */
    public OnItemClickListener getListener() {
        return listener;
    }

    /**
     * get adapter creation context
     * @see DeletedNoteAdapter
     * @return creation context
     */
    public Context getContext() {
        return context;
    }

    /**
     * The full list of the content
     */
    private final ArrayList<Note<?>> listFull;

    /**
     * adapter listener
     */
    private OnItemClickListener listener;

    /**
     * Creation context
     */
    private final Context context;

    /**
     * public constructor
     * @param list a list of notes to be displayed
     * @param context creation context
     */
    public NoteAdapter(ArrayList<Note<?>> list, Context context){

        this.list = list;
        listFull = new ArrayList<>(list);
        this.context = context;
    }

    /**
     * getter for the full list of items
     * @return the full list
     */
    public ArrayList<Note<?>> getListFull() {return listFull;}

    /**
     * allow the overriding of the listener
     * @param listener new listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_note,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.MyViewHolder holder, int position) {

        Note<?> item = list.get(position);
        holder.titleView.setText(item.getTitle());

        if (item.getClass() == TextNote.class){
            holder.contentView.setText((String) item.getContent());}

        if (item.getClass() == CheckListNote.class){
            String content = "";
            for (CheckListItem i: ((CheckListNote) item).getContent()) {
                content += "• " + i.getDescription()+"\n";
            }
            holder.contentView.setText(content);
        }

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) listener.OnClickListener(holder.getAdapterPosition());
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (listener != null) listener.OnLongClickListener(holder.getAdapterPosition());
            return true;
        });

        holder.line.setBackgroundColor(context.getResources().getColor(Style.getCustomColor(context,position, Style.COLORS.WHITE, Style.COLORS.WHITE)));

        holder.moreOptionsView.setVisibility(View.VISIBLE);
        holder.moreOptionsView.setOnClickListener(view -> {

                PopupMenu menu = new PopupMenu(context,holder.moreOptionsView);
                menu.setOnMenuItemClickListener(menuItem -> {

                    final int delete = R.id.item_option_delete;
                    final int color = R.id.item_option_color;

                    switch (menuItem.getItemId()){
                        case delete: if (listener != null) listener.OnOptionOneClick(holder.getAdapterPosition()); return true;
                        case color: if (listener != null) listener.OnOptionTwoClick(holder.getAdapterPosition()); return true;
                    }

                    return false;
                });

                menu.inflate(R.menu.menu_note_more_options);
                menu.show();
            });

        if (isSelected(item.getUid())){
            switch(Style.getAppTheme(context)){
                case 0:
                    holder.backgroundView.setBackgroundResource(
                            Style.getBackgroundPrimary(context,item.getColor()) );
                    break;
                case 1:
                    holder.backgroundView.setBackgroundResource(
                            Style.getBackgroundSecondary(context,item.getColor()) );
                    break;
            }
        }
        else {
            holder.backgroundView.setBackgroundResource( Style.getBackground( getContext(), item.getColor()) );
        }

        if (selectionMode == SelectionMode.SELECTION){
            holder.moreOptionsView.setVisibility(View.INVISIBLE);
            holder.selectionIcon.setVisibility(View.VISIBLE);
            if (isSelected(item.getUid())){
                holder.selectionIcon.setImageResource(R.drawable.ic_checked_circle);
            }
            else {
                holder.selectionIcon.setImageResource(R.drawable.ic_unchecked_circle);
            }
        } else {
            holder.moreOptionsView.setVisibility(View.VISIBLE);
            holder.selectionIcon.setVisibility(View.GONE);
        }

    }

    /**
     * remove and item from the list and update the adapter
     * and send it to the recycled notes bin.
     * @see DatabaseManager
     * @param position index of the item to be removed
     */
    public void removeItem(int position){

        String uid = list.get(position).getUid();

        for (Note<?> note : listFull) {
            if (note.getUid().equals(uid)){
                listFull.remove(note);
                break;
            }
        }

        ArrayList<String> trash = DatabaseManager.getStringArray(App.KEY_NOTE_LIST_TRASH,context);
        trash.add(uid);
        DatabaseManager.setStringArray(trash, App.KEY_NOTE_LIST_TRASH,context);

        list.remove(position);
        notifyItemRemoved(position);
        DatabaseManager.setModificationDate(context);
    }

    /**
     * remove and delete an item permanently from the storage.
     * @see DatabaseManager
     * @param position index of the item to be deleted
     */
    public void deleteItemPermanently(int position){

        String uid = list.get(position).getUid();

        for (Note<?> note : listFull) {
            if (note.getUid().equals(uid)){
                listFull.remove(note);
                break;
            }
        }

        DatabaseManager.deleteNote(uid,getContext());

        list.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * Restore an item from the recycled notes bin into my notes.
     * @see DatabaseManager
     * @param position index of the item to be restored
     */
    public void restoreItem(int position){
        String uid = list.get(position).getUid();

        for (Note<?> note : listFull) {
            if (note.getUid().equals(uid)){
                listFull.remove(note);
                break;
            }
        }

        ArrayList<String> noteList = DatabaseManager.getStringArray(App.KEY_NOTE_LIST,getContext());
        noteList.add(uid);
        DatabaseManager.setStringArray(noteList, App.KEY_NOTE_LIST,getContext());

        list.remove(position);
        notifyItemRemoved(position);
        DatabaseManager.setModificationDate(context);
    }

    /**
     * change the color of an item.
     * @see Style
     * @see Note
     * @param position note index
     * @param newColor override with new color
     */
    public void switchColor(int position, int newColor){

        String uid = list.get(position).getUid();

        for (Note<?> note : listFull) {
            if (note.getUid().equals(uid)){
                note.setColor(newColor);
                note.save(getContext());
                break;
            }
        }

        list.get(position).setColor(newColor);
        notifyItemChanged(position);
        DatabaseManager.setModificationDate(context);
    }

    /**
     * Sort notes by title
     * @param isDescending indicate if sorting is descending or not
     */
    public void sortByTitle(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getTitle().compareTo(i2.getTitle()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort notes by creation date
     * @param isDescending indicate if sorting is descending or not
     */
    public void sortByCreation(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getCreationDate().compareTo(i2.getCreationDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort notes by modification date
     * @param isDescending indicate if sorting is descending or not
     */
    public void sortByModification(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getModificationDate().compareTo(i2.getModificationDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Sort notes by their color index
     * @param isDescending indicate if sorting is descending or not
     */
    public void sortByColor(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getColor()-i2.getColor());
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    /**
     * Select and deselect items depending on their status
     * @param position selected item position
     */
    public void selectDeselectItem(int position){

        if (!isSelected(list.get(position).getUid())){
            selectedItems.add(list.get(position).getUid());
        } else {
            selectedItems.remove(list.get(position).getUid());
        }
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Note<?>> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(listFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase();
                for (Note<?> item: listFull){
                    if (item.containsString(filterPattern))
                        filteredList.add(item);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout backgroundView;
        TextView titleView;
        TextView contentView;
        ImageButton moreOptionsView;
        ImageView selectionIcon;
        View line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundView = itemView.findViewById(R.id.item_background);
            titleView = itemView.findViewById(R.id.item_title);
            contentView = itemView.findViewById(R.id.item_content);
            moreOptionsView = itemView.findViewById(R.id.item_more_options);
            line = itemView.findViewById(R.id.item_line);
            selectionIcon = itemView.findViewById(R.id.item_selected);
        }
    }

    public interface OnItemClickListener{
        void OnClickListener(int position);
        void OnLongClickListener(int position);
        void OnOptionOneClick(int position);
        void OnOptionTwoClick(int position);
    }
}
