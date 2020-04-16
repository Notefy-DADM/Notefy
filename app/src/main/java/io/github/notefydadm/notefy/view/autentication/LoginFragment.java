package io.github.notefydadm.notefy.view.autentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
}
interface LoginCallback{
void login(String mail,String password);
void registerClick();
}