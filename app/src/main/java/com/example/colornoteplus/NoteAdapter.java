package com.example.colornoteplus;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    // display notes in a recycler view

    private final ArrayList<Note<?>> list;
    private OnItemClickListener listener;

    public NoteAdapter(ArrayList<Note<?>> list){
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
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

        Note<?> item = list.get(position);

        if (item.getClass() == TextNote.class){
            holder.titleView.setText(item.getTitle());
            holder.contentView.setText((String) item.getContent());}

        holder.backgroundView.setBackgroundResource( StyleManager.getBackground( item.getColor()) );

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) listener.OnClickListener(position);
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (listener != null) listener.OnLongClickListener(position);
            return true;
        });
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

    public interface OnItemClickListener{
        void OnClickListener(int position);
        void OnLongClickListener(int position);
    }
}
