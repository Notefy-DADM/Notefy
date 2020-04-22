package io.github.notefydadm.notefy.view.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.view.fragments.AboutFragment;
import io.github.notefydadm.notefy.view.fragments.NoteFragment;
import io.github.notefydadm.notefy.view.fragments.NoteListFragment;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NoteListFragment.ChangeToTextEditor {

    private DrawerLayout drawer;

    public Menu toolbarMenu;

    NoteListFragment noteListFragment;
    NoteFragment noteFragment;

    NoteViewModel noteViewModel;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setToolbarDrawer();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteListFragment = new NoteListFragment();
        noteFragment = new NoteFragment();

        //  if we're being restored from a previous state
        //  we don't need to do anything.
        if (savedInstanceState != null) {
            //return;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //  landscape
            //  first note as default selected note
            noteListFragment.setArguments(getIntent().getExtras());
            //noteFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragNoteList, noteListFragment).commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragNoteText, noteFragment).addToBackStack(null).commit();
        } else {
            //  portrait
            //  If the activity was started with special instructions from an
            //  Intent, pass the Intent's extras to the fragment as arguments
            noteListFragment.setArguments(getIntent().getExtras());
            //  Replace the Fragment on the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteListFragment).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_notes:
                openNotesFragment();
                break;

            case R.id.nav_about:
                openAboutFragment();
                break;
        }
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
        this.toolbarMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_toolbar:
                System.out.println("Add item selected");
                noteViewModel.setSelectedNote(new Note());
                MainActivity.this.changeToTextEditor();
                break;
            case R.id.save_toolbar:
                noteFragment.saveNote();
                Toast.makeText(this,"Saved", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToolbarDrawer(){
        toolbar = findViewById(R.id.toolbar);
        //currentToolbarLayout = R.id.toolbar;
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void changeToTextEditor() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteFragment).addToBackStack(null).commit();
    }

    private void openAboutFragment(){
        AboutFragment fragment = new AboutFragment();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //  landscape
            //  first note as default selected note
            noteListFragment.setArguments(getIntent().getExtras());
            //noteFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragNoteList, fragment).commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragNoteText, noteFragment).addToBackStack(null).commit();
        } else {
            //  portrait
            //  If the activity was started with special instructions from an
            //  Intent, pass the Intent's extras to the fragment as arguments
            noteListFragment.setArguments(getIntent().getExtras());
            //  Replace the Fragment on the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, fragment).commit();
        }
    }

    private void openNotesFragment(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //  landscape
            //  first note as default selected note
            noteListFragment.setArguments(getIntent().getExtras());
            //noteFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragNoteList, noteListFragment).commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragNoteText, noteFragment).addToBackStack(null).commit();
        } else {
            //  portrait
            //  If the activity was started with special instructions from an
            //  Intent, pass the Intent's extras to the fragment as arguments
            noteListFragment.setArguments(getIntent().getExtras());
            //  Replace the Fragment on the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteListFragment).commit();
        }
    }


    /*private void switchToolbar(String currentFragment){
        if(currentFragment == ){
            return;
        }
        currentToolbarLayout = layout;
        View v = getLayoutInflater().inflate(layout,null);
        toolbar.removeAllViews();
        toolbar.addView(v);
    }*/

}
