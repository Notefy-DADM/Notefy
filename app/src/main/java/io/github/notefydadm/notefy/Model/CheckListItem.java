package io.github.notefydadm.notefy.Model;

public class CheckListItem {
    private String text;
    private boolean isChecked;

    public CheckListItem(String text) {
        this(text, false);
    }

    public CheckListItem(String text, boolean isChecked) {
        this.text = text;
        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
