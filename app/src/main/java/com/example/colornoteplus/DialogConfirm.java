package com.example.colornoteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class DialogConfirm extends AppCompatDialogFragment {

    private final int color;
    private final int icon;
    private final String text;
    private final int buttonNumbers;
    private final OnConfirmClickListener listener;

    public interface OnConfirmClickListener{
        void OnPrimaryAction();
        void OnSecondaryAction();
    }

    public DialogConfirm(int color, int icon, String text, OnConfirmClickListener listener) {
        this.color = color;
        this.icon = icon;
        this.text = text;
        buttonNumbers = 2;
        this.listener = listener;
    }

    public DialogConfirm(int color, int icon, String text, int buttonNumbers,OnConfirmClickListener listener) {
        this.color = color;
        this.icon = icon;
        this.text = text;
        this.buttonNumbers = buttonNumbers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialog = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.dialog_confirm_action,null);

        builder.setView(dialog);

        ImageView icon = dialog.findViewById(R.id.dialog_icon);
        if (this.icon > 0) {
            icon.setImageResource(this.icon);
            icon.setBackgroundResource(StyleManager.getBackground(getContext(),color));
        }
        else
            icon.setVisibility(View.GONE);

        dialog.findViewById(R.id.confirmation_dialog_background).setBackgroundColor(
                getResources().getColor(StyleManager.getNeutralColor(getContext()))
        );

        TextView text = dialog.findViewById(R.id.dialog_text);
        text.setText(this.text);
        text.setTextColor(getResources().getColor(StyleManager.getColorPrimaryAccent(getContext(), color)));

        Button cancel = dialog.findViewById(R.id.dialog_cancel);
        cancel.setBackgroundResource(StyleManager.getBackground(getContext(),color));
        cancel.setTextColor(getResources().getColor(StyleManager.getColorSecondaryAccent(getContext(),color)));
        cancel.setOnClickListener(view -> dismiss());

        Button confirm = dialog.findViewById(R.id.dialog_confirm);
        confirm.setBackgroundResource(StyleManager.getBackground(getContext(),color));
        confirm.setTextColor(getResources().getColor(StyleManager.getColorSecondaryAccent(getContext(),color)));
        confirm.setOnClickListener(view -> {
            listener.OnPrimaryAction();
            dismiss();
        });

        Button secondaryAction = dialog.findViewById(R.id.dialog_secondary_action);
        secondaryAction.setBackgroundResource(StyleManager.getBackground(getContext(),color));
        secondaryAction.setTextColor(getResources().getColor(StyleManager.getColorSecondaryAccent(getContext(),color)));
        secondaryAction.setOnClickListener(view -> {
            listener.OnSecondaryAction();
            dismiss();
        });

        if (buttonNumbers < 3) secondaryAction.setVisibility(View.GONE);
        if (buttonNumbers < 2) {
            confirm.setVisibility(View.GONE);
            cancel.setText(R.string.ok);
        }

        return builder.create();
    }

}
