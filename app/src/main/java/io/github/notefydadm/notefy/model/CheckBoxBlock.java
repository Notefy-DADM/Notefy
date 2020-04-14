package io.github.notefydadm.notefy.model;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckBoxBlock extends TextBlock {
    private static final Pattern CheckBoxPattern = Pattern.compile("\\s*?- \\[([\\sx])] (.*)");

    public static boolean matches(CharSequence input) {
        return CheckBoxPattern.matcher(input).matches();
    }

    @Nullable
    public static CheckBoxBlock fromLine(CharSequence line) {
        final Matcher checkBoxMatcher = CheckBoxPattern.matcher(line);
        if (checkBoxMatcher.matches()) {
            String checkMark = checkBoxMatcher.group(1);
            String text = checkBoxMatcher.group(2);
            // The regex ensures that there is only either a whitespace or an 'x'
            boolean isChecked = !(checkMark == null || checkMark.trim().isEmpty());
            return new CheckBoxBlock(text, isChecked);
        } else {
            return null;
        }
    }

    private boolean isChecked;

    public CheckBoxBlock(String text, boolean isChecked) {
        super(text);
        this.isChecked = isChecked;
    }

    public CheckBoxBlock(String text, boolean isChecked, String fontFamily, float fontSize, String textStyle) {
        super(text, fontFamily, fontSize, textStyle);
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String getContent() {
        return "- [" + (isChecked ? "x" : " ") + "] " + super.getContent();
    }
}
