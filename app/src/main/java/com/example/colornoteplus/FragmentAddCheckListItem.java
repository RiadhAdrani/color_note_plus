package com.example.colornoteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class FragmentAddCheckListItem extends AppCompatDialogFragment {

    private final int color;
    private OnClickListener listener;
    private View dialog;
    private CheckListItem item;

    public FragmentAddCheckListItem(int color){
        this.color = color;
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        item = new CheckListItem(getString(R.string.new_item),-1L, CheckListItem.PRIORITY.MEDIUM);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        dialog = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_new_check_list_item,null);

        builder.setView(dialog);

        dialog.setBackgroundColor(getResources().getColor(StyleManager.getNeutralColor(getContext())));

        Button cancel = dialog.findViewById(R.id.fragment_cancel);
        cancel.setOnClickListener(view -> dismiss());
        cancel.setBackgroundColor(getResources().getColor(StyleManager.getColorMain(getContext(),color)));
        cancel.setTextColor(getResources().getColor(StyleManager.getNeutralColor(getContext())));

        Button confirm = dialog.findViewById(R.id.fragment_add_item);
        confirm.setOnClickListener(view -> listener.onConfirmClickListener());
        confirm.setBackgroundColor(getResources().getColor(StyleManager.getColorMain(getContext(),color)));
        confirm.setTextColor(getResources().getColor(StyleManager.getNeutralColor(getContext())));

        Spinner priority = dialog.findViewById(R.id.fragment_item_priority);
        PriorityAdapter adapter= new PriorityAdapter(getContext(), CheckListItem.PRIORITY.values(),color);
        priority.setAdapter(adapter);
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item.setPriority(CheckListItem.PRIORITY.values()[i]);
            }

            // (UNUSED)
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageButton setDueTime = dialog.findViewById(R.id.fragment_item_due_time_button);
        setDueTime.setBackgroundResource(StyleManager.getBackground(getContext(),color));
        setDueTime.setOnClickListener(view -> listener.onSetDueTimeClickListener());
        TextView dueTime = dialog.findViewById(R.id.fragment_item_due_time_text);

        dueTime.setTextColor((getResources().getColor(StyleManager.getColorMain(getContext(),color))));
        dueTime.setHintTextColor((getResources().getColor(StyleManager.getColorSecondary(getContext(),color))));
        dueTime.setText(getString(R.string.set_reminder));

        EditText text = dialog.findViewById(R.id.fragment_item_text);
        text.setText(R.string.new_item);
        text.setHint(R.string.new_item);
        text.setTextColor(getResources().getColor(StyleManager.getColorPrimary(getContext(),color)));
        text.setHintTextColor(getResources().getColor(StyleManager.getColorSecondary(getContext(),color)));

        priority.setVisibility(View.GONE);
        setDueTime.setVisibility(View.GONE);
        dueTime.setVisibility(View.GONE);

        return builder.create();
    }

    public interface OnClickListener{
        void onConfirmClickListener();
        void onSetDueTimeClickListener();
    }

    public void setDueTimeText(String text){
        TextView view = dialog.findViewById(R.id.fragment_item_due_time_text);
        view.setText(text);
    }

    public String getInputText(){
        EditText view = dialog.findViewById(R.id.fragment_item_text);
        return view.getText().toString().trim();
    }

    public CheckListItem getItem(){
        return item;
    }
}
