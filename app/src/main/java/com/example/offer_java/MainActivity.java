package com.example.offer_java;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView bottomSheetImageView; // Global ImageView reference

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize ProgressDialog here
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        FloatingActionButton OpenBottomSheet = findViewById(R.id.open_modal_bottom_sheet);
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("https://cdn.wedevs.com/uploads/2021/04/weDevs-Black-friday-offer-1024x559.png");
        arrayList.add("https://www.ryanscomputers.com/storage/sliders/Order-Online-Express-Delivery-in-Dhaka-Main-Slide_1702528248.webp");
        arrayList.add("https://www.ryanscomputers.com/storage/sliders/Intel-Sweater-free-offer-Slider_1702882775.webp");
        arrayList.add("https://imgv3.fotor.com/images/blog-richtext-image/cocacola-ads-example-with-orange-background-slogan.png");
        arrayList.add("https://www.startech.com.bd/image/cache/catalog/home/banner/winter-fest/winter-fest-bkash-free-delivery-home-982x500.png");
        arrayList.add("https://sailors3bucket1.s3.ap-southeast-1.amazonaws.com/uploads/all/plxBU20pKXEnCTYZ50MhTBHF5wCpG9Khu0exzFHv.jpg");

        // Retrieve the data from Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("data");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String editTextValue = snapshot.child("editTextValue").getValue(String.class);

                    // Now you have the image URL and the EditText value
                    // You can load the image from the URL using any image loading library (e.g., Picasso, Glide, etc.)
                    // For example, using Picasso:
                    arrayList.add(imageUrl);
                    //Picasso.get().load(imageUrl).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(MainActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageAdapter adapter = new ImageAdapter(MainActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        //ImageView imageView = findViewById(R.id.imageView);

        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onClick(ImageView imageView, String path) {
                startActivity(new Intent(MainActivity.this, ImageViewActivity.class).putExtra("image", path), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, "image").toBundle());
            }
        });
        recyclerView.setAdapter(adapter);

        OpenBottomSheet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                MainActivity.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                                .inflate(R.layout.modal_bottom_sheet,
                                        (LinearLayout) findViewById(R.id.modalBottomSheetContainer));
                        bottomSheetImageView = bottomSheetView.findViewById(R.id.imageview);

                        bottomSheetView.findViewById(R.id.imageview).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "Folder clicked", Toast.LENGTH_SHORT).show();
                                openGallery();
                                //bottomSheetDialog.dismiss();
                            }
                        });

                        bottomSheetView.findViewById(R.id.post_ad).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText editText = findViewById(R.id.ad_desc);
                                String s = "Demo"; //editText.toString();
                                // Get the image URI from the ImageView inside the Bottom Sheet
                                if (bottomSheetImageView.getDrawable() != null) {
                                    bottomSheetImageView.setDrawingCacheEnabled(true);
                                    bottomSheetImageView.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) bottomSheetImageView.getDrawable()).getBitmap();

                                    // Upload the image to Cloud storage
                                    uploadImageToStorage(bitmap);
                                    //uploadDataToRealtimeDatabase();
                                } else {
                                    Toast.makeText(MainActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                                }
                                //bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();


                    }
                });

    }

//    @Override
//    protected void onStart(){
//
//    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImageToStorage(Bitmap bitmap) {
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
                        Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Call uploadDataToRealtimeDatabase with the download URL and editTextValue
                                uploadDataToRealtimeDatabase(uri.toString(), "editTextValue");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(MainActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void uploadDataToRealtimeDatabase(String imageUrl, String editTextValue) {
        // Get a reference to the Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Create a unique key for the data
        String key = database.child("data").push().getKey();

        // Create a HashMap to store the data
        Map<String, Object> data = new HashMap<>();
        data.put("imageUrl", imageUrl);
        data.put("editTextValue", editTextValue);

        // Upload the data to Realtime Database
        database.child("data").child(key).setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data uploaded successfully
                        Toast.makeText(MainActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(MainActivity.this, "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //uploadImageToStorage(bitmap);
                if (bottomSheetImageView != null) {
                    bottomSheetImageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Error: ImageView not found.", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                if (bottomSheetImageView != null) {
//                    bottomSheetImageView.setImageBitmap(bitmap);
//                } else {
//                    Toast.makeText(this, "Error: ImageView not found.", Toast.LENGTH_LONG).show();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}

//    private void uploadImageToFirestore(Bitmap bitmap, String editTextValue) {
//        // Convert Bitmap to byte array
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageData = baos.toByteArray();
//
//        // Upload the image to Firestore
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference().child("images/" + UUID.randomUUID() + ".jpg");
//        storageRef.putBytes(imageData)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Image uploaded successfully, get the download URL
//                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                // Upload the URI and EditText value to Realtime Database
//                                uploadDataToRealtimeDatabase(uri.toString(), editTextValue);
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle errors
//                        Toast.makeText(MainActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }