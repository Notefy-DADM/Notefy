package io.github.notefydadm.notefy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.databinding.UserItemBinding;

public class UserListAdapter extends Adapter<UserListAdapter.UserListViewHolder> {

    private MutableLiveData<List<String>> users;
    private Context fragmentContext;

    private UserItemBinding selectedItemBinding;

    private ItemListener listener;
    private ItemListenerInternal listenerInternal;

    public UserListAdapter(@NonNull LifecycleOwner owner, Context context, final ItemListener listener) {
        this.fragmentContext = context;
        this.listener = listener;
        this.listenerInternal = new ItemListenerInternal() {
            @Override
            public void itemClicked(UserItemBinding binding, int position) {
                if (selectedItemBinding != null) {
                    selectedItemBinding.setSelected(false);
                }

                binding.setSelected(true);
                selectedItemBinding = binding;

                if (listener != null) listener.itemClicked(position);
            }
        };
        this.users = new MutableLiveData<>();
        this.users.setValue(new ArrayList<String>());
        this.users.observe(owner, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> users) {
                UserListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public LiveData<List<String>> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users.setValue(users);
    }

    public String get(int position) {
        List<String> usersList = users.getValue();
        if (usersList != null) {
            return usersList.get(position);
        }
        return null;
    }

    public void add(String username) {
        List<String> usersList = users.getValue();
        if (usersList != null) {
            usersList.add(username);
            notifyDataSetChanged();
        }
    }

    public void remove(String username) {
        List<String> usersList = users.getValue();
        if (usersList != null) {
            selectedItemBinding.setSelected(false);
            selectedItemBinding = null;

            usersList.remove(username);
            listener.itemRemoved(username);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new UserListViewHolder(binding, listenerInternal);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        List<String> usersList = users.getValue();
        if (usersList != null) {
            String username = usersList.get(position);
            holder.bind(username, fragmentContext);
        }
    }

    @Override
    public int getItemCount() {
        List<String> usersList = users.getValue();
        return usersList == null ? 0 : usersList.size();
    }

    public interface ItemListener {
        void itemClicked(int position);
        void itemRemoved(String item);
    }

    private interface ItemListenerInternal {
        void itemClicked(UserItemBinding itemBinding, int position);
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private UserItemBinding binding;
        private ItemListenerInternal listener;

        private UserListViewHolder(@NonNull UserItemBinding itemBinding, ItemListenerInternal listener) {
            super(itemBinding.getRoot());

            this.binding = itemBinding;

            binding.getRoot().setOnClickListener(this);

            this.listener = listener;
        }

        public void bind(String username, Context context) {
            binding.setText(username);
        }

        @Override
        public void onClick(View view) {
            listener.itemClicked(binding, getAdapterPosition());
        }
    }
}
