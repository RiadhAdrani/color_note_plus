package com.example.colornoteplus;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    // note editable views
    private EditText titleView;
    private TextView titleCharacterCount;
    private ImageButton colorView;
    private EditText contentView;
    private TextView contentCharacterCount;

    // toolbar
    private Toolbar toolbar;

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

        return super.onCreateOptionsMenu(menu);
    }

    // get note from the intent
    private void getNoteFromIntent(){

        if (!getIntent().getStringExtra(Statics.KEY_NOTE_ACTIVITY).equals(Statics.NOTE_DEFAULT_UID)){
            note = MySharedPreferences.LoadTextNoteFromSharedPreferences(getIntent().getStringExtra(Statics.KEY_NOTE_ACTIVITY),getApplicationContext());
        } else {
            note = new TextNote();
        }
    }

    // Method used to automatically configure color theme of the activity
    private void initTheme(){

        setTheme(StyleManager.getTheme(note.getColor()));

        setContentView(R.layout.note_activity);

        changeViewsColor();

        textUndoRedoHandler.pushUndo(contentView.getText().toString().trim());
    }

    // switch theme when the user
    // click on a new color
    private void switchTheme(int id){

        String titleTemp = titleView.getText().toString().trim();
        String textTemp = contentView.getText().toString().trim();

        // change theme
        setTheme(StyleManager.getTheme(id));
        setContentView(R.layout.note_activity);
        getWindow().setStatusBarColor(getResources().getColor(StyleManager.getThemeColor(id)));

        changeViewsColor();

        titleView.setText(titleTemp);
        contentView.setText(textTemp);

    }

    // switch colors and themes
    private void changeViewsColor(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        titleView = findViewById(R.id.note_title_view);
        titleView.setText(note.getTitle());
        titleView.setTextColor(getResources().getColor(StyleManager.getThemeColorDark(note.getColor())));
        titleView.setHintTextColor(getResources().getColor(StyleManager.getThemeColorLight(note.getColor())));

        titleCharacterCount = findViewById(R.id.note_title_characters);
        String m = titleView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.title_max_length);
        titleCharacterCount.setText(m);
        titleCharacterCount.setTextColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));

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
        contentView.setTextColor(getResources().getColor(StyleManager.getThemeColorDarker(note.getColor())));
        contentView.setHintTextColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        contentView.setScroller(new Scroller(getApplicationContext()));
        contentView.setVerticalScrollBarEnabled(true);
        contentView.setMovementMethod(new ScrollingMovementMethod());

        contentCharacterCount = findViewById(R.id.note_content_characters);
        contentCharacterCount.setTextColor(getResources().getColor(StyleManager.getThemeColor(note.getColor())));
        m = contentView.getText().toString().trim().length()+ getString(R.string.text_divider)+ getResources().getInteger(R.integer.content_max_length);
        contentCharacterCount.setText(m);

        contentView.setOnKeyListener((view, i, keyEvent) -> {

            if (i == KeyEvent.KEYCODE_DEL){
                if (!contentView.getText().toString().isEmpty()){
                    if (Statics.SPECIAL_STRINGS.contains(contentView.getText().toString().charAt(contentView.getText().toString().length() - 1))){
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

        colorView = findViewById(R.id.note_color_view);
        colorView.setOnClickListener(view -> buildColorPickDialog());
        colorView.setBackgroundResource(StyleManager.getBackground(note.getColor()));

    }

    // check if the list is old or not
    // if old return true
    // else   return false
    private boolean isNoteOld(){
        for (String n: MySharedPreferences.LoadStringArrayToSharedPreferences(Statics.KEY_NOTE_LIST,getApplicationContext())){
            if (n.equals(note.getUid())) return true;
        }
        return false;
    }

    // build the color picker dialog
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,note.getColor());

        fragment.show(getSupportFragmentManager(),Statics.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                note.setColor(position);
                switchTheme(position);
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

    // save note when the user click on the save button
    // in the toolbar
    private boolean saveTextNote(){

        if (titleView.getText().toString().trim().length() < Statics.NOTE_TITLE_MINIMUM_LENGTH){

            Statics.StyleableToast(getApplicationContext(),
                    getString(R.string.title_short),
                    StyleManager.getThemeColorDark(note.getColor()),
                    R.color.white,
                    3,
                    StyleManager.getThemeColorDark(note.getColor()),
                    true);
        }
        else {

            note.setTitle(titleView.getText().toString().trim());
            note.setContent(contentView.getText().toString().trim());
            note.save(getApplicationContext());

            if (!isNoteOld()){

                Log.d("DEBUG_SAVE","Note is old !");

                ArrayList<String> noteList =
                        MySharedPreferences.LoadStringArrayToSharedPreferences(
                                Statics.KEY_NOTE_LIST,getApplicationContext()
                        );

                noteList.add(note.getUid());
                MySharedPreferences.SaveStringArrayToSharedPreferences(
                        noteList, Statics.KEY_NOTE_LIST,getApplicationContext()
                );
            }

            Statics.StyleableToast(getApplicationContext(),
                    getString(R.string.save_success),
                    StyleManager.getThemeColorDark(note.getColor()),
                    R.color.white,
                    3,
                    StyleManager.getThemeColorDark(note.getColor()),
                    false);
        }

        return true;

    }

    // update Undo stack when text changes
    private void myUpdateUndo(char lastCharacter){

        if (deletedSpecialCharacter){

            textUndoRedoHandler.pushUndo(contentView.getText().toString().trim());
            deletedSpecialCharacter = false;

            Log.d("UNDO_REDO","Last character: "+lastCharacter);
            Log.d("UNDO_REDO","Elements in Undo Stack: "+textUndoRedoHandler.getUndoStack().size());
            return;
        }

        if (Statics.SPECIAL_STRINGS.contains(lastCharacter)){

                textUndoRedoHandler.pushUndo(contentView.getText().toString().trim());
        }

        Log.d("UNDO_REDO","Last character: "+lastCharacter);
        Log.d("UNDO_REDO","Elements in Undo Stack: "+textUndoRedoHandler.getUndoStack().size());
    }

    // perform undo action
    // when the button is clicked
    private boolean undo(){

        if (!textUndoRedoHandler.getUndoStack().empty()) {
            if (textUndoRedoHandler.getUndoStack().size() == 1){

                if (deleteRedoStack) {
                    textUndoRedoHandler.resetRedo();
                    deleteRedoStack = true;
                }

                textUndoRedoHandler.pushRedo(contentView.getText().toString().trim());
                contentView.setText(textUndoRedoHandler.getUndoStack().firstElement());
                Statics.StyleableToast(getApplicationContext(),
                        getString(R.string.nothing_to_undo),
                        StyleManager.getThemeColorDark(note.getColor()),
                        R.color.white,
                        3,
                        StyleManager.getThemeColorDark(note.getColor()),
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

    // perform redo action
    // when the button is clicked
    private boolean redo(){


        Log.d("UNDO_REDO","Elements in Redo Stack: "+textUndoRedoHandler.getRedoStack().size());

        if (!textUndoRedoHandler.getRedoStack().empty()){
            contentView.setText(textUndoRedoHandler.popRedo());
            deleteRedoStack = false;
        }
        else {
            Statics.StyleableToast(getApplicationContext(),
                    getString(R.string.nothing_to_redo),
                    StyleManager.getThemeColorDark(note.getColor()),
                    R.color.white,
                    3,
                    StyleManager.getThemeColorDark(note.getColor()),
                    false);
        }

        return true;
    }

}