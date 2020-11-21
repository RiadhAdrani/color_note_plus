package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

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
        setContentView(R.layout.note_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView = findViewById(R.id.note_title_view);
        colorView = findViewById(R.id.note_color_view);
        contentView = findViewById(R.id.note_content_view);

        TextNote note = getNote(getIntent().getStringExtra(CONST.KEY_NOTE_ACTIVITY));

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


}