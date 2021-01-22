package com.example.colornoteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

/**
 * Allow the displaying of a loading dialog fragment.
 */
public class LoadingFragment extends AppCompatDialogFragment {

    /**
     * Allow the addition of action after loading is completed.
     */
    interface OnLoadingCompleted{

        /**
         * Post loading actions
         */
        void onCompletion();

    }

    private final String text;

    private final OnLoadingCompleted onLoadingCompleted;

    /**
     * public constructor
     */
    public LoadingFragment(String text, OnLoadingCompleted onLoadingCompleted){
        this.text = text;
        this.onLoadingCompleted = onLoadingCompleted;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialog = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_loading,null);

        builder.setView(dialog);

        TextView textView = dialog.findViewById(R.id.text);
        textView.setText(text);

        if (onLoadingCompleted != null) onLoadingCompleted.onCompletion();

        return builder.create();
    }
}
