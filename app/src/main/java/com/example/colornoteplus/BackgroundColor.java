package com.example.colornoteplus;

public class BackgroundColor {

    private final int drawable;
        public int getDrawable() { return drawable; }

    private final int drawableLight;
        public int getDrawableLight() { return drawableLight;}

    private final int drawableDark;
        public int getDrawableDark() {return drawableDark;}

    private final int theme;
        private final int colorLighter;
        private final int colorLight;
        private final int color;
        private final int colorDark;
        private final int colorDarker;


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

    public int getTheme() { return theme; }

    public int getColorLighter() { return colorLighter; }

    public int getColorLight() { return colorLight; }

    public int getColor() { return color; }

    public int getColorDark() { return colorDark; }

    public int getColorDarker() { return colorDarker; }

}