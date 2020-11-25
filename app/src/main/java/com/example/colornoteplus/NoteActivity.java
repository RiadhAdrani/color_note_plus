package com.example.colornoteplus;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    // note editable views
    private EditText titleView;
    private ImageButton colorView;
    private EditText contentView;

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

            Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView = findViewById(R.id.note_title_view);
        titleView.setText(note.getTitle());
        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(note.getColor())));

        contentView = findViewById(R.id.note_content_view);
        contentView.setText(note.getContent());
        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(note.getColor())));

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

    private void switchTheme(int id){

        String titleTemp = titleView.getText().toString().trim();
        String textTemp = contentView.getText().toString().trim();

        // change theme
        setTheme(StyleManager.getTheme(id));
        setContentView(R.layout.note_activity);
        getWindow().setStatusBarColor(getResources().getColor(StyleManager.getThemeColor(id)));

        // change toolbar theme
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(id)));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView = findViewById(R.id.note_title_view);
        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(id)));
        titleView.setText(titleTemp);

        contentView = findViewById(R.id.note_content_view);
        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(id)));
        contentView.setText(textTemp);

        colorView = findViewById(R.id.note_color_view);
        colorView.setBackgroundResource(StyleManager.getBackground(id));
        colorView.setOnClickListener(view -> buildColorPickDialog());

    }

}