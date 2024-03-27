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

import com.example.offer_java.ImageLoadingClasses.ChildModelClass;
import com.example.offer_java.ImageLoadingClasses.ParentAdapter;
import com.example.offer_java.ImageLoadingClasses.ParentModelClass;
import com.example.offer_java.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    ProgressDialog dialog;
    private RecyclerView recyclerView;
    ArrayList<ParentModelClass> parentModelClassArrayList;

    ParentAdapter parentAdapter;
    private long expectedNumberOfUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.parentRecyclerView);

        parentModelClassArrayList = new ArrayList<>();

//        childModelClassArrayList.add(new ChildModelClass(R.drawable.baseline_catching_pokemon_24));
//        childModelClassArrayList.add(new ChildModelClass(R.drawable.bgmain));
//        childModelClassArrayList.add(new ChildModelClass(R.drawable.loginimg));

        //parentModelClassArrayList.add(new ParentModelClass("Test",childModelClassArrayList));


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String uid = user.getUid();
        parentAdapter = new ParentAdapter(parentModelClassArrayList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(parentAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentModelClassArrayList.clear(); // Clear the parent list before populating it again

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    ArrayList<ChildModelClass> childModelClassArrayList = new ArrayList<>(); // Create a new list for each user

                    for (DataSnapshot imagesnapshot : dataSnapshot.getChildren()) {
                        String imageURL = imagesnapshot.child("imageURL").getValue(String.class);
                        String imageDesc = imagesnapshot.child("imageDesc").getValue(String.class);

                        childModelClassArrayList.add(new ChildModelClass(imageURL, imageDesc));
                    }
                    parentModelClassArrayList.add(new ParentModelClass(uid, childModelClassArrayList));
                }
                // After populating the list for all users, set the adapter
                recyclerView.setAdapter(parentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    //childModelClassArrayList.clear();
//                    parentModelClassArrayList.clear();
//                    Toast.makeText(getActivity(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
//                    String uid = dataSnapshot.getKey();
//                    ArrayList<ChildModelClass> childModelClassArrayList=new ArrayList<>();;
//                    // load user specific images into child view
//                    for (DataSnapshot imagesnapshot : dataSnapshot.getChildren()) {
//                        String imageURL = imagesnapshot.child("imageURL").getValue().toString();
//                        String imageDesc = imagesnapshot.child("imageDesc").getValue().toString();
//
//                        Toast.makeText(getActivity(), imageURL + " -- " + imageDesc, Toast.LENGTH_SHORT).show();
//                        childModelClassArrayList.add(new ChildModelClass(imageURL, imageDesc));
//                        //listOfImage.add(imageURL);
//                    } // load user specific images into child view is done
//                    parentModelClassArrayList.add(new ParentModelClass(uid, childModelClassArrayList));
//
//                    // load user specific images into parent view done
//
//                    recyclerView.setAdapter(parentAdapter);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }

    private void loadData() {
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");
        dialog.show();


    }
}
//for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//        //childModelClassArrayList.clear();
//        //parentModelClassArrayList.clear();
//        Toast.makeText(getActivity(),dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();
//        String uid= dataSnapshot.getKey();
//
//        // load user specific images into child view
//        for (DataSnapshot imagesnapshot : dataSnapshot.getChildren()) {
//        String imageURL= imagesnapshot.child("imageURL").getValue().toString();
//        String imageDesc = imagesnapshot.child("imageDesc").getValue().toString();
//
//        Toast.makeText(getActivity(),imageURL+" -- "+imageDesc,Toast.LENGTH_SHORT).show();
//        childModelClassArrayList.add(new ChildModelClass(imageURL,imageDesc));
//        //listOfImage.add(imageURL);
//        } // load user specific images into child view is done
//        parentModelClassArrayList.add(new ParentModelClass(uid,childModelClassArrayList));
//
//        // load user specific images into parent view done
//
//        recyclerView.setAdapter(parentAdapter);
//        }