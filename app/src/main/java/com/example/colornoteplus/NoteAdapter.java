package com.example.colornoteplus;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    // display notes in a recycler view

    private final ArrayList<Note<?>> list;

    public NoteAdapter(ArrayList<Note<?>> list){
        this.list = list;
    }

    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.note_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.MyViewHolder holder, int position) {

        Note item = list.get(position);

        if (item.getClass() == TextNote.class){
            item = (TextNote) list.get(position);
            holder.titleView.setText(item.getTitle());
            holder.contentView.setText((String) item.getContent());

            // TODO: set background color
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout backgroundView;
        TextView titleView;
        TextView contentView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundView = itemView.findViewById(R.id.item_background);
            titleView = itemView.findViewById(R.id.item_title);
            contentView = itemView.findViewById(R.id.item_content);
        }
    }
}
