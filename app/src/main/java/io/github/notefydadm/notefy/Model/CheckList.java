package io.github.notefydadm.notefy.Model;

import java.util.ArrayList;

public class CheckList implements Block {
    private ArrayList<CheckListItem> items;

    public CheckList() {
        this.items = new ArrayList<>();
    }

    public ArrayList<CheckListItem> getItems() {
        return items;
    }
}
