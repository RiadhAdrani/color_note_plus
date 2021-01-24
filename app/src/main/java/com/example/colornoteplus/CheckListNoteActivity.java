package com.example.colornoteplus;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Activity displaying a Check List Note
 * @see Activity
 * @see CheckListNote
 * @see CheckListItem
 * @see App
 * @see Style
 * @see DatabaseManager
 * @see Sync
 */
public class CheckListNoteActivity extends Activity {

    /**
     * Title View
     */
    private EditText titleView;

    /**
     * Title character count view
     */
    private TextView titleCharacterCount;

    /**
     * Color switcher button
     */
    private ImageButton colorView;

    /**
     * Recycler view for the checkListItems
     * @see CheckListItem
     * @see CheckListNote
     */
    private RecyclerView contentView;

    /**
     * FAB (floating action button) allowing the addition of new check list items
     * @see CheckListItem
     */
    private FloatingActionButton fab;

    /**
     * Adapter for the recycler view displaying check list items
     */
    CheckListAdapter adapter;

    /**
     * check list items sort Type
     */
    private boolean sortType = false;

    /**
     * Activity Toolbar
     */
    private Toolbar toolbar;

    /**
     * Currently open note
     */
    private CheckListNote note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the note depending on the UID given from the Main Activity
        if (isNoteOld(getIntent().getStringExtra(App.KEY_NOTE_ACTIVITY))){

            // if the UID exists in the SharedPreference
            // Load the note
            note = DatabaseManager.LoadCheckListNote(getIntent().getStringExtra(App.KEY_NOTE_ACTIVITY),getApplicationContext());
        }
        else {

            // if it is new
            // create a new note
            note = new CheckListNote(getApplicationContext());
        }

        // change the theme of the activity
        // and the color scheme
        changeViewsColor(note.getColor());

