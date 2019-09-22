package com.example.shinycleaning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity {

    private Button loginBtn;
    private TextView signup;
    private EditText userEmail, userPassword;
    private ProgressBar signinProgress;

    private FirebaseAuth signAuth;
    private FirebaseUser currentMember;

   //public static final String USER_ID = "com.example.shinycleaning.USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loadUiView();


        signinProgress.setVisibility(View.INVISIBLE);

        FirebaseApp.initializeApp(this);
        signAuth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mEmail = userEmail.getText().toString();
                final String mPwd = userPassword.getText().toString();

                //If fields are Empty
                if (mEmail.equals("") || mPwd.equals("")) {
                    Toast.makeText(Signin.this, "Please complete required field", Toast.LENGTH_SHORT).show();
                }
                //If all fields are field
                else {
                    signinProgress.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.INVISIBLE);
                    signAuth.signInWithEmailAndPassword(mEmail, mPwd)
                            .addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> loginTask) {
                                    if (!loginTask.isSuccessful()) {
                                        signinProgress.setVisibility(View.INVISIBLE);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(Signin.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
                                        userEmail.setText("");
                                        userPassword.setText("");
                                    } else {
                                        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("members")
                                                .child(signAuth.getCurrentUser().getUid());

                                        //get current user's user id to String id from firebase
//                                        currentMember = signAuth.getInstance().getCurrentUser();
//                                        final String id = currentMember.getUid();
//                                        System.out.println("USER ID of CURRENT USER: " + id);

                                        dbReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Intent lgIntent = new Intent(Signin.this, ActivityHomePage.class);
                                                //lgIntent.putExtra(USER_ID, id);
                                                lgIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(lgIntent);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            });

                }
            }
        });

        //redirect to create user interface
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, Signup.class);
                startActivity(intent);
            }
        });


    }

    private void loadUiView() {

        signup = findViewById(R.id.signup);
        loginBtn = findViewById(R.id.login_btn);
        userEmail = findViewById(R.id.userName);
        userPassword = findViewById(R.id.password);
        signinProgress = findViewById(R.id.progressBar);
    }

}
