package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class CheckListNoteActivity extends AppCompatActivity{

    // note editable views
    private EditText titleView;
    private TextView titleCharacterCount;
    private ImageButton colorView;
    private ArrayList<CheckListNote> content;
    private RecyclerView contentView;
    private ImageButton addItem;
    private ImageButton priority;
    private ImageButton dueTime;

    // toolbar
    private Toolbar toolbar;

    // Current note
    private CheckListNote note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeViewsColor(3);
    }

    private void changeViewsColor(int color){

        // set the global theme
        setTheme(StyleManager.getTheme(color));

        // set the appropriate layout
        setContentView(R.layout.activity_check_list_note);

        // setting the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(color)));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // setting the note title
        titleView = findViewById(R.id.note_title_view);
        titleView.setText("");
        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(color)));
        titleView.setHintTextColor(getResources().getColor(StyleManager.getThemeColorLight(color)));

        // setting the character counter for the note title
        titleCharacterCount = findViewById(R.id.note_title_characters);
        String m = titleView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
        titleCharacterCount.setText(m);
        titleCharacterCount.setTextColor(getResources().getColor(StyleManager.getThemeColor(color)));

        titleView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                String msg = titleView.getText().toString().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
                titleCharacterCount.setText(msg);
            }
        });

        // setting the color view
        colorView = findViewById(R.id.note_color_view);
        // colorView.setOnClickListener(view -> buildColorPickDialog());
        colorView.setBackgroundResource(StyleManager.getBackground(color));

        // setting the new item menu
        ConstraintLayout layout = findViewById(R.id.note_add_check_list);
        layout.setBackgroundColor(getResources().getColor(StyleManager.getThemeColorLighter(color)));
        layout.findViewById(R.id.item_check_box).setVisibility(View.GONE);
        layout.findViewById(R.id.item_delete).setVisibility(View.GONE);
        layout.findViewById(R.id.item_priority).setBackgroundResource(StyleManager.getBackground(color));
        layout.findViewById(R.id.item_due_time).setBackgroundResource(StyleManager.getBackground(color));
        EditText title = layout.findViewById(R.id.item_title);
        title.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(color)));
        title.setHintTextColor(getResources().getColor(StyleManager.getThemeColorLight(color)));
        layout.findViewById(R.id.item_add).setBackgroundResource(StyleManager.getBackground(color));

        contentView = findViewById(R.id.note_content_view);

    }
}