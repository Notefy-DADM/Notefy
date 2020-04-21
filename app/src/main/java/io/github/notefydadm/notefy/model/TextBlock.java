package io.github.notefydadm.notefy.model;

import androidx.annotation.Dimension;

public class TextBlock implements Block {
    private String text;
    private String fontFamily;
    private String textStyle;

    @Dimension(unit = Dimension.SP)
    private double fontSize;

    public TextBlock(String text) {
        this(text, "sans-serif", 18, "normal");
    }

    public TextBlock(String text, String fontFamily, double fontSize, String textStyle) {
        this.text = text;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.textStyle = textStyle;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    @Override
    public String getContent() {
        return this.getText();
    }


}
