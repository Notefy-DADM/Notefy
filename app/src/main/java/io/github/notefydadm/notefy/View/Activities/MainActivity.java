package io.github.notefydadm.notefy.View.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.View.Fragments.NoteListFragment;
import io.github.notefydadm.notefy.View.Fragments.NoteTextFragment;

public class MainActivity extends AppCompatActivity {

    NoteListFragment noteListFragment;
    NoteTextFragment noteTextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteListFragment = new NoteListFragment();

        //  if we're being restored from a previous state
        //  we don't need to do anything.
        if (savedInstanceState != null) {
            return;
        }

        //  If the activity was started with special instructions from an
        //  Intent, pass the Intent's extras to the fragment as arguments
        noteListFragment.setArguments(getIntent().getExtras());
        //  Replace the Fragment on the 'fragmentContainer' FrameLayout
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteListFragment).addToBackStack(null).commit();
    }
}
