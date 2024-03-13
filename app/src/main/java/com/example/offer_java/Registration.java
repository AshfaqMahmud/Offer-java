package com.example.offer_java;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        setContentView(R.layout.activity_registration);

        //Declare variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        Button register = findViewById(R.id.rgstrb);

        EditText tname, tmail, tpass, tphone, tcpass;


        tname = findViewById(R.id.tname);
        tmail = findViewById(R.id.tmail);
        tpass = findViewById(R.id.tpass);
        tcpass = findViewById(R.id.tcpass);
        tphone = findViewById(R.id.tuname);
//        //arraylist for spinner 1
//        List<String> status = new ArrayList<>();
//        status.add(0,"Choose current status");
//        status.add("Employed");
//        status.add("Unemployed");
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, status);
//        spinner.setAdapter(arrayAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Choose current status")){
//                }else {
//                    String item = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        //arraylist for spinner 2
//        List<String> status2 = new ArrayList<>();
//        status2.add(0,"Choose Category");
//        status2.add("IT");
//        status2.add("Business & Management");
//        status2.add("Engineering");
//        status2.add("Biology & Medical Science");
//        status2.add("Others");
//        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, status2);
//        spinner1.setAdapter(arrayAdapter2);
//        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Choose Category")){
//                }else {
//                    String item = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        //register button onclick
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Registration.this, "Register Button CLicked", Toast.LENGTH_SHORT).show();
                String name = tname.getText().toString();
                String phone = tphone.getText().toString();
                //String cat = spinner1.getSelectedItem().toString();
                String email = tmail.getText().toString();
                String cpass = tcpass.getText().toString();
                //String status = spinner.getSelectedItem().toString();
                String pass = tpass.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    tname.setError("Email cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    tpass.setError("Password cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    tphone.setError("Email cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    tmail.setError("Password cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(cpass)) {
                    tcpass.setError("Password cannot be empty");
                    return;
                }
//                if(pass != cpass)
//                {
//                    Toast.makeText(Registration.this, "Password and Confirm Password must be same", Toast.LENGTH_SHORT).show();
//                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(Registration.this, "inside Mauth", Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(Registration.this, "User registered.", Toast.LENGTH_SHORT).show();

                                //get current user id
                                String uid = user.getUid();
                                Toast.makeText(Registration.this, "User ID : " + uid, Toast.LENGTH_SHORT).show();

                                reference.child("userinfo").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Toast.makeText(Registration.this,"Inside Reference", Toast.LENGTH_SHORT).show();
                                        reference.child("userinfo").child(uid).child("Name").setValue(name);
                                        reference.child("userinfo").child(uid).child("Phone").setValue(phone);
                                        reference.child("userinfo").child(uid).child("Gender").setValue(gender);
                                        //reference.child("userinfo").child(uid).child("Category").setValue(cat);
                                        reference.child("userinfo").child(uid).child("Email").setValue(email);
                                        //reference.child("userinfo").child(uid).child("Status").setValue(status);

                                        //start login activity to login
                                        Intent intent = new Intent(Registration.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Registration.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch (view.getId()) {
//            case R.id.rbutton1:
//                if (checked) {
//                    gender = "Male";
//                }
//                break;
//            case R.id.rbutton2:
//                if (checked) {
//                    gender = "Female";
//                }
//                break;
//        }
//    }
}