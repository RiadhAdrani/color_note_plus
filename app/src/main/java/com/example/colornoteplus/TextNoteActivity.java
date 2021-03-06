package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Display Text Note in the activity
 * @see TextNote
 */
public class TextNoteActivity extends Activity {

    // note editable views
    private EditText titleView;
    private TextView titleCharacterCount;
    private ImageButton colorView;
    private EditText contentView;
    private TextView contentCharacterCount;

    // toolbar
    private Toolbar toolbar;

    // status
    private boolean lock = true;

    // Current note
    private TextNote note;

    private boolean deletedSpecialCharacter = false;
    private boolean deleteRedoStack = false;

    // Undo & Redo Handler
    private TextUndoRedo textUndoRedoHandler;

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    // Literally Main()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // note = new TextNote("MyNote",5);

        getNoteFromIntent();

        lock = isNoteOld();

        textUndoRedoHandler = new TextUndoRedo();

        initTheme();
    }

    // Method used to add menus and configure button action
    // like OnClickListeners ...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_note_activity_toolbar,menu);

        MenuItem undoButton = menu.findItem(R.id.undo);
        undoButton.setOnMenuItemClickListener(menuItem -> undo());

        MenuItem redoButton = menu.findItem(R.id.redo);
        redoButton.setOnMenuItemClickListener(menuItem -> redo());

        MenuItem saveButton = menu.findItem(R.id.save);
        saveButton.setOnMenuItemClickListener(menuItem -> saveTextNote());

        MenuItem lockButton = menu.findItem(R.id.lock);
        lockButton.setIcon(lock ? R.drawable.ic_locked : R.drawable.ic_unlocked);
        lockButton.setOnMenuItemClickListener(menuItem -> {
            if (lock) {

                contentView.setFocusableInTouchMode(true);
                lock = false;
                lockButton.setIcon(R.drawable.ic_unlocked);
            }
            else {

                contentView.setFocusableInTouchMode(false);
                contentView.clearFocus();
                lockButton.setIcon(R.drawable.ic_locked);
                lock = true;
            }
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Return Text Note from the last intent.
     */
    private void getNoteFromIntent(){

        if (!getIntent().getStringExtra(App.KEY_NOTE_ACTIVITY).equals(App.NOTE_DEFAULT_UID)){
            note = DatabaseManager.getTextNote(getIntent().getStringExtra(App.KEY_NOTE_ACTIVITY),getApplicationContext());
        } else {
            note = new TextNote(getApplicationContext());
        }
    }

    /**
     * Initialize activity theme
     */
    private void initTheme(){

        setTheme(Style.getTheme(getApplicationContext(),note.getColor()));

        setContentView(R.layout.activity_note);

        changeViewsColor();

        textUndoRedoHandler.pushUndo(contentView.getText().toString().trim());
    }

    /**
     * Switch activity and note color themes
     * @param id new color theme
     */
    private void switchTheme(int id){

        String titleTemp = titleView.getText().toString().trim();
        String textTemp = contentView.getText().toString().trim();

        // change theme
        setTheme(Style.getTheme(getApplicationContext(),id));
        setContentView(R.layout.activity_note);
        getWindow().setStatusBarColor(getResources().getColor(Style.getColorMain(getApplicationContext(),id)));

        changeViewsColor();

        titleView.setText(titleTemp);
        contentView.setText(textTemp);

    }

    /**
     * Change views coloring to the current color theme
     */
    private void changeViewsColor(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(Style.getColorMain(getApplicationContext(),note.getColor())));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onUpButtonPressed());

        findViewById(R.id.text_note_activity_background).setBackgroundColor(
                getResources().getColor(Style.getNeutralColor(getApplicationContext()))
        );

        titleView = findViewById(R.id.note_title_view);
        titleView.setText(note.getTitle());
        titleView.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));
        titleView.setHintTextColor(getResources().getColor(Style.getColorSecondary(getApplicationContext(),note.getColor())));

        titleCharacterCount = findViewById(R.id.note_title_characters);
        String m = titleView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
        titleCharacterCount.setText(m);
        titleCharacterCount.setTextColor(getResources().getColor(Style.getColorMain(getApplicationContext(),note.getColor())));

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

        contentView = findViewById(R.id.note_content_view);
        contentView.setText(note.getContent());
        contentView.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));
        contentView.setHintTextColor(getResources().getColor(Style.getColorMain(getApplicationContext(),note.getColor())));
        contentView.setScroller(new Scroller(getApplicationContext()));
        contentView.setVerticalScrollBarEnabled(true);
        contentView.setMovementMethod(new ScrollingMovementMethod());

        contentCharacterCount = findViewById(R.id.note_content_characters);
        contentCharacterCount.setTextColor(getResources().getColor(Style.getColorMain(getApplicationContext(),note.getColor())));
        m = contentView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.content_max_length);
        contentCharacterCount.setText(m);

        contentView.setOnKeyListener((view, i, keyEvent) -> {

            if (i == KeyEvent.KEYCODE_DEL){
                if (!contentView.getText().toString().isEmpty()){
                    if (App.SPECIAL_STRINGS.contains(contentView.getText().toString().charAt(contentView.getText().toString().length() - 1))){
                        deletedSpecialCharacter = true;
                        Log.d("UNDO_REDO","Deleted special Character");
                    }
                }
            }

            return false;
        });

        contentView.addTextChangedListener(new TextWatcher()
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
                String msg = contentView.getText().toString().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.content_max_length);
                contentCharacterCount.setText(msg);

                if (!s.toString().isEmpty()) myUpdateUndo(s.charAt(s.toString().length()-1));

            }
        });

        contentView.setFocusableInTouchMode(!lock);
        contentView.clearFocus();
        colorView = findViewById(R.id.note_color_view);
        colorView.setOnClickListener(view -> buildColorPickDialog());
        colorView.setBackgroundResource(Style.getBackground(getApplicationContext(), note.getColor()));

    }

    /**
     * Check whether the note is newly created or not
     * @return true if it exists in the local database, else false
     * @see DatabaseManager
     */
    private boolean isNoteOld(){
        for (String n: DatabaseManager.getStringArray(App.KEY_NOTE_LIST,getApplicationContext())){
            if (n.equals(note.getUid())) return true;
        }
        return false;
    }

    /**
     * Allow the user to pick a color from a list
     * @see FragmentPickColor
     * @see Style
     */
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,note.getColor());

        fragment.show(getSupportFragmentManager(), App.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                note.setColor(position);
                switchTheme(position);
                if (isNoteOld()){
                    note.save(getApplicationContext());
                }
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

    /**
     * Save the current text note
     * @return true if the operation succeeded, false for failure
     */
    private boolean saveTextNote(){

        if (titleView.getText().toString().trim().length() < App.NOTE_TITLE_MINIMUM_LENGTH){

            App.StyleableToast(getApplicationContext(),
                    getString(R.string.title_short),
                    Style.getColorMain(getApplicationContext(),note.getColor()),
                    Style.getNeutralColor(getApplicationContext()),
                    3,
                    true);

            return false;
        }
        else {

            note.setTitle(titleView.getText().toString().trim());
            note.setContent(contentView.getText().toString().trim());
            note.save(getApplicationContext());

            if (!isNoteOld()){

                Log.d("DEBUG_SAVE","Note is old !");

                ArrayList<String> noteList =
                        DatabaseManager.getStringArray(
                                App.KEY_NOTE_LIST,getApplicationContext()
                        );

                noteList.add(note.getUid());
                DatabaseManager.setStringArray(
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
     * perform update for the undo/redo stacks depending on the last character, deleted or added
     * @param lastCharacter last character deleted/added
     * @see TextUndoRedo
     */
    private void myUpdateUndo(char lastCharacter){

        if (deletedSpecialCharacter){

            textUndoRedoHandler.pushUndo(contentView.getText().toString().trim());
            deletedSpecialCharacter = false;

            Log.d("UNDO_REDO","Last character: "+lastCharacter);
            Log.d("UNDO_REDO","Elements in Undo Stack: "+textUndoRedoHandler.getUndoStack().size());
            return;
        }

        if (App.SPECIAL_STRINGS.contains(lastCharacter)){

                textUndoRedoHandler.pushUndo(contentView.getText().toString().trim());
        }

        Log.d("UNDO_REDO","Last character: "+lastCharacter);
        Log.d("UNDO_REDO","Elements in Undo Stack: "+textUndoRedoHandler.getUndoStack().size());
    }

    /**
     * Undo last action made for the content text
     * @return true
     * @see TextUndoRedo
     */
    private boolean undo(){

        if (!textUndoRedoHandler.getUndoStack().empty()) {
            if (textUndoRedoHandler.getUndoStack().size() == 1){

                if (deleteRedoStack) {
                    textUndoRedoHandler.resetRedo();
                    deleteRedoStack = true;
                }

                textUndoRedoHandler.pushRedo(contentView.getText().toString().trim());
                contentView.setText(textUndoRedoHandler.getUndoStack().firstElement());
                App.StyleableToast(getApplicationContext(),
                        getString(R.string.nothing_to_undo),
                        Style.getColorPrimary(getApplicationContext(),note.getColor()),
                        R.color.white,
                        3,
                        false);
            }
            else {

                if (deleteRedoStack) {
                    textUndoRedoHandler.resetRedo();
                    deleteRedoStack = true;
                }

                textUndoRedoHandler.pushRedo(contentView.getText().toString().trim());
                contentView.setText(textUndoRedoHandler.popUndo());
            }
        }
        return true;
    }

    /**
     * Redo the previously undone action
     * @return true
     * @see TextUndoRedo
     */
    private boolean redo(){


        Log.d("UNDO_REDO","Elements in Redo Stack: "+textUndoRedoHandler.getRedoStack().size());

        if (!textUndoRedoHandler.getRedoStack().empty()){
            contentView.setText(textUndoRedoHandler.popRedo());
            deleteRedoStack = false;
        }
        else {
            App.StyleableToast(getApplicationContext(),
                    getString(R.string.nothing_to_redo),
                    Style.getColorPrimary(getApplicationContext(),note.getColor()),
                    R.color.white,
                    3,
                    false);
        }

        return true;
    }

    /**
     * Return to main activity
     * @see MainActivity
     */
    private void startMainActivity(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Display an alert in case there is some unsaved modifications
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

                        if (saveTextNote())
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
     * Action executed upon pressing up button from the toolbar
     */
    private void onUpButtonPressed(){

        // save the note
        note.setTitle(titleView.getText().toString().trim());
        note.setContent(contentView.getText().toString().trim());

        if (note.hasChanged(getApplicationContext())){
            unsavedChangesAlert();
            return;
        }

        startMainActivity();
    }

    @Override
    public void onBackPressed() {

        // save the note
        note.setTitle(titleView.getText().toString().trim());
        note.setContent(contentView.getText().toString().trim());

        if (note.hasChanged(this)){
            unsavedChangesAlert();
            Toast.makeText(this, "Unsaved changes", Toast.LENGTH_SHORT).show();
            return;
        }

        startMainActivity();
    }

}