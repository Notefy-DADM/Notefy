package io.github.notefydadm.notefy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Consumer;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.FragmentNoteViewBinding;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteViewFragment extends Fragment {
    private FragmentNoteViewBinding binding;
    private NoteViewModel noteViewModel;
    private NoteEditFragment noteEditFragment;

    public NoteViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        try {
            noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
            noteEditFragment = new NoteEditFragment();

            // Inflate the layout for this fragment with data binding
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_view, container, false);
            binding.setLifecycleOwner(this);
            binding.setViewModel(noteViewModel);
            binding.setEditView(new Consumer() {
                @Override
                public void accept(Object t) {
                    NoteViewFragment.this.editView();
                }
            });

            // Get the inflated view
            v = binding.getRoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void editView() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteEditFragment).addToBackStack(null).commit();
    }
}
