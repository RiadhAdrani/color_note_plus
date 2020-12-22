package com.example.colornoteplus;

import android.content.Context;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Statics {

    public final static String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    public final static int NOTE_TITLE_MINIMUM_LENGTH = 3;

    public final static String NOTE_TEXT_ID = "T";
    public final static String NOTE_CHECK_ID = "C";

    public final static String KEY_NOTE_ACTIVITY = "LOAD_NOTE";
    public final static String KEY_NOTE_LIST = "NOTE_LIST";
    public final static String KEY_NOTE_LIST_TRASH = "NOTE_LIST_TRASH";
    public final static String KEY_LIGHT_THEME = "LIGHT_THEME";
    public final static String KEY_APP_COLOR = "APP_COLOR";

    public final static String TAG_FRAGMENT_COLOR_PICK = "PICK_COLOR";
    public final static String TAG_FRAGMENT_ADD_CHECK_LIST_ITEM = "ADD_CHECK_LIST_ITEM";
    public final static String TAG_FRAGMENT_DATE_PICKER = "DATE_PICKER";
    public final static String TAG_DIALOG_CONFIRM = "CONFIRM_DIALOG";

    public final static String NOTE_PLACEHOLDER = "New Note";
    public final static int NOTE_DEFAULT_COLOR = 0;
    public final static String NOTE_DEFAULT_UID = "NEW_NOTE";
    public final static int DEFAULT_LIGHT_THEME = 0;
    public final static int DEFAULT_COLOR_THEME = 0;

    public final static ArrayList<Character> SPECIAL_STRINGS = new ArrayList<>(Arrays.asList(
            ' '));

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

    public enum SORT_ITEM { ALPHA , STATUS , CREATION , MODIFICATION , DUE , PRIORITY  }

    public enum SORT_NOTE { ALPHA , CREATION, MODIFICATION , COLOR}
}
