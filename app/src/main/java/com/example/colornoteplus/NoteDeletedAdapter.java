package com.example.colornoteplus;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NoteDeletedAdapter extends NoteAdapter {


    public NoteDeletedAdapter(ArrayList<Note<?>> list, Context context) {
        super(list, context);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note<?> item = getList().get(position);
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
            if (getListener() != null) getListener().OnClickListener(holder.getAdapterPosition());
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (getListener() != null) getListener().OnLongClickListener(holder.getAdapterPosition());
            return true;
        });

        holder.moreOptionsView.setVisibility(View.GONE);
    }

}