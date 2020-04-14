package io.github.notefydadm.notefy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Consumer;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.FragmentNoteTextViewBinding;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteTextViewFragment extends Fragment {
    private FragmentNoteTextViewBinding binding;
    private NoteViewModel noteViewModel;
    private NoteTextEditFragment noteTextEditFragment;

    public NoteTextViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        try {
            noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
            noteTextEditFragment = new NoteTextEditFragment();

            // Inflate the layout for this fragment with data binding
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_text_view, container, false);
            binding.setLifecycleOwner(this);
            binding.setViewModel(noteViewModel);
            binding.setEditView(new Consumer() {
                @Override
                public void accept(Object t) {
                    NoteTextViewFragment.this.editView();
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
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPortraitContainer, noteTextEditFragment).addToBackStack(null).commit();
    }
}
