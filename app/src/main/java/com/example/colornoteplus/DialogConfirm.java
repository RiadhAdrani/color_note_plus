package com.example.colornoteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogConfirm extends AppCompatDialogFragment {

    private final int color;
    private final Context context;
    private final int icon;
    private final String text;
    private final OnConfirmClickListener listener;

    public interface OnConfirmClickListener{
        void OnConfirm();
    }

    public DialogConfirm(Context context, int color, int icon, String text, OnConfirmClickListener listener) {
        this.context = context;
        this.color = color;
        this.icon = icon;
        this.text = text;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm_action,null);

        builder.setView(dialog);

        ImageView icon = dialog.findViewById(R.id.dialog_icon);
        if (this.icon > 0) {
            icon.setImageResource(this.icon);
            icon.setBackgroundResource(StyleManager.getBackground(color));
        }
        else
            icon.setVisibility(View.GONE);

        TextView text = dialog.findViewById(R.id.dialog_text);
        text.setText(this.text);
        text.setTextColor(getResources().getColor(StyleManager.getColorPrimaryAccent(color)));

        Button cancel = dialog.findViewById(R.id.dialog_cancel);
        cancel.setBackgroundResource(StyleManager.getBackground(color));
        cancel.setTextColor(getResources().getColor(StyleManager.getColorSecondaryAccent(color)));
        cancel.setOnClickListener(view -> dismiss());

        Button confirm = dialog.findViewById(R.id.dialog_confirm);
        confirm.setBackgroundResource(StyleManager.getBackground(color));
        confirm.setTextColor(getResources().getColor(StyleManager.getColorSecondaryAccent(color)));
        confirm.setOnClickListener(view -> {
            listener.OnConfirm();
            dismiss();
        });

        return builder.create();
    }

}
