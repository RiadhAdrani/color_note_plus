package com.example.colornoteplus;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextNote note = new TextNote("MyNote",1);

        // set Style
        setTheme(StyleManager.getTheme(note.getColor()));

        setContentView(R.layout.note_activity);

        if (getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_YES){
            Toast.makeText(this,"Dark Mode",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this,"Light Mode",Toast.LENGTH_SHORT).show();
        }
        
        toolbar = findViewById(R.id.toolbar);
        Log.d("NOTE_ACTIVITY_DEBUG","Color ID: " + StyleManager.getThemeColor(note.getColor()));
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView = findViewById(R.id.note_title_view);
        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(note.getColor())));

        colorView = findViewById(R.id.note_color_view);

        contentView = findViewById(R.id.note_content_view);
        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(note.getColor())));

        // For Debugging Purpose
        Log.d("NOTE_ACTIVITY_DEBUG","Note Name: "+note.getTitle());
        Log.d("NOTE_ACTIVITY_DEBUG","Note UID: "+note.getUid());
        Log.d("NOTE_ACTIVITY_DEBUG","Note Color: "+note.getColor());

        titleView.setText(note.getTitle());
        colorView.setBackgroundResource(StyleManager.getBackground(note.getColor()));
        contentView.setText(note.getContent());
    }

    private TextNote getNote(String uid){
        if (uid.equals(CONST.NOTE_DEFAULT_UID)) return new TextNote();
        else return MySharedPreferences.LoadTextNoteFromSharedPreferences(uid,getApplicationContext());
    }

    private void setActivityColor(){

    }
}