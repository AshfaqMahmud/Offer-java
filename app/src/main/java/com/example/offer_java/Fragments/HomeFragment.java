package com.example.offer_java.Fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.offer_java.ImageAdapter;
import com.example.offer_java.ImageViewActivity;
import com.example.offer_java.MainActivity;
import com.example.offer_java.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView bottomSheetImageView; // Global ImageView reference

    private View view;
    private ProgressBar progressBar;
    private FrameLayout progressBarContainer;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the container
        progressBarContainer = view.findViewById(R.id.progressBarContainer);

        // Create a ProgressBar programmatically
        progressBar = new ProgressBar(getContext());
        // Optionally set layout parameters for the ProgressBar
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = android.view.Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);

        // Add ProgressBar to the container
        progressBarContainer.addView(progressBar);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.recycler);
        //FloatingActionButton openBottomSheet = view.findViewById(R.id.open_modal_bottom_sheet);
        ArrayList<String> arrayList = new ArrayList<>();

//        // Assume the arrayList is populated as before
//        // Setup recyclerView and adapter as before, but use getContext() instead of MainActivity.this
//        arrayList.add("https://cdn.wedevs.com/uploads/2021/04/weDevs-Black-friday-offer-1024x559.png");
//        arrayList.add("https://www.ryanscomputers.com/storage/sliders/Order-Online-Express-Delivery-in-Dhaka-Main-Slide_1702528248.webp");
//        arrayList.add("https://www.ryanscomputers.com/storage/sliders/Intel-Sweater-free-offer-Slider_1702882775.webp");
//        arrayList.add("https://imgv3.fotor.com/images/blog-richtext-image/cocacola-ads-example-with-orange-background-slogan.png");
//        arrayList.add("https://www.startech.com.bd/image/cache/catalog/home/banner/winter-fest/winter-fest-bkash-free-delivery-home-982x500.png");
//        arrayList.add("https://sailors3bucket1.s3.ap-southeast-1.amazonaws.com/uploads/all/plxBU20pKXEnCTYZ50MhTBHF5wCpG9Khu0exzFHv.jpg");

        // Retrieve the data from Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("data");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String editTextValue = snapshot.child("editTextValue").getValue(String.class);
                    arrayList.add(imageUrl);
                    //Picasso.get().load(imageUrl).into(imageView);
                }
                adapter = new ImageAdapter(getActivity(), arrayList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(getActivity(), "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//
//        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(ImageView imageView, String path) {
//                startActivity(new Intent(getActivity(), ImageViewActivity.class).putExtra("image", path), ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "image").toBundle());
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        progressBar.setVisibility(View.GONE);
        return view;
    }


}