package io.github.notefydadm.notefy.view.fragments.noteBlocks;

@FunctionalInterface
public interface TextWatcherOnTextChanged {
    void onTextChanged(CharSequence s, int start, int before, int count);
}
