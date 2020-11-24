package com.example.colornoteplus;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StyleManager {

    private static final ArrayList<BackgroundColor> backgroundColors = new ArrayList<>(Arrays.asList(

            // Default Color : Orange Yellow
            new BackgroundColor(R.drawable.item_default_style,
                    R.style.ThemeOrangeYellow,
                    false,
                    R.color.orange_yellow_crayola_lighter,
                    R.color.orange_yellow_crayola_light,
                    R.color.orange_yellow_crayola,
                    R.color.orange_yellow_crayola_dark,
                    R.color.orange_yellow_crayola_darker),

            // Blue : Liberty Blue
            new BackgroundColor(R.drawable.item_blue_style,
                    R.style.ThemeBlueLiberty,
                    false,
                    R.color.blue_liberty_lighter,
                    R.color.blue_liberty_light,
                    R.color.blue_liberty,
                    R.color.blue_liberty_dark,
                    R.color.blue_liberty_darker),

            // Green
            new BackgroundColor(R.drawable.item_green_style,
                    R.style.ThemeGreen,
                    false,
                    R.color.green_lighter,
                    R.color.green_light,
                    R.color.green,
                    R.color.green_dark,
                    R.color.green_darker),

            // Grey : Silver to Black
            new BackgroundColor(R.drawable.item_grey_style,
                    R.style.ThemeGrey,
                    false,
                    R.color.grey_lighter_platinum,
                    R.color.grey_light_silver,
                    R.color.grey_spanish,
                    R.color.grey_dark_sonic_silver,
                    R.color.grey_darker_davys),

            // Red : Imperial Red
            new BackgroundColor(R.drawable.item_red_style,
                    R.style.ThemeRedImperial,
                    false,
                    R.color.red_imperial_lighter,
                    R.color.red_imperial_light,
                    R.color.red_imperial,
                    R.color.red_imperial_dark,
                    R.color.red_imperial_darker),

            // Pink
            new BackgroundColor(R.drawable.item_pink_style,
                    R.style.ThemePink,
                    false,
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
                backgroundColors.get(index).getDrawable() : 0;
    }

    public static int getTheme(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getTheme() : backgroundColors.get(0).getTheme();
    }

    public static int getThemeColor(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColor() : backgroundColors.get(0).getColor();
    }

    public static int getThemeColorLight(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorLight() : backgroundColors.get(0).getColorLight();
    }

    public static int getThemeColorLighter(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorLighter() : backgroundColors.get(0).getColorLighter();
    }

    public static int getThemeColorDark(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorDark() : backgroundColors.get(0).getColorDarker();
    }

    public static int getThemeColorDarker(int index){
        return index > 0 && index < backgroundColors.size() ?
                backgroundColors.get(index).getColorDarker() : backgroundColors.get(0).getColorDarker();
    }
}
