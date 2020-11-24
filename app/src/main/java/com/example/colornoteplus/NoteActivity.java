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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        note = new TextNote("MyNote",5);

        isLightUI = true;

        // TODO: get UI mode from Shared Preferences
        // the user can also manually switch the UI mode

        setActivityColorTheme();

        // For Debugging Purpose
        Log.d("NOTE_ACTIVITY_DEBUG","Note Name: "+note.getTitle());
        Log.d("NOTE_ACTIVITY_DEBUG","Note UID: "+note.getUid());
        Log.d("NOTE_ACTIVITY_DEBUG","Note Color: "+note.getColor());

        titleView.setText(note.getTitle());
        colorView.setBackgroundResource(StyleManager.getBackground(note.getColor()));
        contentView.setText(note.getContent());
    }

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

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private TextNote getNote(String uid){
        if (uid.equals(CONST.NOTE_DEFAULT_UID)) return new TextNote();
        else return MySharedPreferences.LoadTextNoteFromSharedPreferences(uid,getApplicationContext());
    }

    private void setActivityColorTheme(){
        // set Style
        setTheme(StyleManager.getTheme(note.getColor()));

        setContentView(R.layout.note_activity);
        toolbar = findViewById(R.id.toolbar);
        titleView = findViewById(R.id.note_title_view);
        contentView = findViewById(R.id.note_content_view);
        colorView = findViewById(R.id.note_color_view);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(note.getColor())));

        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(note.getColor())));

    }


}