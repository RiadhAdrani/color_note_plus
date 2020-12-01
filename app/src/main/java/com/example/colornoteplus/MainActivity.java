package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

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
        toolbar.setBackgroundColor(getResources().getColor(Statics.DEFAULT_TOOLBAR_COLOR));

        for (String s : MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST,this)) {
            noteList.add(MySharedPreferences.LoadTextNoteFromSharedPreferences(s,this));
        }

        // initializing the recycler view and its adapter
        // and displaying the list of the notes
        adapter = new NoteAdapter(noteList,getApplicationContext());

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                noteOnClickListener(position);
            }

            @Override
            public void OnLongClickListener(int position) {
                Toast.makeText(getApplicationContext(),"Item "+ position + " Held",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnDeleteClickListener(int position) {
                deleteItem(position);
            }

            @Override
            public void OnColorSwitchClickListener(int position) {
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

    // action to be executed
    // when the activity is paused or suspended
    @Override
    protected void onPause() {
        super.onPause();

        saveNoteList();

    }

    // handle the main FAB
    // and the animations
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

    // create and open a note
    // in a new activity
    private void textFABOnClickListener(){
        Intent i = new Intent(this,NoteActivity.class);
        i.putExtra(Statics.KEY_NOTE_ACTIVITY, Statics.NOTE_DEFAULT_UID);
        startActivity(i);
    }

    // create and open a check list
    // in a new activity
    private void checkListFABOnClickListener(){
        Intent i = new Intent(this,CheckListNoteActivity.class);
        i.putExtra(Statics.KEY_NOTE_ACTIVITY, Statics.NOTE_DEFAULT_UID);
        startActivity(i);
    }

    // open the note activity
    // of a note at a given position
    // in note list
    private void noteOnClickListener(int position){

        Intent i;

        if (TextNote.class.equals(noteList.get(position).getClass())) {

            i = new Intent(getApplicationContext(),NoteActivity.class);
            i.putExtra(Statics.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
            startActivity(i);
        }
    }

    // delete a note at a given position
    // in the note list
    private void deleteItem(int position){

        ArrayList<String> n = MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST_TRASH,getApplicationContext());
        n.add(noteList.get(position).getUid());
        MySharedPreferences.SaveStringArrayToSharedPreferences(n,Statics.KEY_NOTE_LIST_TRASH,getApplicationContext());

        noteList.remove(position);
        adapter.notifyItemRemoved(position);
    }


    // collect the UIDs of the notes
    // and save them to the shared preferences
    private void saveNoteList(){

        ArrayList<String> mNoteList = new ArrayList<>();

        for (Note<?> note : noteList) {
            mNoteList.add(note.getUid());
        }

        MySharedPreferences.SaveStringArrayToSharedPreferences(mNoteList,Statics.KEY_NOTE_LIST,getApplicationContext());

    }

}