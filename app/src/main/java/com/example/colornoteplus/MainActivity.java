package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        // MySharedPreferences.SaveStringArrayToSharedPreferences(new ArrayList<>(),Statics.KEY_NOTE_LIST,getApplicationContext());

        Log.d("DEBUG_NOTE_LIST","Note List Size = " + MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST,this).size());

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
        i.putExtra(Statics.KEY_NOTE_ACTIVITY, Statics.NOTE_DEFAULT_UID);
        startActivity(i);
    }

    private void checkListFABOnClickListener(){
        // Toast.makeText(this, "Feature Not available yet !", Toast.LENGTH_SHORT).show();
        Statics.StyleableToast(
                getApplicationContext(),
                "Feature not available yet !",
                R.color.grey_dark_sonic_silver,
                R.color.white,
                3,
                R.color.grey_dark_sonic_silver,
                true);
    }

    private void noteOnClickListener(int position){

        Intent i;

        if (TextNote.class.equals(noteList.get(position).getClass())) {

            i = new Intent(getApplicationContext(),NoteActivity.class);
            i.putExtra(Statics.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
            startActivity(i);
        }
    }

    private void deleteItem(int position){
        noteList.remove(position);
        adapter.notifyItemRemoved(position);
    }

}