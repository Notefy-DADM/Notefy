package io.github.notefydadm.notefy.Model;

public class TextBlock implements Block {
    private String text;
    private String fontFamily;
    private String fontSize;

    public TextBlock(String text) {
        this(text, "Arial", "12");
    }

    public TextBlock(String text, String fontFamily, String fontSize) {
        this.text = text;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
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

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String getContent() {
        return this.getText();
    }
}
