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

/**
 * Display a dialog fragment box offering the user to make an action.
 * From one (1 : Cancel/Ok) up to three (3) buttons could be used and customized
 * to set the better options.
 * @see OnConfirmClickListener
 */
public class DialogConfirm extends AppCompatDialogFragment {

    private final int color;
    private final int icon;
    private final String text;
    private final int buttonNumbers;
    private final OnConfirmClickListener listener;

    /**
     * Allow the overriding of the second and third options.
     * @see DialogConfirm
     */
    public interface OnConfirmClickListener{

        /**
         * Primary action for the confirm dialog
         * @see DialogConfirm
         */
        void OnPrimaryAction();

        /**
         * Secondary action for the confirm dialog
         */
        void OnSecondaryAction();
    }

    /**
     * Public Constructor (3 Buttons)
     * @param color app theme
     * @param icon id of the icon to be displayed , set to -1 for none
     * @param text text to be displayed
     * @param listener override action for button 1 and 2
     */
    public DialogConfirm(int color, int icon, String text, OnConfirmClickListener listener) {
        this.color = color;
        this.icon = icon;
        this.text = text;
        buttonNumbers = 2;
        this.listener = listener;
    }

    /**
     * Public Constructor (Custom button number)
     * @param color app theme
     * @param icon id of the icon to be displayed, -1 for none
     * @param text text to be displayed
     * @param buttonNumbers number of buttons, 1, 2 or 3
     * @param listener override the action of the buttons
     */
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
            icon.setBackgroundResource(Style.getBackground(getContext(),color));
        }
        else
            icon.setVisibility(View.GONE);

        dialog.findViewById(R.id.confirmation_dialog_background).setBackgroundColor(
                getResources().getColor(Style.getNeutralColor(getContext()))
        );

        TextView text = dialog.findViewById(R.id.dialog_text);
        text.setText(this.text);
        text.setTextColor(getResources().getColor(Style.getColorPrimaryAccent(getContext(), color)));

        Button cancel = dialog.findViewById(R.id.dialog_cancel);
        cancel.setBackgroundResource(Style.getBackground(getContext(),color));
        cancel.setTextColor(getResources().getColor(R.color.white));
        cancel.setOnClickListener(view -> dismiss());

        Button confirm = dialog.findViewById(R.id.dialog_confirm);
        confirm.setBackgroundResource(Style.getBackground(getContext(),color));
        confirm.setTextColor(getResources().getColor(R.color.white));
        confirm.setOnClickListener(view -> {
            listener.OnPrimaryAction();
            dismiss();
        });

        Button secondaryAction = dialog.findViewById(R.id.dialog_secondary_action);
        secondaryAction.setBackgroundResource(Style.getBackground(getContext(),color));
        secondaryAction.setTextColor(getResources().getColor(R.color.white));
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
