package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // recycler view and its adapter
    RecyclerView recyclerView;
    NoteAdapter adapter;

    // floating action buttons
    ExtendedFloatingActionButton mainFAB;
    FloatingActionButton textFAB;
    FloatingActionButton checkListFAB;

    // status variables
    Boolean areFABsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Note<?>> dummyList = new ArrayList<>();
        dummyList.add(new TextNote(UUID.randomUUID().toString(),0));
        dummyList.add(new TextNote(UUID.randomUUID().toString(),0));
        dummyList.add(new TextNote(UUID.randomUUID().toString(),0));
        dummyList.add(new TextNote(UUID.randomUUID().toString(),0));
        dummyList.add(new TextNote(UUID.randomUUID().toString(),0));
        dummyList.add(new TextNote(UUID.randomUUID().toString(),0));

        // initializing the recycler view and its adapter
        // and displaying the list of the notes
        NoteAdapter adapter = new NoteAdapter(dummyList);
        RecyclerView rv = findViewById(R.id.note_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // initializing FABs
        mainFAB = findViewById(R.id.fab_main);
        mainFAB.shrink();

        textFAB = findViewById(R.id.fab_text);
        textFAB.hide();

        checkListFAB = findViewById(R.id.fab_checklist);
        checkListFAB.hide();

        areFABsVisible = false;

        // main FAB onClickListener
        mainFAB.setOnClickListener(view -> {
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
        });
    }
}