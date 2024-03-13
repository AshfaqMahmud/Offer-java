package com.example.offer_java.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offer_java.ChatListAdapter;
import com.example.offer_java.R;
import com.example.offer_java.User;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatFragment extends Fragment {

    private ChatListAdapter adapter;
    private final List<User> userList = new ArrayList<>();

    public void ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView chatListRecyclerView = view.findViewById(R.id.chatListRecyclerView);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatListAdapter(getContext(), userList);
        chatListRecyclerView.setAdapter(adapter);

        loadChatList();

        return view;
    }

    private void loadChatList() {
        FirebaseDatabase.getInstance().getReference("userinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    if(!Objects.equals(snapshot.getKey(), uid))
                    {
                        String name = snapshot.child("Name").getValue(String.class);
                        User user = new User(snapshot.getKey(), name,"Hello there");//snapshot.getValue(User.class);
                        userList.add(user);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}