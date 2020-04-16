package io.github.notefydadm.notefy.view.autentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import io.github.notefydadm.notefy.R;


public class LoginFragment extends Fragment {

    LoginCallback callback;

    LoginFragment(LoginCallback callback){
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextInputLayout textInputLayoutMail = view.findViewById(R.id.textFieldMail);
        final TextInputLayout textInputLayoutPassword = view.findViewById(R.id.textFieldPassword);

        Button buttonRegister = view.findViewById(R.id.buttonRegister);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.registerClick();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = textInputLayoutMail.getEditText().getText().toString();
                String password = textInputLayoutPassword.getEditText().getText().toString();
                callback.loginCallback(mail,password);
            }
        });
    }

}
interface LoginCallback{
void loginCallback(String mail, String password);
void registerClick();
}