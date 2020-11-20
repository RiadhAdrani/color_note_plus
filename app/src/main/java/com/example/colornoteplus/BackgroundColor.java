package com.example.colornoteplus;

public class BackgroundColor {

    private final int drawable;
        public int getDrawable() { return drawable; }

    private final boolean isDark;
        public boolean isDark() { return isDark; }

    public BackgroundColor(int drawable,boolean isDark){
        this.drawable = drawable;
        this.isDark = isDark;
    }

}
