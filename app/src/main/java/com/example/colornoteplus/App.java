package com.example.colornoteplus;

import android.content.Context;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains necessary data and setting values for the app to function correctly.
 * @see User
 * @see Style
 * @see DatabaseManager
 * @see Sync
 */
public abstract class App {

    // -----------------------------------------------------------------------------------------------------------------
    //                                          DEFAULT KEYS & TAGS
    // -----------------------------------------------------------------------------------------------------------------

    public final static String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    public final static String KEY_NOTE_ACTIVITY = "LOAD_NOTE";
    public final static String KEY_NOTE_LIST = "NOTE_LIST";
    public final static String KEY_NOTE_LIST_TRASH = "NOTE_LIST_TRASH";
    public final static String KEY_AUTO_SYNC = "AUTO_SYNC";
    public final static String KEY_DATABASE_LAST_UPDATE = "DATABASE_LAST_UPDATE";
    public final static String KEY_LIGHT_THEME = "LIGHT_THEME";
    public final static String KEY_APP_COLOR = "APP_COLOR";
    public final static String KEY_CURRENT_USER = "CURRENT_USER";
    public final static String KEY_REMEMBER_ME = "REMEMBER_ME";
    public final static String KEY_PATCH_NOTES = "PATCH_NOTES";
    public final static String TAG_FRAGMENT_COLOR_PICK = "PICK_COLOR";
    public final static String TAG_FRAGMENT_ADD_CHECK_LIST_ITEM = "ADD_CHECK_LIST_ITEM";
    public final static String TAG_FRAGMENT_DATE_PICKER = "DATE_PICKER";
    public final static String TAG_DIALOG_CONFIRM = "CONFIRM_DIALOG";
    public final static ArrayList<Character> SPECIAL_STRINGS = new ArrayList<>(Arrays.asList(' ',' '));
    public enum SORT_ITEM { ALPHA , STATUS , CREATION , MODIFICATION , DUE , PRIORITY  }
    public enum SORT_NOTE { ALPHA , CREATION, MODIFICATION , COLOR}

    // -----------------------------------------------------------------------------------------------------------------
    //                                          DEFAULT VALUES
    // -----------------------------------------------------------------------------------------------------------------

    public final static int NOTE_TITLE_MINIMUM_LENGTH = 3;
    public final static int DAY_THEME = 0;
    public final static int NIGHT_THEME = 1;
    public final static String NOTE_PLACEHOLDER = "New Note";
    public final static int NOTE_DEFAULT_COLOR = 0;
    public final static String NOTE_DEFAULT_UID = "NEW_NOTE";
    public final static int DEFAULT_LIGHT_THEME = 0;
    public final static int DEFAULT_COLOR_THEME = 0;
    public final static String NOTE_TEXT_ID = "T";
    public final static String NOTE_CHECK_ID = "C";

    // -----------------------------------------------------------------------------------------------------------------
    //                                          FIREBASE DATABASE KEYS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @deprecated
     * */
    public final static String DATABASE_URL = "https://color-noter-default-rtdb.europe-west1.firebasedatabase.app/";

    public final static String TEST_USER = "test_user";
    public final static String DATABASE_DATA_INFO = "db_info";
    public final static String DATABASE_DATA_APP_VERSION = "app_version";
    public final static String DATABASE_DATA_UPDATE_NOTES = "update_notes";
    public final static String DATABASE_DATA_USERS = "users";
    public final static String DATABASE_USER_INFO = "user_info";
    public final static String DATABASE_USER_ID = "id";
    public final static String DATABASE_USER_PASSWORD = "password";
    public final static String DATABASE_USER_EMAIL = "email";
    public final static String DATABASE_USER_COLOR = "user_color";
    public final static String DATABASE_USER_THEME = "user_theme";
    public final static String DATABASE_USER_INFO_LAST_SYNC = "last_sync";
    public final static String DATABASE_USER_NOTES = "user_notes";
    public final static String DATABASE_OBJECT_UID = "uid";
    public final static String DATABASE_OBJECT_CREATION_DATE = "creationDate";
    public final static String DATABASE_OBJECT_MODIFICATION_DATE = "modificationDate";
    public final static String DATABASE_NOTE_TITLE = "title";
    public final static String DATABASE_NOTE_COLOR = "color";
    public final static String DATABASE_NOTE_CONTENT = "content";
    public final static String DATABASE_CL_ITEM_DESCRIPTION = "description";
    public final static String DATABASE_CL_ITEM_DONE_DATE = "doneDate";
    public final static String DATABASE_CL_ITEM_DUE_DATE = "dueDate";
    public final static String DATABASE_CL_ITEM_PRIORITY = "priority";

