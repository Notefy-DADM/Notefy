package io.github.notefydadm.notefy.View.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import io.github.notefydadm.notefy.Adapter.NoteListAdapter;
import io.github.notefydadm.notefy.Model.Note;
import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.View.Fragments.NoteListFragment;
import io.github.notefydadm.notefy.View.Fragments.NoteTextFragment;
import io.github.notefydadm.notefy.ViewModel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    NoteListFragment noteListFragment;
    NoteTextFragment noteTextFragment;

    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        //LiveData<List<Note>> notesData = noteViewModel.getNotes();
        LiveData<List<Note>> notesData = noteViewModel.getAllNotes();
        //first note as default selected note
        noteViewModel.setSelectedNote(notesData.getValue().get(0));

        noteListFragment = new NoteListFragment();

        //  if we're being restored from a previous state
        //  we don't need to do anything.
        if (savedInstanceState != null) {
            //return;
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //  landscape
        }
        else{
            //  portrait
            //  If the activity was started with special instructions from an
            //  Intent, pass the Intent's extras to the fragment as arguments
            noteListFragment.setArguments(getIntent().getExtras());
            //  Replace the Fragment on the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteListFragment).addToBackStack(null).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()){
            case R.id.nav_about:
                intent = new Intent(this, AboutActivity.class);
                break;
        }
        startActivity(intent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //  Close drawer if it's on the screen
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        //  Drawer not opened
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_toolbar:
                System.out.println("Add item selected");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public NoteViewModel getNoteViewModel() {
        return this.noteViewModel;
    }
}
