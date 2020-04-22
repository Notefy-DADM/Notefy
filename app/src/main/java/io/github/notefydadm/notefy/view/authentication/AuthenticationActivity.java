package io.github.notefydadm.notefy.view.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.database.SingletonDatabase;
import io.github.notefydadm.notefy.view.dialogs.LoadingDialog;
import io.github.notefydadm.notefy.view.activities.MainActivity;

public class AuthenticationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        loadingDialog = new LoadingDialog();

        LoginCallback callback = new LoginCallback() {
            @Override
            public void loginCallback(String mail, String password) {
                login(mail,password);
            }

            @Override
            public void registerClick() {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                openRegister();
            }
        };

        // Create new fragment and transaction
        LoginFragment loginFragment = new LoginFragment(callback);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_auth, loginFragment);

// Commit the transaction
        transaction.commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        super.onBackPressed();
    }

    void openRegister(){
        RegisterCallback callback = new RegisterCallback() {
            @Override
            public void registerCallback(String userName, String mail, String password) {
                register(userName,mail,password);
            }
        };

        RegisterFragment registerFragment = new RegisterFragment(callback);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_auth, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    void login(String mail, String password){
        showLoading();
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("auth", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("auth", "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, R.string.authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    void register(final String userName, String mail, String password){
        showLoading();
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("auth", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("auth", "User profile updated.");
                                                goToMain();
                                            }
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("auth", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, R.string.authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void showLoading() {
        loadingDialog.show(getSupportFragmentManager(), null);
    }

    private void hideLoading() {
        loadingDialog.dismiss();
    }

    void goToMain(){
        SingletonDatabase.getInstance().init();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
