package io.github.notefydadm.notefy.Model;

public class CheckBoxBlock extends TextBlock {
    private boolean isChecked;

    public CheckBoxBlock(String text, String fontFamily, String fontSize, boolean isChecked) {
        super(text, fontFamily, fontSize);
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