        // set the title
        titleView.setText(note.getTitle());
    }

    @Override
    public void onBackPressed() {

        // save the note
        note.setTitle(titleView.getText().toString().trim());

        if (note.hasChanged(this)){
            unsavedChangesAlert();
            Toast.makeText(this, "Unsaved changes", Toast.LENGTH_SHORT).show();
            return;
        }

        startMainActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check_list_activity,menu);

        // save button
        MenuItem save = menu.findItem(R.id.save);
        save.setOnMenuItemClickListener(menuItem -> saveNote());

        // sort by alpha
        MenuItem sortAlpha = menu.findItem(R.id.sort_alpha);
        sortAlpha.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByDescription(sortType);
            return true;
        });

        // sort by status
        MenuItem sortStatus = menu.findItem(R.id.sort_status);
        sortStatus.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByStatus(sortType);
            return true;
        });

        // sort by creation
        MenuItem sortCreation = menu.findItem(R.id.sort_creation);
        sortCreation.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByCreation(sortType);
            return true;
        });

        // sort by modification
        MenuItem sortModification = menu.findItem(R.id.sort_modification);
        sortModification.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByModification(sortType);
            return true;
        });

        // sort by due
        MenuItem sortDue = menu.findItem(R.id.sort_due);
        sortDue.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByDue(sortType);
            return true;
        });

        // sort by due
        MenuItem sortPriority = menu.findItem(R.id.sort_priority);
        sortPriority.setOnMenuItemClickListener(menuItem -> {
            adapter.sortByPriority(sortType);
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.home)
            return onUpButtonPressed();

        return true;
    }

    /**
     * Change the color of the views
     * @param color new color value
     */
    private void changeViewsColor(int color){

        // set the global theme
        setTheme(Style.getTheme(getApplicationContext(),color));

        // set the appropriate layout
        setContentView(R.layout.activity_check_list_note);

        // change status bar color
        getWindow().setStatusBarColor(getResources().getColor(Style.getColorMain(getApplicationContext(),color)));

        // change background color
        findViewById(R.id.check_list_activity_background).setBackgroundColor(
                getResources().getColor(Style.getNeutralColor(getApplicationContext())));

        // set up FAB action
        fab = findViewById(R.id.fab_add_item);
        fab.setBackgroundColor(getResources().getColor(Style.getColorPrimary(getApplicationContext(),color)));
        fab.setOnClickListener(view -> onFabClickListener());

        // setting the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(Style.getColorMain(getApplicationContext(),color)));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onUpButtonPressed());

        // setting the note title
        titleView = findViewById(R.id.note_title_view);
        titleView.setText("");
        titleView.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));
        titleView.setHintTextColor(getResources().getColor(Style.getColorSecondary(getApplicationContext(),color)));

        // setting the character counter for the note title
        titleCharacterCount = findViewById(R.id.note_title_characters);
        String m = titleView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
        titleCharacterCount.setText(m);
        titleCharacterCount.setTextColor(getResources().getColor(Style.getColorMain(getApplicationContext(),color)));

        titleView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                String msg = titleView.getText().toString().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
                titleCharacterCount.setText(msg);
            }
        });

        // setting the color view
        colorView = findViewById(R.id.note_color_view);
        colorView.setOnClickListener(view -> buildColorPickDialog());
        colorView.setBackgroundResource(Style.getBackground(getApplicationContext(),color));

        // check list items adapter
        adapter = new CheckListAdapter(getApplicationContext(),note.getContent(),color);
        adapter.setOnItemClickListener(new CheckListAdapter.OnItemClickListener() {

            // set the item to done
            @Override
            public void onChecked(int position) {
                note.getContent().get(position).setDone();
            }

            // set the item to undone
            @Override
            public void onUnchecked(int position) {
                note.getContent().get(position).setUnDone();
            }

            // allow the user to change the priority
            @Override
            public void onSetPriority(int position,int priority) {
                note.getContent().get(position).setPriority(CheckListItem.PRIORITY.values()[priority]);
            }

            // allow the user to change or set a reminder/due date
            @Override
            public void onSetReminder(int position) {
                FragmentDatePicker datePicker = new FragmentDatePicker(note.getColor());
                datePicker.show(getSupportFragmentManager(), App.TAG_FRAGMENT_DATE_PICKER);
                datePicker.setOnDateSet((year, month, day) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);
                    note.getContent().get(position).setDueDate(c.getTime().getTime());
                    adapter.notifyItemChanged(position);
                });
            }

            // delete the item from the list
            @Override
            public void onDelete(int position) {
                adapter.removeItem(position);
            }

            // save the item when the user add/remove any character in the EditText
            @Override
            public void onDescriptionChanged(int position,String description) {
                note.getContent().get(position).setDescription(description);
            }
        });

        // initializing and formatting the content recycler view
        contentView = findViewById(R.id.note_content_view);
        contentView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contentView.setAdapter(adapter);

    }

    /**
     * Switch the color of the activity
     * @param color new color value
     */
    private void switchColor(int color){

        String tempTitle = titleView.getText().toString().trim();
        changeViewsColor(color);
        titleView.setText(tempTitle);

    }


    /**
     * Displaying a dialog allowing the user to switch the color of the note
     * and the activity.
     */
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,note.getColor());
        fragment.show(getSupportFragmentManager(), App.TAG_FRAGMENT_COLOR_PICK);
        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                note.setColor(position);
                switchColor(position);
                if (isNoteOld(note.getUid())){
                    note.save(getApplicationContext());
                }
                fragment.dismiss();
            }

            // (UNUSED)
            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

    /**
     * Handle click on the FAB : display a dialog box allowing the user to customize
     * his new check list item
     * @see CheckListItem
     */
    private void onFabClickListener(){
        FragmentAddCheckListItem fragment = new FragmentAddCheckListItem(note.getColor());
        fragment.show(getSupportFragmentManager(), App.TAG_FRAGMENT_ADD_CHECK_LIST_ITEM);
        fragment.setOnClickListener(new FragmentAddCheckListItem.OnClickListener() {

            // add the item to the check list
            @Override
            public void onConfirmClickListener() {
                fragment.getItem().setDescription(fragment.getInputText());
                adapter.addItem(fragment.getItem(),0);
                contentView.scrollToPosition(0);
                fragment.dismiss();
            }

            // allow the user to select a date from a displayed calendar
            // which will be set as reminder/due date.
            @Override
            public void onSetDueTimeClickListener() {
                FragmentDatePicker datePicker = new FragmentDatePicker(note.getColor());
                datePicker.show(getSupportFragmentManager(), App.TAG_FRAGMENT_DATE_PICKER);
                datePicker.setOnDateSet((year, month, day) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);
                    fragment.getItem().setDueDate(c.getTime().getTime());
                    fragment.setDueTimeText(DateFormat.getDateInstance().format(new Date(fragment.getItem().getDueDate())));
                });
            }
        });
    }

    /**
     * check if the note is old (already exists in the local database)
     * @see DatabaseManager
     * @param UID note uid
     * @return the result of the checking
     */
    private boolean isNoteOld(String UID){
        for (String n: DatabaseManager.LoadStringArray(App.KEY_NOTE_LIST,getApplicationContext())){
            if (n.equals(UID)) return true;
        }
        return false;
    }

    /**
     * save note to the local database
     * @return return if the operation succeeded or not
     * (due to errors or some non-respected constraints).
     */
    private boolean saveNote(){

        // if the text is too short, alert the user
        if (titleView.getText().toString().trim().length() < App.NOTE_TITLE_MINIMUM_LENGTH){

            App.StyleableToast(getApplicationContext(),
                    getString(R.string.title_short),
                    Style.getColorMain(getApplicationContext(),note.getColor()),
                    Style.getNeutralColor(getApplicationContext()),
                    3,
                    true);

            return false;
        }

        // else, proceed to save the note
        else {

            // save the note
            note.setTitle(titleView.getText().toString().trim());
            if (note.getContent().size() > 0){
                for (int i = 0; i < note.getContent().size(); i++){
                    if (note.getContent().get(i).getDescription().trim().isEmpty()){
                        adapter.removeItem(i);
                    }
                }
            }
            note.save(getApplicationContext());

            // if note is not old
            // add its UID to the shared preferences
            if (!isNoteOld(note.getUid())){

                Log.d("DEBUG_SAVE","Note is old !");

                ArrayList<String> noteList =
                        DatabaseManager.LoadStringArray(
                                App.KEY_NOTE_LIST,getApplicationContext()
                        );

                noteList.add(note.getUid());
                DatabaseManager.SaveStringArray(
                        noteList, App.KEY_NOTE_LIST,getApplicationContext()
                );
            }

            // alert user of the success
            App.StyleableToast(getApplicationContext(),
                    getString(R.string.save_success),
                    Style.getColorMain(getApplicationContext(),note.getColor()),
                    Style.getNeutralColor(getApplicationContext()),
                    3,
                    false);
        }

        return true;

    }

    /**
     * Start main activity
     * @see MainActivity
     */
    private void startMainActivity(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * display an alert if the current note has changed ,just before leaving the activity
     */
    private void unsavedChangesAlert(){
        DialogConfirm dialogConfirm = new DialogConfirm(
                note.getColor(),
                R.drawable.ic_info,
                getString(R.string.confirm_unsaved_changes),
                3,
                new DialogConfirm.OnConfirmClickListener() {

                    // save and exit
                    @Override
                    public void OnPrimaryAction() {

                        if (saveNote())
                            startMainActivity();
                    }

                    // discard
                    @Override
                    public void OnSecondaryAction() {
                        startMainActivity();
                    }
                });
        dialogConfirm.show(getSupportFragmentManager(), App.TAG_DIALOG_CONFIRM);
    }

    /**
     * checking to be executed before exiting the activity
     * @return return true
     */
    private boolean onUpButtonPressed(){
        // save the note
        note.setTitle(titleView.getText().toString().trim());

        if (note.hasChanged(getApplicationContext())){
            unsavedChangesAlert();
            return true;
        }

        startMainActivity();
        return true;
    }

}