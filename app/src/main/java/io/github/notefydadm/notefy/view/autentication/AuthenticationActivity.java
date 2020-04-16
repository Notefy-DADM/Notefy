package io.github.notefydadm.notefy.view.autentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import io.github.notefydadm.notefy.R;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        LoginCallback callback = new LoginCallback() {
            @Override
            public void login(String mail, String password) {

            }

            @Override
            public void registerClick() {
                openRegister();
            }
        };

        // Create new fragment and transaction
        LoginFragment loginFragment = new LoginFragment(callback);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_auth, loginFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();

    }

    void openRegister(){
        RegisterCallback callback = new RegisterCallback() {
            @Override
            public void register(String userName, String mail, String password) {

            }
        };

        RegisterFragment registerFragment = new RegisterFragment(callback);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_auth, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
