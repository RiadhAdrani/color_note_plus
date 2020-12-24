package com.example.colornoteplus;

public class BackgroundColor {

    // default drawable
    private final int drawable;
        public int getDrawable() { return drawable; }

    // a lighter version of the default drawable
    private final int drawableLight;
        public int getDrawableLight() { return drawableLight;}

    // a darker version of the drawable
    private final int drawableDark;
        public int getDrawableDark() {return drawableDark;}

    // the representing theme
    private final int theme;

    // the dark theme
    private final int themeDark;

        // the list of the colors
        private final int colorLighter;
        private final int colorLight;
        private final int color;
        private final int colorDark;
        private final int colorDarker;

    // custom constructor
    public BackgroundColor(int drawable,int drawableLight,int drawableDark,int theme,int themeDark, int colorLighter,int colorLight,int color,int colorDark,int colorDarker){
        this.drawable = drawable;
        this.drawableLight = drawableLight;
        this.drawableDark = drawableDark;
        this.theme = theme;
        this.themeDark = themeDark;
        this.colorLighter = colorLighter;
        this.colorLight = colorLight;
        this.color = color;
        this.colorDark = colorDark;
        this.colorDarker = colorDarker;
    }

    // getter for the theme
    public int getTheme() { return theme; }

    public int getThemeDark() {return themeDark; }

    // getter for the lighter color
    public int getColorLighter() { return colorLighter; }

    // getter for the light color
    public int getColorLight() { return colorLight; }

    // getter for the main color
    public int getColor() { return color; }

    // getter for the dark color
    public int getColorDark() { return colorDark; }

    // getter for the darker color
    public int getColorDarker() { return colorDarker; }

}