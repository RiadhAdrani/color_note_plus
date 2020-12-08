package com.example.colornoteplus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{

    // display notes in a recycler view

    private final ArrayList<Note<?>> list;
    private OnItemClickListener listener;
    private final Context context;

    public NoteAdapter(ArrayList<Note<?>> list, Context context){

        this.list = list;
        this.context = context;
    }

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

        holder.backgroundView.setBackgroundResource( StyleManager.getBackground( item.getColor()) );

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) listener.OnClickListener(holder.getAdapterPosition());
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (listener != null) listener.OnLongClickListener(holder.getAdapterPosition());
            return true;
        });

        holder.moreOptionsView.setOnClickListener(view -> {

            PopupMenu menu = new PopupMenu(context,holder.moreOptionsView);
            menu.setOnMenuItemClickListener(menuItem -> {

                final int delete = R.id.item_option_delete;
                final int color = R.id.item_option_color;

                switch (menuItem.getItemId()){
                    case delete: if (listener != null) listener.OnDeleteClickListener(holder.getAdapterPosition()); return true;
                    case color: if (listener != null) listener.OnColorSwitchClickListener(holder.getAdapterPosition()); return true;
                }

                return false;
            });

            menu.inflate(R.menu.menu_note_more_options);
            menu.show();
        });
    }

    public void sortByTitle(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getTitle().compareTo(i2.getTitle()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    public void sortByCreation(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getCreationDate().compareTo(i2.getCreationDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    public void sortByModification(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getModificationDate().compareTo(i2.getModificationDate()));
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    public void sortByColor(boolean isDescending){
        Collections.sort(list, (i1, i2) -> i1.getColor()-i2.getColor());
        if (isDescending) Collections.reverse(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout backgroundView;
        TextView titleView;
        TextView contentView;
        ImageButton moreOptionsView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundView = itemView.findViewById(R.id.item_background);
            titleView = itemView.findViewById(R.id.item_title);
            contentView = itemView.findViewById(R.id.item_content);
            moreOptionsView = itemView.findViewById(R.id.item_more_options);
        }
    }

    public interface OnItemClickListener{
        void OnClickListener(int position);
        void OnLongClickListener(int position);
        void OnDeleteClickListener(int position);
        void OnColorSwitchClickListener(int position);
    }
}
