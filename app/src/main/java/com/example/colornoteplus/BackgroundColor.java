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

        // the list of the colors
        private final int colorLighter;
        private final int colorLight;
        private final int color;
        private final int colorDark;
        private final int colorDarker;


    // default constructor
    public BackgroundColor(){
            this.drawable = R.drawable.item_default_style;
            this.drawableLight = R.drawable.item_default_light;
            this.drawableDark = R.drawable.item_default_dark;
            this.theme = R.style.Theme_ColorNotePlus;
            this.colorLighter = R.color.orange_yellow_crayola_lighter;
            this.colorLight = R.color.orange_yellow_crayola_light;
            this.color = R.color.orange_yellow_crayola;
            this.colorDark = R.color.orange_yellow_crayola_dark;
            this.colorDarker = R.color.orange_yellow_crayola_darker;
    }

    // custom constructor
    public BackgroundColor(int drawable,int drawableLight,int drawableDark,int theme,int colorLighter,int colorLight,int color,int colorDark,int colorDarker){
        this.drawable = drawable;
        this.drawableLight = drawableLight;
        this.drawableDark = drawableDark;
        this.theme = theme;
        this.colorLighter = colorLighter;
        this.colorLight = colorLight;
        this.color = color;
        this.colorDark = colorDark;
        this.colorDarker = colorDarker;
    }

    // getter for the theme
    public int getTheme() { return theme; }

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