package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    // Activity state
    private STATES state;

    // Activity theme
    private int theme ;

    // recycler view and its adapter
    private RecyclerView rv;
    private NoteAdapter adapter;

    // navigation drawer
    private DrawerLayout drawer;
    private NavigationView navigationView;

    // note list
    private ArrayList<Note<?>> noteList = new ArrayList<>();

    // floating action buttons
    private ExtendedFloatingActionButton mainFAB;
    private FloatingActionButton textFAB;
    private FloatingActionButton checkListFAB;

    // sortType
    private boolean sortType ;

    // toolbar
    private Toolbar toolbar;

    private ConstraintLayout selectionToolbar;
    private ConstraintLayout recyclerSelectionToolbar;

    // status variables
    private Boolean areFABsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        state = STATES.NOTES;

        theme = Style.getAppColor(getApplicationContext());

        setTheme(Style.getTheme(getApplicationContext(),theme));
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_activity_background).setBackgroundColor(
                getResources().getColor(Style.getNeutralColor(getApplicationContext()))
        );

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.my_notes);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(Style.getColorMain(getApplicationContext(),theme)));

        recyclerSelectionToolbar = findViewById(R.id.recycler_selection_toolbar);
        recyclerSelectionToolbar.setBackgroundColor(getResources().getColor(Style.getColorMain(getApplicationContext(),theme)));

        ImageView rTCancel = recyclerSelectionToolbar.findViewById(R.id.toolbar_cancel);
        rTCancel.setOnClickListener( v -> exitRecyclerSelectionMode());

        ImageView rTSelectAll = recyclerSelectionToolbar.findViewById(R.id.toolbar_select_all);
        rTSelectAll.setOnClickListener( v -> {
            if (adapter.getSelectedItems().size() != adapter.getList().size()){
                adapter.getSelectedItems().clear();
                for(Note<?> note : adapter.getList()){
                    adapter.getSelectedItems().add(note.getUid());
                }
            }
            else {
                adapter.getSelectedItems().clear();
            }

            adapter.notifyDataSetChanged();
        });

        ImageView rTEmptyBin = recyclerSelectionToolbar.findViewById(R.id.toolbar_empty_recycler);
        rTEmptyBin.setOnClickListener(v -> {

            DialogConfirm dialogConfirm = new DialogConfirm(
                    theme,
                    R.drawable.ic_info,
                    getString(R.string.confirm_delete_permanently_all),
                    new DialogConfirm.OnConfirmClickListener() {
                        @Override
                        public void OnPrimaryAction() {
                            for (Note<?> note: adapter.getListFull()){
                                DatabaseManager.DeleteNote(note.getUid(),getApplicationContext());
                            }
                            adapter.getList().clear();
                            adapter.getListFull().clear();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void OnSecondaryAction() {
                            // Unused
                        }
                    }
            );

            dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
        });

        ImageView rTDeletePermanently = recyclerSelectionToolbar.findViewById(R.id.toolbar_delete_selection);
        rTDeletePermanently.setOnClickListener( v -> {

            if (adapter.getSelectedItems().isEmpty()){
                Toast.makeText(this, getString(R.string.no_item_selected), Toast.LENGTH_SHORT).show();
            }
            else {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        theme,
                        R.drawable.ic_info,
                        getString(R.string.confirm_delete_permanently_selected),
                        new DialogConfirm.OnConfirmClickListener() {
                            @Override
                            public void OnPrimaryAction() {
                                for (int i = 0; i < adapter.getListFull().size() ; i++){
                                    if (adapter.isSelected(adapter.getListFull().get(i).getUid())){
                                        adapter.deleteItemPermanently(i);
                                        i --;
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void OnSecondaryAction() {
                                // Unused
                            }
                        }
                );

                dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
            }

        });

        ImageView rTRestore = recyclerSelectionToolbar.findViewById(R.id.toolbar_restore);
        rTRestore.setOnClickListener( v -> {

            if (adapter.getSelectedItems().isEmpty()){
                Toast.makeText(this, getString(R.string.no_item_selected), Toast.LENGTH_SHORT).show();
            } else {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        theme,
                        R.drawable.ic_info,
                        getString(R.string.confirm_restore_selected),
                        new DialogConfirm.OnConfirmClickListener() {
                            @Override
                            public void OnPrimaryAction() {
                                for (int i = 0; i < adapter.getListFull().size() ; i++){
                                    if (adapter.isSelected(adapter.getListFull().get(i).getUid())){
                                        adapter.restoreItem(i);
                                        i --;
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void OnSecondaryAction() {
                                // Unused
                            }
                        }
                );

                dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
            }
        });

        selectionToolbar = findViewById(R.id.selection_toolbar);
        selectionToolbar.setBackgroundColor(getResources().getColor(Style.getColorMain(getApplicationContext(),theme)));
        ImageView toolbarCancel = selectionToolbar.findViewById(R.id.toolbar_cancel);
        toolbarCancel.setOnClickListener( view -> exitSelectionMode() );

        ImageView toolbarSelectAll = selectionToolbar.findViewById(R.id.toolbar_select_all);
        toolbarSelectAll.setOnClickListener(view -> {

            if (adapter.getSelectedItems().size() != adapter.getList().size()){
                adapter.getSelectedItems().clear();
                for(Note<?> note : adapter.getList()){
                    adapter.getSelectedItems().add(note.getUid());
                }
            }
            else {
                adapter.getSelectedItems().clear();
            }

            adapter.notifyDataSetChanged();
        });

        ImageView toolbarDelete = selectionToolbar.findViewById(R.id.toolbar_delete_selection);
        toolbarDelete.setOnClickListener( v -> {

            if (adapter.getSelectedItems().isEmpty()){
                Toast.makeText(this, getString(R.string.no_item_selected), Toast.LENGTH_SHORT).show();

            } else {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        theme,
                        R.drawable.ic_info,
                        getString(R.string.confirm_delete_selected),
                        new DialogConfirm.OnConfirmClickListener() {
                            @Override
                            public void OnPrimaryAction() {
                                for (int i = 0; i < adapter.getList().size(); i++){
                                    if (adapter.isSelected(adapter.getList().get(i).getUid())){
                                        adapter.removeItem(i);
                                        i--;
                                    }
                                }
                            }

                            @Override
                            public void OnSecondaryAction() {

                            }
                        }
                        );
                dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
            }

        });

        ImageView toolbarChangeSelectionColor = selectionToolbar.findViewById(R.id.toolbar_change_selection_color);
        toolbarChangeSelectionColor.setOnClickListener(
                v -> {
                    if (adapter.getSelectedItems().isEmpty()){
                        Toast.makeText(this, getString(R.string.no_item_selected), Toast.LENGTH_SHORT).show();
                    }
                    else
                        buildColorPickDialog();
                });

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_notes);
        navigationView.setNavigationItemSelectedListener(item -> {

            final int
                    notes = R.id.nav_notes,
                    recycler = R.id.nav_recycler_bin,
                    settings = R.id.nav_settings,
                    about = R.id.nav_about,
                    updates = R.id.nav_updates;

            switch (item.getItemId()){
                case notes:
                    startMyNote();
                    break;
                case recycler:
                    startRecyclerBin();
                    break;
                case settings:
                    Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case about:
                    about();
                    break;
                case updates:
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

        navigationView.setCheckedItem(R.id.nav_notes);

        for (String s : DatabaseManager.LoadStringArray(App.KEY_NOTE_LIST,this)) {
            switch (Note.getNoteClass(s)){
                case TEXT_NOTE: noteList.add(DatabaseManager.LoadTextNote(s,this)); break;
                case CHECK_NOTE: noteList.add(DatabaseManager.LoadCheckListNote(s,this)); break;
            }
        }
        
        // initializing the recycler view and its adapter
        // and displaying the list of the notes
        adapter = new NoteAdapter(noteList,getApplicationContext());
        adapter.getSelectedItems().clear();

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {

                switch (adapter.getSelectionMode()){

                    // selecting a note will open it
                    case NO_SELECTION:
                        noteOnClickListener(position);
                        break;

                    // selecting a note will add it to the list of selected items
                    case SELECTION:

                        // if note is not selected : select it
                        if (!adapter.isSelected(noteList.get(position).getUid())) {
                            adapter.getSelectedItems().add(noteList.get(position).getUid());
                        }
                        // if note is selected : deselect it
                        else {
                            adapter.getSelectedItems().remove(noteList.get(position).getUid());
                        }

                        adapter.notifyItemChanged(position);
                        break;
                }

            }

            @Override
            public void OnLongClickListener(int position) {

                if (adapter.getSelectionMode() == NoteAdapter.SelectionMode.NO_SELECTION){
                    adapter.setSelectionMode(NoteAdapter.SelectionMode.SELECTION);
                    enterSelectionMode();
                }

                if (!adapter.isSelected(noteList.get(position).getUid())){
                    adapter.getSelectedItems().add(noteList.get(position).getUid());
                    adapter.notifyItemChanged(position);
                }

            }

            @Override
            public void OnOptionOneClick(int position) {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        theme,
                        R.drawable.ic_delete,
                        getString(R.string.confirm_delete),
                        new DialogConfirm.OnConfirmClickListener() {
                            @Override
                            public void OnPrimaryAction() {
                                adapter.removeItem(position);
                            }

                            @Override
                            public void OnSecondaryAction() {

                            }
                        }
                );
                dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
            }

            @Override
            public void OnOptionTwoClick(int position) {
                buildColorPickDialog(position);
            }

        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        mainFAB.show();

        toolbar.setTitle(R.string.my_notes);

        exitSelectionMode();
        exitRecyclerSelectionMode();
    }

    private void initRecyclerState(){

        noteList.clear();

        navigationView.setCheckedItem(R.id.nav_recycler_bin);

        for (String s : DatabaseManager.LoadStringArray(App.KEY_NOTE_LIST_TRASH,this)) {
            switch (Note.getNoteClass(s)){
                case TEXT_NOTE: noteList.add(DatabaseManager.LoadTextNote(s,this)); break;
                case CHECK_NOTE: noteList.add(DatabaseManager.LoadCheckListNote(s,this)); break;
            }
        }

        // initializing the recycler view and its adapter
        // and displaying the list of the notes
        adapter = new DeletedNoteAdapter(noteList,getApplicationContext());
        adapter.getSelectedItems().clear();

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {

                // if selection mode is not active
                if (adapter.getSelectionMode() == NoteAdapter.SelectionMode.NO_SELECTION){

                    // open a dialog asking the user for confirmation
                    DialogConfirm dialogConfirm = new DialogConfirm(
                            theme,
                            R.drawable.ic_info,
                            getString(R.string.confirm_restore_open),
                            new DialogConfirm.OnConfirmClickListener() {
                                @Override
                                public void OnPrimaryAction() {
                                    ArrayList<String> temp = DatabaseManager.LoadStringArray(
                                            App.KEY_NOTE_LIST,
                                            getApplicationContext()
                                    );

                                    temp.add(noteList.get(position).getUid());

                                    DatabaseManager.SaveStringArray(
                                            temp,
                                            App.KEY_NOTE_LIST,
                                            getApplicationContext()
                                    );

                                    Intent i;

                                    if (TextNote.class.equals(noteList.get(position).getClass())) {

                                        i = new Intent(getApplicationContext(), TextNoteActivity.class);
                                        i.putExtra(App.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
                                        startActivity(i);
                                        finish();
                                    }

                                    if (CheckListNote.class.equals(noteList.get(position).getClass())){
                                        i = new Intent(getApplicationContext(),CheckListNoteActivity.class);
                                        i.putExtra(App.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
                                        startActivity(i);
                                        finish();
                                    }

                                    adapter.removeItem(position);
                                }

                                @Override
                                public void OnSecondaryAction() {
                                    buildColorPickDialog(position);
                                }
                            }
                    );
                    dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
                }

                // if selection mode is active
                else {
                    adapter.selectDeselectItem(position);
                }

            }

            @Override
            public void OnLongClickListener(int position) {
                if (adapter.getSelectionMode() == NoteAdapter.SelectionMode.NO_SELECTION){
                    adapter.setSelectionMode(NoteAdapter.SelectionMode.SELECTION);
                    enterRecyclerSelectionMode();
                }
                adapter.selectDeselectItem(position);
                adapter.notifyItemChanged(position);
            }

            // restore
            @Override
            public void OnOptionOneClick(int position) {
                // open a dialog asking the user for confirmation
                DialogConfirm dialogConfirm = new DialogConfirm(
                        theme,
                        R.drawable.ic_info,
                        getString(R.string.confirm_restore),
                        new DialogConfirm.OnConfirmClickListener() {
                            @Override
                            public void OnPrimaryAction() {
                                {
                                    ArrayList<String> temp = DatabaseManager.LoadStringArray(
                                            App.KEY_NOTE_LIST,
                                            getApplicationContext()
                                    );

                                    temp.add(noteList.get(position).getUid());

                                    DatabaseManager.SaveStringArray(
                                            temp,
                                            App.KEY_NOTE_LIST,
                                            getApplicationContext()
                                    );

                                    adapter.removeItem(position);
                                }
                            }

                            @Override
                            public void OnSecondaryAction() {

                            }
                        }
                );
                dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
            }

            // delete permanently
            @Override
            public void OnOptionTwoClick(int position) {
                DialogConfirm dialogConfirm = new DialogConfirm(
                        theme,
                        R.drawable.ic_delete,
                        getString(R.string.confirm_delete_permanently),
                        new DialogConfirm.OnConfirmClickListener() {
                            @Override
                            public void OnPrimaryAction() {
                                    DatabaseManager.DeleteNote(noteList.get(position).getUid(),getApplicationContext());
                                    adapter.removeItem(position);
                            }

                            @Override
                            public void OnSecondaryAction() {

                            }
                        }
                );
                dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
            }

        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        exitSelectionMode();

        mainFAB.shrink();
        mainFAB.hide();
        textFAB.hide();
        checkListFAB.hide();
        areFABsVisible = false;

        toolbar.setTitle(R.string.recycler_bin);

        exitRecyclerSelectionMode();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (adapter.getSelectionMode() == NoteAdapter.SelectionMode.SELECTION){
            adapter.setSelectionMode(NoteAdapter.SelectionMode.NO_SELECTION);
            adapter.setSelectedItems(new ArrayList<>());
            adapter.notifyDataSetChanged();
            if (state == STATES.NOTES) exitSelectionMode();
            if (state == STATES.DELETED_NOTES) exitRecyclerSelectionMode();
            return;
        }

        if (adapter.getSelectionMode() == NoteAdapter.SelectionMode.NO_SELECTION && state == STATES.DELETED_NOTES){
            startMyNote();
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

        if (!App.stringArrayEqualStringArray(
                App.getNotesAsUIDFromList(noteList),
                DatabaseManager.LoadStringArray(App.KEY_NOTE_LIST,getApplicationContext()))
        )
        {
            saveNoteList();
        }
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
        Intent i = new Intent(this, TextNoteActivity.class);
        i.putExtra(App.KEY_NOTE_ACTIVITY, App.NOTE_DEFAULT_UID);
        startActivity(i);
        finish();
    }

    // create and open a check list
    // in a new activity
    private void checkListFABOnClickListener(){
        Intent i = new Intent(this,CheckListNoteActivity.class);
        i.putExtra(App.KEY_NOTE_ACTIVITY, App.NOTE_DEFAULT_UID);
        startActivity(i);
        finish();
    }

    // open the note activity
    // of a note at a given position
    // in note list
    private void noteOnClickListener(int position){

        Intent i;

        if (TextNote.class.equals(noteList.get(position).getClass())) {

            i = new Intent(getApplicationContext(), TextNoteActivity.class);
            i.putExtra(App.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
            startActivity(i);
            finish();
        }

        if (CheckListNote.class.equals(noteList.get(position).getClass())){
            i = new Intent(getApplicationContext(),CheckListNoteActivity.class);
            i.putExtra(App.KEY_NOTE_ACTIVITY,noteList.get(position).getUid());
            startActivity(i);
            finish();
        }
    }

    // collect the UIDs of the notes
    // and save them to the shared preferences
    private void saveNoteList(){

        ArrayList<String> mNoteList = new ArrayList<>();
        for (Note<?> note : noteList) { mNoteList.add(note.getUid()); }

        switch (state){
            case NOTES:
                DatabaseManager.SaveStringArray(mNoteList, App.KEY_NOTE_LIST,getApplicationContext());
                break;
            case DELETED_NOTES:
                DatabaseManager.SaveStringArray(mNoteList, App.KEY_NOTE_LIST_TRASH,getApplicationContext());
                break;
        }
    }

    // build the color picker dialog
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,theme);

        fragment.show(getSupportFragmentManager(), App.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {

                for (int i = 0;i < adapter.getList().size(); i++){
                    if (adapter.isSelected(noteList.get(i).getUid())){
                        adapter.switchColor(i,position);
                    }
                }
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

    // build the color picker dialog
    private void buildColorPickDialog(int notePosition){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,noteList.get(notePosition).getColor());

        fragment.show(getSupportFragmentManager(), App.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {

                adapter.switchColor(notePosition,position);
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

    void enterSelectionMode(){
        toolbar.setVisibility(View.INVISIBLE);
        selectionToolbar.setVisibility(View.VISIBLE);
        adapter.getSelectedItems().clear();
        adapter.notifyDataSetChanged();
        areFABsVisible = false;
        checkListFAB.hide();
        textFAB.hide();
        mainFAB.shrink();
        mainFAB.hide();
    }

    void exitSelectionMode(){
        adapter.setSelectionMode(NoteAdapter.SelectionMode.NO_SELECTION);
        toolbar.setVisibility(View.VISIBLE);
        selectionToolbar.setVisibility(View.INVISIBLE);
        adapter.getSelectedItems().clear();
        adapter.notifyDataSetChanged();
        mainFAB.show();
    }

    void enterRecyclerSelectionMode(){
        toolbar.setVisibility(View.INVISIBLE);
        recyclerSelectionToolbar.setVisibility(View.VISIBLE);
        adapter.getSelectedItems().clear();
        adapter.notifyDataSetChanged();
    }

    void exitRecyclerSelectionMode(){
        adapter.setSelectionMode(NoteAdapter.SelectionMode.NO_SELECTION);
        toolbar.setVisibility(View.VISIBLE);
        recyclerSelectionToolbar.setVisibility(View.INVISIBLE);
        adapter.getSelectedItems().clear();
        adapter.notifyDataSetChanged();
    }

    void startMyNote(){
        saveNoteList();
        initNoteState();
        state = STATES.NOTES;
    }

    void startRecyclerBin(){
        saveNoteList();
        initRecyclerState();
        state = STATES.DELETED_NOTES;
    }

    void about(){
        DialogConfirm dialog = new DialogConfirm(
                theme,
                R.drawable.ic_info,
                getString(R.string.about_12_2020),
                1,
                new DialogConfirm.OnConfirmClickListener() {
                    @Override
                    public void OnPrimaryAction() {

                    }

                    @Override
                    public void OnSecondaryAction() {

                    }
                });
        dialog.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
    }

}