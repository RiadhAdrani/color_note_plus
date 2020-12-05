package com.example.colornoteplus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {

    private final ArrayList<BackgroundColor> list;

    private OnItemClickListener listener;

    public ColorAdapter(){ list = StyleManager.getBackgroundColors(); }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_color,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.MyViewHolder holder, int position) {

        BackgroundColor currentItem = list.get(position);

        holder.backgroundView.setBackgroundResource(currentItem.getDrawable());

        if (listener != null){
            holder.itemView.setOnClickListener(view -> listener.OnClickListener(position));

            // (UNUSED)
            holder.itemView.setOnLongClickListener(view -> { listener.OnLongClickListener(position); return true; });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout backgroundView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundView = itemView.findViewById(R.id.fragment_item_color);
        }
    }

    public interface OnItemClickListener{
        void OnClickListener(int position);

        // (UNUSED)
        void OnLongClickListener(int position);
    }

}
