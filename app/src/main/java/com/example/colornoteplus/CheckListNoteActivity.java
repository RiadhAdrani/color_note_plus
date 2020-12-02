package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class CheckListNoteActivity extends AppCompatActivity{

    // note editable views
    private EditText titleView;
    private TextView titleCharacterCount;
    private ImageButton colorView;
    private ArrayList<CheckListItem> content;
    private RecyclerView contentView;
    private FloatingActionButton fab;

    CheckListAdapter adapter;

    // toolbar
    private Toolbar toolbar;

    // Current note
    private CheckListNote note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<CheckListItem> dummyList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            dummyList.add(new CheckListItem(UUID.randomUUID().toString()));
        }

        content = dummyList;

        getNoteFromIntent();

        changeViewsColor(0);
    }

    // Method used to add menus and configure button action
    // like OnClickListeners ...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check_list_activity,menu);

        return super.onCreateOptionsMenu(menu);
    }

    // get note from the intent
    private void getNoteFromIntent(){

        if (!getIntent().getStringExtra(Statics.KEY_NOTE_ACTIVITY).equals(Statics.NOTE_DEFAULT_UID)){
            note = MySharedPreferences.LoadCheckListNoteFromSharedPreferences(getIntent().getStringExtra(Statics.KEY_NOTE_ACTIVITY),getApplicationContext());
        } else {
            note = new CheckListNote();
        }
    }

    private void changeViewsColor(int color){

        // set the global theme
        setTheme(StyleManager.getTheme(color));

        // set the appropriate layout
        setContentView(R.layout.activity_check_list_note);

        // change status bar color
        getWindow().setStatusBarColor(getResources().getColor(StyleManager.getThemeColor(color)));

        // set up FAB action
        fab = findViewById(R.id.fab_add_item);
        fab.setOnClickListener(view -> onFabClickListener());

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
        colorView.setOnClickListener(view -> buildColorPickDialog());
        colorView.setBackgroundResource(StyleManager.getBackground(color));

        adapter = new CheckListAdapter(getApplicationContext(),note.getContent(),color);
        adapter.setOnItemClickListener(new CheckListAdapter.OnItemClickListener() {
            @Override
            public void onChecked(int position) {

            }

            @Override
            public void onUnchecked(int position) {

            }

            @Override
            public void onSetPriority(int position) {

            }

            @Override
            public void onSetReminder(int position) {

            }

            @Override
            public void onDelete(int position) {
                note.getContent().remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        contentView = findViewById(R.id.note_content_view);
        contentView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contentView.setAdapter(adapter);

    }

    private void switchColor(int color){

        String tempTitle = titleView.getText().toString().trim();
        changeViewsColor(color);
        titleView.setText(tempTitle);

    }

    // build the color picker dialog
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,note.getColor());

        fragment.show(getSupportFragmentManager(),Statics.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                note.setColor(position);
                switchColor(position);
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

    private void onFabClickListener(){
        FragmentAddCheckListItem fragment = new FragmentAddCheckListItem(note.getColor());
        fragment.show(getSupportFragmentManager(),Statics.TAG_FRAGMENT_ADD_CHECK_LIST_ITEM);
        fragment.setOnClickListener(new FragmentAddCheckListItem.OnClickListener() {
            @Override
            public void onConfirmClickListener() {
                fragment.getItem().setDescription(fragment.getInputText());
                note.getContent().add(0,fragment.getItem());
                adapter.notifyItemInserted(0);
                fragment.dismiss();
            }

            @Override
            public void onSetPriorityClickListener() {

            }

            @Override
            public void onSetDueTimeClickListener() {

            }
        });
    }
}