package com.example.colornoteplus;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StyleManager {

    private static final ArrayList<BackgroundColor> backgroundColors = new ArrayList<>(Arrays.asList(
            new BackgroundColor(R.drawable.item_default_style,false),
            new BackgroundColor(R.drawable.item_blue_style,false),
            new BackgroundColor(R.drawable.item_green_style,false),
            new BackgroundColor(R.drawable.item_grey_style,false),
            new BackgroundColor(R.drawable.item_red_style,false)
    ));

    public static ArrayList<BackgroundColor> getBackgroundColors(){ return backgroundColors; }

    public static int getBackground(int index){ return backgroundColors.get(index).getDrawable(); }
}
