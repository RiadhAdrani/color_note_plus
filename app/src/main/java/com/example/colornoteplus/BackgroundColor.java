package com.example.colornoteplus;

public class BackgroundColor {

    private final int drawable;
        public int getDrawable() { return drawable; }

    private final int theme;
        private final int colorLighter;
        private final int colorLight;
        private final int color;
        private final int colorDark;
        private final int colorDarker;

    private final boolean isDark;
        public boolean isDark() { return isDark; }

    public BackgroundColor(){
            this.drawable = R.drawable.item_default_style;
            this.theme = R.style.Theme_ColorNotePlus;
            this.colorLighter = R.color.orange_yellow_crayola_lighter;
            this.colorLight = R.color.orange_yellow_crayola_light;
            this.color = R.color.orange_yellow_crayola;
            this.colorDark = R.color.orange_yellow_crayola_dark;
            this.colorDarker = R.color.orange_yellow_crayola_darker;
            this.isDark = false;
    }

    public BackgroundColor(int drawable,boolean isDark){
        this.drawable = drawable;
        this.isDark = isDark;
        this.theme = R.style.Theme_ColorNotePlus;
        this.colorLighter = R.color.orange_yellow_crayola_lighter;
        this.colorLight = R.color.orange_yellow_crayola_light;
        this.color = R.color.orange_yellow_crayola;
        this.colorDark = R.color.orange_yellow_crayola_dark;
        this.colorDarker = R.color.orange_yellow_crayola_darker;
    }

    public BackgroundColor(int drawable,int theme,boolean isDark,int colorLighter,int colorLight,int color,int colorDark,int colorDarker){
        this.drawable = drawable;
        this.isDark = isDark;
        this.theme = theme;
        this.colorLighter = colorLighter;
        this.colorLight = colorLight;
        this.color = color;
        this.colorDark = colorDark;
        this.colorDarker = colorDarker;
    }

    public int getTheme() { return theme; }

    public int getColorLighter() { return colorLighter; }

    public int getColorLight() { return colorLight; }

    public int getColor() { return color; }

    public int getColorDark() { return colorDark; }

    public int getColorDarker() { return colorDarker; }

}