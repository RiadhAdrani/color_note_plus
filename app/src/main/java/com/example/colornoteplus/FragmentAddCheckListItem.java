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

/**
 * Fragment allowing the user to add a checklist item in the check list note activity
 * @see CheckListNote
 * @see CheckListNoteActivity
 */
public class FragmentAddCheckListItem extends AppCompatDialogFragment {

    private final int color;
    private OnClickListener listener;
    private View dialog;
    private CheckListItem item;

    /**
     * Public constructor
     * @param color theme of the activity
     */
    public FragmentAddCheckListItem(int color){
        this.color = color;
    }

    /**
     * Override the onClick listener
     * @see OnClickListener
     * @param listener new listener
     */
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

        dialog.setBackgroundColor(getResources().getColor(Style.getNeutralColor(getContext())));

        Button cancel = dialog.findViewById(R.id.fragment_cancel);
        cancel.setOnClickListener(view -> dismiss());
        cancel.setBackgroundColor(getResources().getColor(Style.getColorMain(getContext(),color)));
        cancel.setTextColor(getResources().getColor(Style.getNeutralColor(getContext())));

        Button confirm = dialog.findViewById(R.id.fragment_add_item);
        confirm.setOnClickListener(view -> listener.onConfirmClickListener());
        confirm.setBackgroundColor(getResources().getColor(Style.getColorMain(getContext(),color)));
        confirm.setTextColor(getResources().getColor(Style.getNeutralColor(getContext())));

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
        setDueTime.setBackgroundResource(Style.getBackground(getContext(),color));
        setDueTime.setOnClickListener(view -> listener.onSetDueTimeClickListener());
        TextView dueTime = dialog.findViewById(R.id.fragment_item_due_time_text);

        dueTime.setTextColor((getResources().getColor(Style.getColorMain(getContext(),color))));
        dueTime.setHintTextColor((getResources().getColor(Style.getColorSecondary(getContext(),color))));
        dueTime.setText(getString(R.string.set_reminder));

        EditText text = dialog.findViewById(R.id.fragment_item_text);
        text.setText(R.string.new_item);
        text.setHint(R.string.new_item);
        text.setTextColor(getResources().getColor(Style.getColorPrimary(getContext(),color)));
        text.setHintTextColor(getResources().getColor(Style.getColorSecondary(getContext(),color)));

        priority.setVisibility(View.GONE);
        setDueTime.setVisibility(View.GONE);
        dueTime.setVisibility(View.GONE);

        return builder.create();
    }

    /**
     * Allow to customize the action made in the fragment
     * @see FragmentAddCheckListItem
     */
    public interface OnClickListener{

        void onConfirmClickListener();
        void onSetDueTimeClickListener();
    }

    /**
     * Change the due time in the fragment
     * @param text new text
     */
    public void setDueTimeText(String text){
        TextView view = dialog.findViewById(R.id.fragment_item_due_time_text);
        view.setText(text);
    }

    /**
     * Get the input text from the fragment
     * @return input text from the fragment
     */
    public String getInputText(){
        EditText view = dialog.findViewById(R.id.fragment_item_text);
        return view.getText().toString().trim();
    }

    /**
     * return the current item created in the fragment
     * @see CheckListItem
     * @return a check list item
     */
    public CheckListItem getItem(){
        return item;
    }
}
