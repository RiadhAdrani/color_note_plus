package com.example.colornoteplus;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // recycler view and its adapter
    RecyclerView rv;
    NoteAdapter adapter;

    // note list
    ArrayList<Note<?>> noteList = new ArrayList<>();

    // floating action buttons
    ExtendedFloatingActionButton mainFAB;
    FloatingActionButton textFAB;
    FloatingActionButton checkListFAB;

    // toolbar
    Toolbar toolbar;

    // status variables
    Boolean areFABsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(CONST.DEFAULT_TOOLBAR_COLOR));

        // load note list
        ArrayList<Note<?>> dummyList = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            dummyList.add(new TextNote(UUID.randomUUID().toString(),
                    (int) (Math.random() * StyleManager.getBackgroundColors().size())
            ));
        }

        Log.d("DEBUG_NOTE_LIST","Note List Size = " + MySharedPreferences.LoadStringArrayToSharedPreferences(CONST.KEY_NOTE_LIST,this).size());

        for (String s : MySharedPreferences.LoadStringArrayToSharedPreferences(CONST.KEY_NOTE_LIST,this)) {
            noteList.add(MySharedPreferences.LoadTextNoteFromSharedPreferences(s,this));
        }

        // initializing the recycler view and its adapter
        // and displaying the list of the notes
        adapter = new NoteAdapter(noteList);
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                noteOnClickListener(position);
            }

            @Override
            public void OnLongClickListener(int position) {
                Toast.makeText(getApplicationContext(),"Item "+ position + " Held",Toast.LENGTH_SHORT).show();
            }
        });

        rv = findViewById(R.id.note_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // initializing FABs
        mainFAB = findViewById(R.id.fab_main);
        mainFAB.shrink();

        textFAB = findViewById(R.id.fab_text);
        textFAB.hide();
        textFAB.setOnClickListener(view -> textFABOnClickListener());

        checkListFAB = findViewById(R.id.fab_checklist);
        checkListFAB.hide();
        checkListFAB.setOnClickListener(view -> checkListFABOnClickListener());

        areFABsVisible = false;

        // main FAB onClickListener
        mainFAB.setOnClickListener(view -> mainFABHandler());
    }

    private void mainFABHandler(){
        if (!areFABsVisible){

            textFAB.show();
            checkListFAB.show();

            mainFAB.extend();

            areFABsVisible = true;
        }
        else {
            textFAB.hide();
            checkListFAB.hide();

            mainFAB.shrink();

            areFABsVisible = false;
        }
    }

    private void textFABOnClickListener(){
        Intent i = new Intent(this,NoteActivity.class);
        i.putExtra(CONST.KEY_NOTE_ACTIVITY,CONST.NOTE_DEFAULT_UID);
        startActivity(i);
    }

    private void checkListFABOnClickListener(){
        TextNote dumpNote = new TextNote("Testing", 0);
        noteList.add(dumpNote);
        adapter.notifyItemInserted(noteList.size()-1);
        MySharedPreferences.SaveTextNoteToSharedPreferences( dumpNote,this);
    }

    private void noteOnClickListener(int position){

        Intent i;

        if (TextNote.class.equals(noteList.get(position).getClass())) {

            i = new Intent(getApplicationContext(),NoteActivity.class);
            i.putExtra(CONST.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
            startActivity(i);
        }
    }
}