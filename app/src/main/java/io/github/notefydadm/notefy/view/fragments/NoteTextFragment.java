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

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;
import io.github.notefydadm.notefy.databinding.FragmentNoteTextBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteTextFragment extends Fragment {
    private FragmentNoteTextBinding binding;
    private NoteViewModel noteViewModel;

    public NoteTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param text Text of the note.
     * @return A new instance of fragment NoteTextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteTextFragment newInstance(CharSequence text) {
        NoteTextFragment fragment = new NoteTextFragment();
        Bundle args = new Bundle();
        args.putCharSequence("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        try {
            // Inflate the layout for this fragment with data binding
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_text, container, false);
            binding.setLifecycleOwner(this);
            binding.setViewModel(noteViewModel);

            // Get the inflated view
            v = binding.getRoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        noteViewModel.getSelectedNote().observe(getViewLifecycleOwner(), new Observer<Note>() {
//            @Override
//            public void onChanged(Note note) {
//
//            }
//        });
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        //mainActivity = (MainActivity) context;
//    }
}
