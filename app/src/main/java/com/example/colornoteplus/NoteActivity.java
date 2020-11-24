package com.example.colornoteplus;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private boolean isLightUI;

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    // Literally Main()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        note = new TextNote("MyNote",0);

        setTheme(StyleManager.getTheme(note.getColor()));

        setContentView(R.layout.note_activity);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        titleView = findViewById(R.id.note_title_view);
        contentView = findViewById(R.id.note_content_view);
        colorView = findViewById(R.id.note_color_view);

        isLightUI = true;

        // TODO: get UI mode from Shared Preferences
        // the user can also manually switch the UI mode

        setActivityColorTheme();

        titleView.setText(note.getTitle());
        colorView.setBackgroundResource(StyleManager.getBackground(note.getColor()));
        contentView.setText(note.getContent());
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
        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // TODO: save note to Shared Preferences
                // TODO: add note to note list

                note.save(getApplicationContext());

                ArrayList<String> noteList =
                        MySharedPreferences.LoadStringArrayToSharedPreferences(
                                CONST.KEY_NOTE_LIST,getApplicationContext()
                        );

                noteList.add(note.getUid());
                MySharedPreferences.SaveStringArrayToSharedPreferences(
                        noteList,CONST.KEY_NOTE_LIST,getApplicationContext()
                );

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    private TextNote getNote(String uid){
        if (uid.equals(CONST.NOTE_DEFAULT_UID)) return new TextNote();
        else return MySharedPreferences.LoadTextNoteFromSharedPreferences(uid,getApplicationContext());
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    // Method used to automatically configure color theme of the activity
    private void setActivityColorTheme(){
        // set Style

        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(note.getColor())));

        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(note.getColor())));

    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

}