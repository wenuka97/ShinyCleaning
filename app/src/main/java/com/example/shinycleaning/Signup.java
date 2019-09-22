package com.example.shinycleaning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    private ImageView backImage;

    EditText fName, sName, email, pwd;

    Button btnRegister;

   // private ProgressDialog proDialog;

    FirebaseAuth firebaseAuth;

    DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //loadUI();

        fName = findViewById(R.id.first_name);
        sName = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnSignup);

        backImage = findViewById(R.id.backImgView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = fName.getText().toString();
                String surName = sName.getText().toString();
                String uEmail = email.getText().toString();
                String uPwd = pwd.getText().toString();

                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(surName)
                        || TextUtils.isEmpty(uEmail) || TextUtils.isEmpty(uPwd)) {

                    Toast.makeText(Signup.this, "Fill all the fields", Toast.LENGTH_SHORT).show();

                } else if (uPwd.length() < 8) {

                    Toast.makeText(Signup.this, "Password must include 8 characters", Toast.LENGTH_SHORT).show();

                } else {
                    registerUser(firstName, surName, uEmail, uPwd);
                }
            }
        });
    }


    private void registerUser(final String firstName, final String surName, final String uEmail, final String uPwd) {
        firebaseAuth.createUserWithEmailAndPassword(uEmail, uPwd).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> taskReg) {

                if (taskReg.isSuccessful()) {
                    // send user credentials to ths db and create user

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    System.out.println(userId);
                    System.out.println(firstName);
                    System.out.println(surName);
                    System.out.println(uEmail);

                    dbReference = FirebaseDatabase.getInstance().getReference().child("members").child(userId);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("uId", userId);
                    hashMap.put("firstName", firstName.toLowerCase());
                    hashMap.put("surName", surName.toLowerCase());
                    hashMap.put("uEmail", uEmail);


                    dbReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> taskRedSign) {
                            if (taskRedSign.isSuccessful()) {
                                Toast.makeText(Signup.this, "User registration success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this, Signin.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    //clear the all fields when error occurred
                    Toast.makeText(Signup.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                    fName.setText("");
                    sName.setText("");
                    email.setText("");
                    pwd.setText("");
                }
            }
        });

    }

    private void loadUI() {

        fName = (EditText)findViewById(R.id.first_name);
        sName = (EditText)findViewById(R.id.surname);
        email = (EditText)findViewById(R.id.email);
        pwd = (EditText)findViewById(R.id.password);
    }


    public void backToLogin() {
        Intent intent = new Intent(this, Signin.class);
        startActivity(intent);
    }


}
