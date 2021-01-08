package com.example.colornoteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Pick a color from the available colors in the app
 * @see Style
 * @see MainActivity
 * @see TextNoteActivity
 * @see CheckListNoteActivity
 * @see SettingsActivity
 */
public class FragmentPickColor extends AppCompatDialogFragment {

    ColorAdapter adapter;
    int span;
    int color;

    /**
     * Public constructor
     * @param adapter adapter displaying the color
     * @param span number of colors per row
     * @param color the current theme of the activity/app
     * @see ColorAdapter
     */
    public FragmentPickColor(ColorAdapter adapter, int span,int color){
        this.adapter = adapter;
        this.span = span;
        this.color = color;
    }

    /**
     * overriding the listener that handle clicks on elements
     * @param listener new listener
     * @see ColorAdapter
     * @see ColorAdapter.OnItemClickListener
     */
    public void setOnItemClickListener(ColorAdapter.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialog = getActivity().getLayoutInflater().inflate(R.layout.fragment_color_pick,null);

        builder.setView(dialog);

        dialog.findViewById(R.id.fragment_pick_color_background).setBackgroundColor(
                getResources().getColor(Style.getNeutralColor(getContext()))
        );

        TextView text = dialog.findViewById(R.id.prompt_text);
        text.setTextColor(getResources().getColor(Style.getNeutralTextColor(getContext())));

        Button cancel = dialog.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(view -> dismiss());
        cancel.setBackgroundResource(Style.getBackground(getContext(),color));

        RecyclerView rv = dialog.findViewById(R.id.color_recyclerView);
        rv.setLayoutManager(new GridLayoutManager(getContext(),span));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        return builder.create();
    }
}
