package com.example.offer_java.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.offer_java.MainActivity;
import com.example.offer_java.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostFragment extends Fragment {

    private View view;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView;
    private EditText editTextDescription;
    private Button buttonPost;
    private Button buttonDiscard;

    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);


        imageView = view.findViewById(R.id.imageView);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonPost = view.findViewById(R.id.buttonPost);
        buttonDiscard = view.findViewById(R.id.buttonDiscard);


        imageView.setOnClickListener(v -> openGallery());

        buttonPost.setOnClickListener(v -> uploadData());
        buttonDiscard.setOnClickListener(v -> discardData());
        return view;

    }

    private void discardData() {
        imageView.setImageResource(R.drawable.baseline_catching_pokemon_24);
        editTextDescription.setText("");
        imageUri = null;
    }

    private void uploadData() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        // Convert Bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Create a reference to the Firebase Storage location
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + UUID.randomUUID() + ".jpg");

        // Upload the image data to Firebase Storage
        storageRef.putBytes(imageData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Call uploadDataToRealtimeDatabase with the download URL and editTextValue
                                // Get a reference to the Realtime Database
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                                // Create a unique key for the data
                                String key = database.child("data").push().getKey();

                                // Create a HashMap to store the data
                                Map<String, Object> data = new HashMap<>();
                                data.put("imageUrl", uri.toString());
                                data.put("editTextValue", editTextDescription.getText().toString());

                                // Upload the data to Realtime Database
                                database.child("data").child(key).setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data uploaded successfully
                                                Toast.makeText(getActivity(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle errors
                                                Toast.makeText(getActivity(), "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}