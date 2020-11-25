package com.example.colornoteplus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    // note editable views
    private EditText titleView;
    private TextView titleCharacterCount;
    private ImageButton colorView;
    private EditText contentView;
    private TextView contentCharacterCount;

    // toolbar
    private Toolbar toolbar;

    // Current note
    private TextNote note;

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    // Literally Main()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // note = new TextNote("MyNote",5);

        if (!getIntent().getStringExtra(Statics.KEY_NOTE_ACTIVITY).equals(Statics.NOTE_DEFAULT_UID)){
            note = MySharedPreferences.LoadTextNoteFromSharedPreferences(getIntent().getStringExtra(Statics.KEY_NOTE_ACTIVITY),getApplicationContext());
        } else {
            note = new TextNote();
        }

        initTheme();
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // Method used to add menus and configure button action
    // like OnClickListeners ...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_note_activity_toolbar,menu);

        MenuItem saveButton = menu.findItem(R.id.save);
        saveButton.setOnMenuItemClickListener(menuItem -> {

            // TODO: save note to Shared Preferences
            // TODO: add note to note list

            if (titleView.getText().toString().trim().length() < Statics.NOTE_TITLE_MINIMUM_LENGTH){

                Statics.StyleableToast(getApplicationContext(),
                        getString(R.string.title_short),
                        StyleManager.getThemeColorDark(note.getColor()),
                        R.color.white,
                        3,
                        StyleManager.getThemeColorDark(note.getColor()),
                        true);
            }
            else {

                note.setTitle(titleView.getText().toString().trim());
                note.setContent(contentView.getText().toString().trim());
                note.save(getApplicationContext());

                if (!isNoteOld()){

                    Log.d("DEBUG_SAVE","Note is old !");

                    ArrayList<String> noteList =
                            MySharedPreferences.LoadStringArrayToSharedPreferences(
                                    Statics.KEY_NOTE_LIST,getApplicationContext()
                            );

                    noteList.add(note.getUid());
                    MySharedPreferences.SaveStringArrayToSharedPreferences(
                            noteList, Statics.KEY_NOTE_LIST,getApplicationContext()
                    );
                }

                Statics.StyleableToast(getApplicationContext(),
                        getString(R.string.save_success),
                        StyleManager.getThemeColorDark(note.getColor()),
                        R.color.white,
                        3,
                        StyleManager.getThemeColorDark(note.getColor()),
                        false);
            }

            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    // Method used to automatically configure color theme of the activity
    private void initTheme(){

        setTheme(StyleManager.getTheme(note.getColor()));

        setContentView(R.layout.note_activity);

        changeViewsColor(note.getColor());
    }

    private void switchTheme(int id){

        String titleTemp = titleView.getText().toString().trim();
        String textTemp = contentView.getText().toString().trim();

        // change theme
        setTheme(StyleManager.getTheme(id));
        setContentView(R.layout.note_activity);
        getWindow().setStatusBarColor(getResources().getColor(StyleManager.getThemeColor(id)));

        changeViewsColor(id);

        titleView.setText(titleTemp);
        contentView.setText(textTemp);

    }

    private void changeViewsColor(int i){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView = findViewById(R.id.note_title_view);
        titleView.setText(note.getTitle());
        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(note.getColor())));
        titleView.setHintTextColor(getResources().getColor(StyleManager.getThemeColorLight(note.getColor())));

        titleCharacterCount = findViewById(R.id.note_title_characters);
        String m = titleView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
        titleCharacterCount.setText(m);
        titleCharacterCount.setTextColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));

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

        contentView = findViewById(R.id.note_content_view);
        contentView.setText(note.getContent());
        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(note.getColor())));
        contentView.setHintTextColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        contentView.setScroller(new Scroller(getApplicationContext()));
        contentView.setVerticalScrollBarEnabled(true);
        contentView.setMovementMethod(new ScrollingMovementMethod());

        contentCharacterCount = findViewById(R.id.note_content_characters);
        contentCharacterCount.setTextColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        m = contentView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.content_max_length);
        contentCharacterCount.setText(m);

        contentView.addTextChangedListener(new TextWatcher()
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
                String msg = contentView.getText().toString().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.content_max_length);
                contentCharacterCount.setText(msg);
            }
        });

        colorView = findViewById(R.id.note_color_view);
        colorView.setOnClickListener(view -> buildColorPickDialog());
        colorView.setBackgroundResource(StyleManager.getBackground(note.getColor()));

    }

    private boolean isNoteOld(){
        for (String n: MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST,getApplicationContext())){
            if (n.equals(note.getUid())) return true;
        }
        return false;
    }

    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,note.getColor());

        fragment.show(getSupportFragmentManager(),Statics.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                note.setColor(position);
                switchTheme(position);
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

}