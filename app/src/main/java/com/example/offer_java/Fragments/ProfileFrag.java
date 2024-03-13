package com.example.offer_java.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offer_java.Login;
import com.example.offer_java.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFrag extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView logout = view.findViewById(R.id.logout);
        ShapeableImageView userimage =view.findViewById(R.id.userimage);
        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
        DatabaseReference reference= rootnode.getReference();
        TextView tname,tmail,tphone;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Button edit;

        //get id of textviews
        tname = view.findViewById(R.id.username);
        tmail = view.findViewById(R.id.usermail);
        tphone = view.findViewById(R.id.userconct);

        edit = view.findViewById(R.id.editprofile);

        //set value to TV using firebase
        reference.child("userinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uid = user.getUid();
                String name = snapshot.child(uid).child("Name").getValue(String.class);
                String mail = snapshot.child(uid).child("Email").getValue(String.class);
                String phone = snapshot.child(uid).child("Phone").getValue(String.class);
                //String image = snapshot.child(uid).child("imageurl").getValue(String.class);



                tname.setText(name);
                tmail.setText(mail);
                tphone.setText(phone);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Signing Out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences = getActivity().getSharedPreferences("login", 0);
                SharedPreferences.Editor myEdit = preferences.edit();
                myEdit.putBoolean("status",false);
                myEdit.apply();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
        //profile edit
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), EditProfile.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }
}