package com.example.colornoteplus;

/**
 * Class describing a color theme object. contains variation of a single color with
 * the addition of themes (day and night) and background drawables.
 * @see App
 * @see DatabaseManager
 */
public class ColorTheme {

    /**
     * Standard drawable
     */
    private final int drawable;

    /**
     * getter of the standard drawable
     * @return drawable id
     */
    public int getDrawable() {
        return drawable;
    }

    /**
     * Light themed drawable
     */
    private final int drawableLight;

    /**
     * getter of the light themed drawable
     * @return drawable id
     */
    public int getDrawableLight() {
        return drawableLight;
    }

    /**
     * Dark themed drawable
     */
    private final int drawableDark;

    /**
     * getter for the dark themed drawable
     * @return drawable id
     */
    public int getDrawableDark() {
        return drawableDark;
    }

    /**
     * Day theme style id
     */
    private final int theme;

    /**
     * Night theme style id
     */
    private final int themeDark;

    /**
     * lighter color variation id
     */
    private final int colorLighter;

    /**
     * light color variation id
     */
    private final int colorLight;

    /**
     * main color id
     */
    private final int color;

    /**
     * dark color variation id
     */
    private final int colorDark;

    /**
     * darker color variation id
     */
    private final int colorDarker;

    /**
     * Color theme constructor
     * @param drawable standard drawable id
     * @param drawableLight light drawable id
     * @param drawableDark dark drawable id
     * @param theme theme style id in R.style
     * @param themeDark dark theme style id in R.style
     * @param colorLighter lighter color variation id in R.color
     * @param colorLight light color variation id in R.color
     * @param color main color id in R.color
     * @param colorDark dark color variation id in R.color
     * @param colorDarker darker color variation id in R.color
     */
    public ColorTheme(int drawable, int drawableLight, int drawableDark, int theme, int themeDark, int colorLighter, int colorLight, int color, int colorDark, int colorDarker){
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

    /**
     * getter of the current light theme
     * @return theme id
     */
    public int getTheme() { return theme; }

    /**
     * getter of the current dark theme
     * @return theme id
     */
    public int getThemeDark() {return themeDark; }

    /**
     * getter of the lighter color variation
     * @return color id
     */
    public int getColorLighter() { return colorLighter; }

    /**
     * getter of the light color variation
     * @return color id
     */
    public int getColorLight() { return colorLight; }

    /**
     * getter of the main color
     * @return color id
     */
    public int getColor() { return color; }

    /**
     * getter of the dark color variation
     * @return color id
     */
    public int getColorDark() { return colorDark; }

    /**
     * getter of the darker color variation
     * @return color id
     */
    public int getColorDarker() { return colorDarker; }

}