package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    enum STATES {
        NOTES, DELETED_NOTES
    }

    STATES state;

    // recycler view and its adapter
    RecyclerView rv;
    NoteAdapter adapter;

    // navigation drawer
    private DrawerLayout drawer;

    // note list
    ArrayList<Note<?>> noteList = new ArrayList<>();

    // floating action buttons
    ExtendedFloatingActionButton mainFAB;
    FloatingActionButton textFAB;
    FloatingActionButton checkListFAB;

    // sortType
    boolean sortType;

    // toolbar
    Toolbar toolbar;

    // status variables
    Boolean areFABsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        state = STATES.NOTES;

        setTheme(R.style.ThemeGrey);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.my_notes);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(Statics.DEFAULT_TOOLBAR_COLOR));

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_notes);
        navigationView.setNavigationItemSelectedListener(item -> {

            final int notes = R.id.nav_notes, recycler = R.id.nav_recycler_bin, settings = R.id.nav_settings, about = R.id.nav_about;

            switch (item.getItemId()){
                case notes:
                    saveNoteList();
                    initNoteState();
                    state = STATES.NOTES;
                    break;
                case recycler:
                    saveNoteList();
                    initRecyclerState();
                    state = STATES.DELETED_NOTES;
                    break;
                case settings:
                    Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                    break;
                case about:
                    Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                    break;
            }

            drawer.closeDrawers();

            return true;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mainFAB = findViewById(R.id.fab_main);
        textFAB = findViewById(R.id.fab_text);
        checkListFAB = findViewById(R.id.fab_checklist);

        // initializing FABs
        mainFAB.shrink();

        textFAB.hide();
        textFAB.setOnClickListener(view -> textFABOnClickListener());

        checkListFAB.hide();
        checkListFAB.setOnClickListener(view -> checkListFABOnClickListener());

        areFABsVisible = false;

        // main FAB onClickListener
        mainFAB.setOnClickListener(view -> mainFABHandler());

        rv = findViewById(R.id.note_recycler_view);

        initNoteState();
    }

    private void initNoteState(){

        noteList.clear();

        for (String s : MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST,this)) {
            switch (Note.getNoteClass(s)){
                case TEXT_NOTE: noteList.add(MySharedPreferences.LoadTextNoteFromSharedPreferences(s,this)); break;
                case CHECK_NOTE: noteList.add(MySharedPreferences.LoadCheckListNoteFromSharedPreferences(s,this)); break;
            }
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
            }

            @Override
            public void OnOptionOneClick(int position) {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        getApplicationContext(),
                        3, // TODO: make a global variable for color
                        R.drawable.ic_delete,
                        getString(R.string.confirm_delete),
                        () -> adapter.removeItem(position)
                );
                dialogConfirm.show(getSupportFragmentManager(),Statics.TAG_DIALOG_CONFIRM);
            }

            @Override
            public void OnOptionTwoClick(int position) {
            }

        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        mainFAB.show();

        toolbar.setTitle(R.string.my_notes);
    }

    private void initRecyclerState(){

        noteList.clear();

        for (String s : MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST_TRASH,this)) {
            switch (Note.getNoteClass(s)){
                case TEXT_NOTE: noteList.add(MySharedPreferences.LoadTextNoteFromSharedPreferences(s,this)); break;
                case CHECK_NOTE: noteList.add(MySharedPreferences.LoadCheckListNoteFromSharedPreferences(s,this)); break;
            }
        }

        // initializing the recycler view and its adapter
        // and displaying the list of the notes
        adapter = new NoteDeletedAdapter(noteList,getApplicationContext());

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                // open a dialog asking the user for confirmation
                DialogConfirm dialogConfirm = new DialogConfirm(
                        getApplicationContext(),
                        3,
                        R.drawable.ic_info,
                        getString(R.string.confirm_restore_open),
                        () -> {
                            ArrayList<String> temp = MySharedPreferences.LoadStringArrayToSharedPreferences(
                                    Statics.KEY_NOTE_LIST,
                                    getApplicationContext()
                            );

                            temp.add(noteList.get(position).getUid());

                            MySharedPreferences.SaveStringArrayToSharedPreferences(
                                    temp,
                                    Statics.KEY_NOTE_LIST,
                                    getApplicationContext()
                            );

                            Intent i;

                            if (TextNote.class.equals(noteList.get(position).getClass())) {

                                i = new Intent(getApplicationContext(),NoteActivity.class);
                                i.putExtra(Statics.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
                                startActivity(i);
                            }

                            if (CheckListNote.class.equals(noteList.get(position).getClass())){
                                i = new Intent(getApplicationContext(),CheckListNoteActivity.class);
                                i.putExtra(Statics.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
                                startActivity(i);
                            }

                            adapter.removeItem(position);
                        }
                );
                dialogConfirm.show(getSupportFragmentManager(),Statics.TAG_DIALOG_CONFIRM);
            }

            @Override
            public void OnLongClickListener(int position) {
            }

            // restore
            @Override
            public void OnOptionOneClick(int position) {
                // open a dialog asking the user for confirmation
                DialogConfirm dialogConfirm = new DialogConfirm(
                        getApplicationContext(),
                        3,
                        R.drawable.ic_info,
                        getString(R.string.confirm_restore),
                        () -> {
                            ArrayList<String> temp = MySharedPreferences.LoadStringArrayToSharedPreferences(
                                    Statics.KEY_NOTE_LIST,
                                    getApplicationContext()
                            );

                            temp.add(noteList.get(position).getUid());

                            MySharedPreferences.SaveStringArrayToSharedPreferences(
                                    temp,
                                    Statics.KEY_NOTE_LIST,
                                    getApplicationContext()
                            );

                            adapter.removeItem(position);
                        }
                );
                dialogConfirm.show(getSupportFragmentManager(),Statics.TAG_DIALOG_CONFIRM);
            }

            // delete permanently
            @Override
            public void OnOptionTwoClick(int position) {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        getApplicationContext(),
                        3,
                        R.drawable.ic_delete,
                        getString(R.string.confirm_delete_permanently),
                        () -> {
                            MySharedPreferences.DeleteNote(noteList.get(position).getUid(),getApplicationContext());
                            adapter.removeItem(position);
                        }
                );
                dialogConfirm.show(getSupportFragmentManager(),Statics.TAG_DIALOG_CONFIRM);
            }

        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        mainFAB.hide();

        toolbar.setTitle(R.string.recycler_bin);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity,menu);

        // search
        MenuItem search = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) search.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // save by title
        MenuItem sortAlpha = menu.findItem(R.id.sort_alpha);
        sortAlpha.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByTitle(sortType);
            return true;
        } );

        // save by color
        MenuItem sortColor = menu.findItem(R.id.sort_color);
        sortColor.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByColor(sortType);
            return true;
        });

        // sort by creation date
        MenuItem sortCreation = menu.findItem(R.id.sort_creation);
        sortCreation.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByCreation(sortType);
            return true;
        });

        // sort by modification date
        MenuItem sortModification = menu.findItem(R.id.sort_modification);
        sortModification.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByModification(sortType);
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    // action to be executed
    // when the activity is paused or suspended
    @Override
    protected void onPause() {
        super.onPause();

        noteList = new ArrayList<>(adapter.getListFull());
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

        if (CheckListNote.class.equals(noteList.get(position).getClass())){
            i = new Intent(getApplicationContext(),CheckListNoteActivity.class);
            i.putExtra(Statics.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
            startActivity(i);
        }
    }

    // collect the UIDs of the notes
    // and save them to the shared preferences
    private void saveNoteList(){

        ArrayList<String> mNoteList = new ArrayList<>();
        for (Note<?> note : noteList) { mNoteList.add(note.getUid()); }

        switch (state){
            case NOTES:
                MySharedPreferences.SaveStringArrayToSharedPreferences(mNoteList,Statics.KEY_NOTE_LIST,getApplicationContext());
                break;
            case DELETED_NOTES:
                MySharedPreferences.SaveStringArrayToSharedPreferences(mNoteList,Statics.KEY_NOTE_LIST_TRASH,getApplicationContext());
                break;
        }
    }

}