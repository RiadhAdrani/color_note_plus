package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class NoteActivity extends AppCompatActivity {

    private EditText titleView;
    private ImageButton colorView;
    private EditText contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        titleView = findViewById(R.id.note_title_view);
        colorView = findViewById(R.id.note_color_view);
        contentView = findViewById(R.id.note_content_view);

        TextNote note = new TextNote("New Note",getResources().getColor(R.color.purple_200));

        titleView.setText(note.getTitle());
        colorView.setBackgroundColor(note.getColor());
        contentView.setText(note.getContent());
    }
}