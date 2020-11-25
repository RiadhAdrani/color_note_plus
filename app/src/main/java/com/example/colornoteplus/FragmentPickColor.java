package com.example.colornoteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentPickColor extends AppCompatDialogFragment {

    ColorAdapter adapter;
    int span;
    int color;

    public FragmentPickColor(ColorAdapter adapter, int span,int color){
        this.adapter = adapter;
        this.span = span;
        this.color = color;
    }

    public void setOnItemClickListener(ColorAdapter.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialog = getActivity().getLayoutInflater().inflate(R.layout.fragment_color_pick,null);

        builder.setView(dialog);

        Button cancel = dialog.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(view -> dismiss());
        cancel.setBackgroundResource(StyleManager.getBackground(color));

        RecyclerView rv = dialog.findViewById(R.id.color_recyclerView);
        rv.setLayoutManager(new GridLayoutManager(getContext(),span));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        return builder.create();
    }
}
