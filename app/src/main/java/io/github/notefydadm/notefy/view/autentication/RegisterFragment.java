package io.github.notefydadm.notefy.view.autentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

        private FragmentRegisterBinding binding;
        private RegisterCallback callback;

        RegisterFragment(RegisterCallback callback){
        this.callback = callback;
    }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        binding.setRegisterCallback(callback);
        return binding.getRoot();
    }
}

