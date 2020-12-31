package com.example.colornoteplus;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

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

        if (item.getClass() == NoteText.class){
            holder.contentView.setText((String) item.getContent());}

        if (item.getClass() == NoteCheckList.class){
            String content = "";
            for (CheckListItem i: ((NoteCheckList) item).getContent()) {
                content += "• " + i.getDescription()+"\n";
            }
            holder.contentView.setText(content);
        }

        holder.backgroundView.setBackgroundResource( Style.getBackground(getContext(), item.getColor()) );

        holder.itemView.setOnClickListener(view -> {
            if (getListener() != null) getListener().OnClickListener(holder.getAdapterPosition());
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (getListener() != null) getListener().OnLongClickListener(holder.getAdapterPosition());
            return true;
        });

        holder.moreOptionsView.setOnClickListener(view -> {

            PopupMenu menu = new PopupMenu(getContext(),holder.moreOptionsView);
            menu.setOnMenuItemClickListener(menuItem -> {

                final int restore = R.id.item_restore;
                final int delete = R.id.item_delete_permanently;

                switch (menuItem.getItemId()){
                    case restore: if (getListener() != null) getListener().OnOptionOneClick(holder.getAdapterPosition()); return true;
                    case delete: if (getListener() != null) getListener().OnOptionTwoClick(holder.getAdapterPosition()); return true;
                }

                return false;
            });

            menu.inflate(R.menu.menu_recycler_bin);
            menu.show();
        });

        if (getSelectionMode() == SelectionMode.NO_SELECTION){
            holder.selectionIcon.setVisibility(View.GONE);
            holder.moreOptionsView.setVisibility(View.VISIBLE);
        } else {
            holder.selectionIcon.setVisibility(View.VISIBLE);
            holder.moreOptionsView.setVisibility(View.INVISIBLE);
            if (isSelected(item.getUid())){
                holder.selectionIcon.setImageResource(R.drawable.ic_checked_circle);
                switch(Style.getLightTheme(getContext())){
                    case 0:
                        holder.backgroundView.setBackgroundResource(
                                Style.getBackgroundPrimary(getContext(),item.getColor()) );
                        break;
                    case 1:
                        holder.backgroundView.setBackgroundResource(
                                Style.getBackgroundSecondary(getContext(),item.getColor()) );
                        break;
                }
            }
            else {
                holder.selectionIcon.setImageResource(R.drawable.ic_unchecked_circle);
                holder.backgroundView.setBackgroundResource( Style.getBackground(getContext(), item.getColor()));
            }
        }

    }

}