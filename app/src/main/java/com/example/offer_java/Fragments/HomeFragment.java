package com.example.offer_java.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offer_java.ParentAdapter;
import com.example.offer_java.R;
import com.example.offer_java.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    ProgressDialog dialog;
    private RecyclerView parentRecyclerView;
    private long expectedNumberOfUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        parentRecyclerView = view.findViewById(R.id.parentRecyclerView);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData();
        return view;
    }

    private void loadData() {
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");
        dialog.show();

        DatabaseReference databaseUserInfo = FirebaseDatabase.getInstance().getReference().child("userinfo");


        databaseUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserData> usersData = new ArrayList<>();
                expectedNumberOfUsers = dataSnapshot.getChildrenCount();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserData userData = new UserData();
                    userData.userId = userSnapshot.getKey();
                    // Assume UserData class has a field for the list of image URLs
                    // Here, we'll need another database call to fill this userData with the actual image URLs or data
                    // For brevity, assuming a method fetchUserData that fills in the details
                    fetchUserData(userData, usersData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load user info.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserData(UserData userData, List<UserData> usersData) {
        // Example method to fetch user-specific data (like image URLs) and update the UI
        // After fetching and filling userData:
        usersData.add(userData);

        // Once all user data are fetched, update the adapter
        if (usersData.size() == expectedNumberOfUsers) { // Ensure all data are fetched
            ParentAdapter parentAdapter = new ParentAdapter(usersData, getContext());
            parentRecyclerView.setAdapter(parentAdapter);
            dialog.dismiss();
        }
    }
}

