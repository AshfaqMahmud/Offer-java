package com.example.offer_java;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.offer_java.Fragments.ChatFragment;
import com.example.offer_java.Fragments.HomeFragment;
import com.example.offer_java.Fragments.PostFragment;
import com.example.offer_java.Fragments.ProfileFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView bottomSheetImageView; // Global ImageView reference

    private ProgressDialog progressDialog;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar!=null)
//            actionBar.hide();
        SharedPreferences mPrefs = getSharedPreferences("login",0);
        boolean stat = mPrefs.getBoolean("status", false);
        System.out.println(stat);
        Toast.makeText(MainActivity.this, String.valueOf(stat),Toast.LENGTH_SHORT).show();
        if(!stat)
        {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        HomeFragment fragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.navigation_profile:

                        ProfileFrag fragment1 = new ProfileFrag();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.container, fragment1);
                        fragmentTransaction1.commit();
                        return true;

                    case R.id.navigation_chat:

                        ChatFragment fragment2 = new ChatFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.container, fragment2);
                        fragmentTransaction2.commit();
                        return true;
                    case R.id.navigation_post:
                        //openBottomSheet();

                        PostFragment fragment3 = new PostFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.container, fragment3);
                        fragmentTransaction3.commit();
                        return true;

                }
                return false;

            }
        });
    }
}
