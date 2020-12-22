package com.example.colornoteplus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StyleManager {

    private static final ArrayList<BackgroundColor> backgroundColors = new ArrayList<>(Arrays.asList(

            // Default Color : Orange Yellow
            new BackgroundColor(
                    R.drawable.item_default_style,
                    R.drawable.item_default_light,
                    R.drawable.item_default_dark,
                    R.style.ThemeOrangeYellow,
                    R.color.orange_yellow_crayola_lighter,
                    R.color.orange_yellow_crayola_light,
                    R.color.orange_yellow_crayola,
                    R.color.orange_yellow_crayola_dark,
                    R.color.orange_yellow_crayola_darker),

            // Blue : Liberty Blue
            new BackgroundColor(
                    R.drawable.item_blue_style,
                    R.drawable.item_blue_light,
                    R.drawable.item_blue_dark,
                    R.style.ThemeBlueLiberty,
                    R.color.blue_liberty_lighter,
                    R.color.blue_liberty_light,
                    R.color.blue_liberty,
                    R.color.blue_liberty_dark,
                    R.color.blue_liberty_darker),

            // Green
            new BackgroundColor(
                    R.drawable.item_green_style,
                    R.drawable.item_green_light,
                    R.drawable.item_green_dark,
                    R.style.ThemeGreen,
                    R.color.green_lighter,
                    R.color.green_light,
                    R.color.green,
                    R.color.green_dark,
                    R.color.green_darker),

            // Grey : Silver to Black
            new BackgroundColor(
                    R.drawable.item_grey_style,
                    R.drawable.item_grey_light,
                    R.drawable.item_grey_dark,
                    R.style.ThemeGrey,
                    R.color.grey_lighter_platinum,
                    R.color.grey_light_silver,
                    R.color.grey_spanish,
                    R.color.grey_dark_sonic_silver,
                    R.color.grey_darker_davys),

            // Red : Imperial Red
            new BackgroundColor(
                    R.drawable.item_red_style,
                    R.drawable.item_red_light,
                    R.drawable.item_red_dark,
                    R.style.ThemeRedImperial,
                    R.color.red_imperial_lighter,
                    R.color.red_imperial_light,
                    R.color.red_imperial,
                    R.color.red_imperial_dark,
                    R.color.red_imperial_darker),

            // Pink
            new BackgroundColor(
                    R.drawable.item_pink_style,
                    R.drawable.item_pink_light,
                    R.drawable.item_pink_dark,
                    R.style.ThemePink,
                    R.color.pink_lighter,
                    R.color.pink_light,
                    R.color.pink,
                    R.color.pink_dark,
                    R.color.pink_darker)
    ));

    public static ArrayList<BackgroundColor> getBackgroundColors(){
        return backgroundColors;
    }

    public static int getBackground(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getDrawable() : backgroundColors.get(0).getDrawable();
    }

    public static int getBackgroundLight(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getDrawableLight() : backgroundColors.get(0).getDrawableLight();
    }

    public static int getBackgroundDark(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getDrawableDark() : backgroundColors.get(0).getDrawableDark();
    }

    public static int getTheme(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getTheme() : backgroundColors.get(0).getTheme();
    }

    public static int getColorMain(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColor() : backgroundColors.get(0).getColor();
    }

    public static int getColorSecondary(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorLight() : backgroundColors.get(0).getColorLight();
    }

    public static int getColorSecondaryAccent(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorLighter() : backgroundColors.get(0).getColorLighter();
    }

    public static int getColorPrimary(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorDark() : backgroundColors.get(0).getColorDarker();
    }

    public static int getColorPrimaryAccent(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorDarker() : backgroundColors.get(0).getColorDarker();
    }

    public static int getNeutralColor(){
        return R.color.white;
    }

    public static int getLightTheme(Context context){
        return MySharedPreferences.LoadInteger(Statics.KEY_LIGHT_THEME,context);
    }

    public static void setLightTheme(int theme, Context context){
        MySharedPreferences.SaveInteger(theme,Statics.KEY_LIGHT_THEME,context);
    }

    public static int getAppColor(Context context){
        return MySharedPreferences.LoadInteger(Statics.KEY_APP_COLOR,context);
    }

    public static void setAppColor(int theme, Context context){
        MySharedPreferences.SaveInteger(theme,Statics.KEY_APP_COLOR,context);
    }
}
