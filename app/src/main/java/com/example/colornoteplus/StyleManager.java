package com.example.colornoteplus;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StyleManager {

    private static final ArrayList<BackgroundColor> backgroundColors = new ArrayList<>(Arrays.asList(

            // Default Color : Orange Yellow
            new BackgroundColor(),

            // Blue : Liberty Blue
            new BackgroundColor(R.drawable.item_blue_style,
                    R.style.ThemeBlueLiberty,
                    false,
                    R.color.blue_liberty_lighter,
                    R.color.blue_liberty_light,
                    R.color.blue_liberty,
                    R.color.blue_liberty_dark,
                    R.color.blue_liberty_darker),

            // Green : Persian Green
            new BackgroundColor(R.drawable.item_green_style,false),

            // Grey : Silver to Black
            new BackgroundColor(R.drawable.item_grey_style,false),

            // Red : Imperial Red
            new BackgroundColor(R.drawable.item_red_style,false)
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
