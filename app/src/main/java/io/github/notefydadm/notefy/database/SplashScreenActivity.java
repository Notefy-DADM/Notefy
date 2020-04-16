package io.github.notefydadm.notefy.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.view.autentication.AuthenticationActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initAll();
    }

    private void initAll(){


        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            openLogin();
        }else{
            openMain();
            SingletonDatabase.getInstance().init();
        }
    }

    private void openLogin(){
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }

    private void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