    // -----------------------------------------------------------------------------------------------------------------
    //                                          CUSTOM METHODS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Display a customized styleable Toast.
     * @see StyleableToast
     * @param context context in which the app is created
     * @param text text to be displayed
     * @param textColor color of the text
     * @param bgColor background color of the toast
     * @param strokeWidth stroke of the toast
     * @param strokeColor stroke color
     * @param isLong display toast for a long time or not
     */
    public static void StyleableToast(Context context, String text, int textColor, int bgColor, int strokeWidth, int strokeColor,boolean isLong){

        new StyleableToast
                .Builder(context)
                .text(text)
                .textColor(context.getResources().getColor(textColor))
                .backgroundColor(context.getResources().getColor(bgColor))
                .stroke(strokeWidth, context.getResources().getColor(textColor))
                .length(isLong ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();
    }

    /**
     * @deprecated
     * Convert a string to the appropriate priority
     * @see CheckListItem.PRIORITY
     * @see CheckListItem
     * @param context the context in which the function is called
     * @param string string to be converted
     * @return priority
     */
    public static CheckListItem.PRIORITY stringToPriority(Context context, String string){

        final String
                low = context.getString(R.string.low_priority),
                medium = context.getString(R.string.medium_priority),
                high = context.getString(R.string.high_priority),
                urgent = context.getString(R.string.urgent_priority);

        if (string.equals(low)) return CheckListItem.PRIORITY.LOW;
        if (string.equals(medium)) return CheckListItem.PRIORITY.MEDIUM;
        if (string.equals(high)) return CheckListItem.PRIORITY.HIGH;
        if (string.equals(urgent)) return CheckListItem.PRIORITY.URGENT;

        return CheckListItem.PRIORITY.MEDIUM;
    }

    /**
     * Return if a given note exists by UID in a given note list.
     * @param note given note
     * @param list given list
     * @return true if uid exists, false otherwise
     */
    public static boolean isNoteByUidInList(Note<?> note, ArrayList<Note<?>> list){

        for (Note<?> n : list){
            if (n.getUid().equals(note.getUid())) return true;
        }

        return false;

    }

    /**
     * @deprecated
     * return the index of a note (if it exists) by its id
     * @param note given note
     * @param list given list
     * @return index of the note in question
     */
    public static int getNoteIndexByUidInList(Note<?> note, ArrayList<Note<?>> list){

        if (!isNoteByUidInList(note,list)) return -1;

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getUid().equals(note.getUid())) return i;
        }

        return -1;
    }

    /**
     * Return a list of notes as an Array list of Strings of their UID
     * @param list array list of notes
     * @return Array List of Strings
     */
    public static ArrayList<String> getNotesAsUIDFromList(ArrayList<Note<?>> list){

        ArrayList<String> uidList = new ArrayList<>();

        for (Note<?> note : list){
            uidList.add(note.getUid());
        }

        return uidList;
    }

    /**
     * return the result of the comparison of two Array Lists of strings
     * @param listOne first array list
     * @param listTwo second array list
     * @return the result of comparison of listOne & listTwo
     */
    public static boolean stringArrayEqualStringArray(ArrayList<String> listOne, ArrayList<String> listTwo){

        if (listOne.size() != listTwo.size()) return false;

        for (int i = 0; i < listOne.size(); i++){
            if (!listOne.get(i).equals(listTwo.get(i))) return false;
        }

        return true;
    }
}