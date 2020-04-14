package io.github.notefydadm.notefy.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.github.notefydadm.notefy.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    private void initAll(){
        SingletonDatabase.getInstance().init();
    }
}
