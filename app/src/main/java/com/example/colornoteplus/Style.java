package com.example.colornoteplus;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Style extends AppCompatActivity {

    private static final ArrayList<ColorTheme> themes = new ArrayList<>(Arrays.asList(

            // Default Color : Orange Yellow
            new ColorTheme(
                    R.drawable.item_default_style,
                    R.drawable.item_default_light,
                    R.drawable.item_default_dark,
                    R.style.ThemeOrangeYellow,
                    R.style.ThemeOrangeYellowDark,
                    R.color.orange_yellow_crayola_lighter,
                    R.color.orange_yellow_crayola_light,
                    R.color.orange_yellow_crayola,
                    R.color.orange_yellow_crayola_dark,
                    R.color.orange_yellow_crayola_darker),

            // Blue : Liberty Blue
            new ColorTheme(
                    R.drawable.item_blue_style,
                    R.drawable.item_blue_light,
                    R.drawable.item_blue_dark,
                    R.style.ThemeBlueLiberty,
                    R.style.ThemeBlueLibertyDark,
                    R.color.blue_liberty_lighter,
                    R.color.blue_liberty_light,
                    R.color.blue_liberty,
                    R.color.blue_liberty_dark,
                    R.color.blue_liberty_darker),

            // Green
            new ColorTheme(
                    R.drawable.item_green_style,
                    R.drawable.item_green_light,
                    R.drawable.item_green_dark,
                    R.style.ThemeGreen,
                    R.style.ThemeGreenDark,
                    R.color.green_lighter,
                    R.color.green_light,
                    R.color.green,
                    R.color.green_dark,
                    R.color.green_darker),

            // Grey : Silver to Black
            new ColorTheme(
                    R.drawable.item_grey_style,
                    R.drawable.item_grey_light,
                    R.drawable.item_grey_dark,
                    R.style.ThemeGrey,
                    R.style.ThemeGreyDark,
                    R.color.grey_lighter_platinum,
                    R.color.grey_light_silver,
                    R.color.grey_spanish,
                    R.color.grey_dark_sonic_silver,
                    R.color.grey_darker_davys),

            // Red : Imperial Red
            new ColorTheme(
                    R.drawable.item_red_style,
                    R.drawable.item_red_light,
                    R.drawable.item_red_dark,
                    R.style.ThemeRedImperial,
                    R.style.ThemeRedImperialDark,
                    R.color.red_imperial_lighter,
                    R.color.red_imperial_light,
                    R.color.red_imperial,
                    R.color.red_imperial_dark,
                    R.color.red_imperial_darker),

            // Pink
            new ColorTheme(
                    R.drawable.item_pink_style,
                    R.drawable.item_pink_light,
                    R.drawable.item_pink_dark,
                    R.style.ThemePink,
                    R.style.ThemePinkDark,
                    R.color.pink_lighter,
                    R.color.pink_light,
                    R.color.pink,
                    R.color.pink_dark,
                    R.color.pink_darker),

            // Orange
            new ColorTheme(
                    R.drawable.item_orange_style,
                    R.drawable.item_orange_light,
                    R.drawable.item_orange_dark,
                    R.style.ThemeOrange,
                    R.style.ThemeOrangeDark,
                    R.color.orange_lighter,
                    R.color.orange_light,
                    R.color.orange,
                    R.color.orange_dark,
                    R.color.orange_darker
                    ),

            // Green Charcoal
            new ColorTheme(
                    R.drawable.item_charcoal_style,
                    R.drawable.item_charcoal_light,
                    R.drawable.item_charcoal_dark,
                    R.style.ThemeCharcoal,
                    R.style.ThemeCharcoalDark,
                    R.color.green_charcoal_lighter,
                    R.color.green_charcoal_light,
                    R.color.green_charcoal,
                    R.color.green_charcoal_dark,
                    R.color.green_charcoal_darker
            ),

            // Cyan
            new ColorTheme(
                    R.drawable.item_cyan_style,
                    R.drawable.item_cyan_light,
                    R.drawable.item_cyan_dark,
                    R.style.ThemeCyan,
                    R.style.ThemeCyanDark,
                    R.color.cyan_lighter,
                    R.color.cyan_light,
                    R.color.cyan,
                    R.color.cyan_dark,
                    R.color.cyan_darker
            ),

            // Red Bordeaux
            new ColorTheme(
                    R.drawable.item_red_bordeaux_style,
                    R.drawable.item_red_bordeaux_light,
                    R.drawable.item_red_bordeaux_dark,
                    R.style.ThemeRedBordeaux,
                    R.style.ThemeRedBordeauxDark,
                    R.color.red_bordeaux_lighter,
                    R.color.red_bordeaux_light,
                    R.color.red_bordeaux,
                    R.color.red_bordeaux_dark,
                    R.color.red_bordeaux_darker
            ),

            // Purple
            new ColorTheme(
            R.drawable.item_purple_style,
            R.drawable.item_purple_light,
            R.drawable.item_purple_dark,
            R.style.ThemePurple,
            R.style.ThemePurpleDark,
            R.color.purple_lighter,
            R.color.purple_light,
            R.color.purple,
            R.color.purple_dark,
            R.color.purple_darker
            )
    ));

    public static ArrayList<ColorTheme> getThemes(){
        return themes;
    }

    public enum BACKGROUND { LIGHT, NORMAL, DARK }

    public enum COLORS { LIGHTER, LIGHT, NORMAL, DARK, DARKER }

    public static int getCustomBackground(Context context, int index, BACKGROUND lightThemeBackground, BACKGROUND darkThemeBackground){

        if (getAppTheme(context) == 0){
            switch (lightThemeBackground){
                case LIGHT:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getDrawableLight() : themes.get(0).getDrawableLight();
                case NORMAL:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getDrawable() : themes.get(0).getDrawable();
                case DARK:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getDrawableDark() : themes.get(0).getDrawableDark();
            }
        }

        else
            switch (darkThemeBackground){
                case LIGHT:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getDrawableLight() : themes.get(0).getDrawableLight();
                case NORMAL:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getDrawable() : themes.get(0).getDrawable();
                case DARK:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getDrawableDark() : themes.get(0).getDrawableDark();
            }

        return themes.get(0).getDrawable();
    }

    public static int getBackground(Context context, int index){

            return index > 0 && index < themes.size() ?
                    themes.get(index).getDrawable() : themes.get(0).getDrawable();
    }

    public static int getBackgroundSecondary(Context context,int index){

        if (getAppTheme(context) == 0)
            return index > 0 && index < themes.size() ?
                themes.get(index).getDrawableLight() : themes.get(0).getDrawableLight();
        else
            return index > 0 && index < themes.size() ?
                    themes.get(index).getDrawableDark() : themes.get(0).getDrawableDark();
    }

    public static int getBackgroundPrimary(Context context,int index){
        if (getAppTheme(context) == 0)
            return index > 0 && index < themes.size() ?
                themes.get(index).getDrawableDark() : themes.get(0).getDrawableDark();
        else
            return index > 0 && index < themes.size() ?
                themes.get(index).getDrawableLight() : themes.get(0).getDrawableLight();

    }

    public static int getTheme(Context context, int index){

        if (getAppTheme(context) == 0){
            return index > 0 && index < themes.size() ?
                    themes.get(index).getTheme() : themes.get(0).getTheme();
        }
        else
            return index > 0 && index < themes.size() ?
                themes.get(index).getThemeDark() : themes.get(0).getThemeDark();
    }

    public static int getCustomColor(Context context, int index, COLORS lightThemeColor, COLORS darkThemeColor){

        if (getAppTheme(context) == 0){

            switch (lightThemeColor){
                case LIGHTER:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorLighter() : themes.get(0).getColorLighter();
                case LIGHT:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorLight() : themes.get(0).getColorLight();
                case NORMAL:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColor() : themes.get(0).getColor();
                case DARK:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorDark() : themes.get(0).getColorDark();
                case DARKER:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorDarker() : themes.get(0).getColorDarker();
            }
        }

        else
            switch (darkThemeColor){
                case LIGHTER:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorLighter() : themes.get(0).getColorLighter();
                case LIGHT:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorLight() : themes.get(0).getColorLight();
                case NORMAL:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColor() : themes.get(0).getColor();
                case DARK:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorDark() : themes.get(0).getColorDark();
                case DARKER:
                    return index > 0 && index < themes.size() ?
                            themes.get(index).getColorDarker() : themes.get(0).getColorDarker();
            }

        return themes.get(0).getColor();
    }

    public static int getColorMain(Context context, int index){

            return index > 0 && index < themes.size() ?
                themes.get(index).getColor() : themes.get(0).getColor();

    }

    public static int getColorSecondary(Context context, int index){
        if (getAppTheme(context) == 0)
            return index > 0 && index < themes.size() ?
                themes.get(index).getColorLight() : themes.get(0).getColorLight();
        else
            return index > 0 && index < themes.size() ?
                    themes.get(index).getColorDark() : themes.get(0).getColorDark();

    }

    public static int getColorSecondaryAccent(Context context, int index){

        if (getAppTheme(context) == 0)
            return index > 0 && index < themes.size() ?
                themes.get(index).getColorLighter() : themes.get(0).getColorLighter();
        else
            return index > 0 && index < themes.size() ?
                    themes.get(index).getColorDarker() : themes.get(0).getColorDarker();
    }

    public static int getColorPrimary(Context context, int index){

        if (getAppTheme(context) == 0)
            return index > 0 && index < themes.size() ?
                themes.get(index).getColorDark() : themes.get(0).getColorDarker();

        else
            return index > 0 && index < themes.size() ?
                    themes.get(index).getColorLight() : themes.get(0).getColorLight();
    }

    public static int getColorPrimaryAccent(Context context, int index){

        if (getAppTheme(context) == 0)
        return index > 0 && index < themes.size() ?
                themes.get(index).getColorDarker() : themes.get(0).getColorDarker();

        else
            return index > 0 && index < themes.size() ?
                    themes.get(index).getColorLighter() : themes.get(0).getColorLighter();
    }

    public static int getNeutralColor(Context context){

        if (getAppTheme(context) == 0)
            return R.color.white;
        else
            return R.color.grey_darkest;

    }

    public static int getNeutralTextColor(Context context){

        if (getAppTheme(context) == 0)
            return R.color.grey_darkest;
        else
            return R.color.white;

    }

    public static int getAppTheme(Context context){
        return DatabaseManager.LoadInteger(App.KEY_LIGHT_THEME,context);
    }

    public static void setAppTheme(int theme, Context context){
        DatabaseManager.SaveInteger(theme, App.KEY_LIGHT_THEME,context);
        DatabaseManager.setDatabaseLastModificationDate(context);
    }

    public static int getAppColor(Context context){
        return DatabaseManager.LoadInteger(App.KEY_APP_COLOR,context);
    }

    public static void setAppColor(int theme, Context context){
        DatabaseManager.SaveInteger(theme, App.KEY_APP_COLOR,context);
        DatabaseManager.setDatabaseLastModificationDate(context);
    }
}