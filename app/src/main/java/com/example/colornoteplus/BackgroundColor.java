package com.example.colornoteplus;

public class BackgroundColor {

    private final int drawable;
        public int getDrawable() { return drawable; }

    private final int theme;

    private final boolean isDark;
        public boolean isDark() { return isDark; }

    public BackgroundColor(int drawable,boolean isDark){
        this.drawable = drawable;
        this.isDark = isDark;
        this.theme = R.style.Theme_ColorNotePlus;
    }

    public BackgroundColor(int drawable,int theme,boolean isDark){
        this.drawable = drawable;
        this.isDark = isDark;
        this.theme = theme;
    }

}
