package com.example.offer_java.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offer_java.ImageLoadingClasses.ChildModelClass;
import com.example.offer_java.ImageLoadingClasses.ParentAdapter;
import com.example.offer_java.ImageLoadingClasses.ParentModelClass;
import com.example.offer_java.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements ParentAdapter.OnItemClickListener {

    ProgressDialog dialog;
    private RecyclerView recyclerView;
    ArrayList<ParentModelClass> parentModelClassArrayList;
    private ProgressBar progressBar;
    ParentAdapter parentAdapter;
    String name = " dd";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.parentRecyclerView);

        parentModelClassArrayList = new ArrayList<>();

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");
        dialog.show();

//        // Create a ProgressBar programmatically
//        progressBar = new ProgressBar(getContext());
//        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        parentAdapter = new ParentAdapter(parentModelClassArrayList, getActivity(), this::onItemClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentModelClassArrayList.clear(); // Clear the parent list before populating it again

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("userinfo").child(uid).child("Name");
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot4) {
                            name = snapshot4.getValue().toString();
                            Toast.makeText(getActivity(), "Name is from " + name, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    ArrayList<ChildModelClass> childModelClassArrayList = new ArrayList<>(); // Create a new list for each user

                    for (DataSnapshot imagesnapshot : dataSnapshot.getChildren()) {
                        String imageURL = imagesnapshot.child("imageURL").getValue(String.class);
                        String imageDesc = imagesnapshot.child("imageDesc").getValue(String.class);

                        childModelClassArrayList.add(new ChildModelClass(imageURL, imageDesc));
                    }
                    Toast.makeText(getActivity(), "Name is " + name, Toast.LENGTH_SHORT).show();
                    parentModelClassArrayList.add(new ParentModelClass(name, childModelClassArrayList));
                }
                // After populating the list for all users, set the adapter
                recyclerView.setAdapter(parentAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });


        return view;
    }


    @Override
    public void onItemClick(int position, ParentModelClass parentItem) {

    }
}