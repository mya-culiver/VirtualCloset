package com.example.tommyscloset;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText mTextEmail;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    EditText mTextname;
    EditText mTextGender;
    Button mButtonRegister;
    TextView mTextViewLogin;
    FirebaseAuth mFirebaseAuth;
    ProgressBar progressBar;

    private static final String TAG = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mFirebaseAuth   = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        mTextEmail      = findViewById(R.id.edittext_email);
        mTextPassword   = findViewById(R.id.edittext_password);
        mTextCnfPassword = findViewById(R.id.edittext_cnf_password);
        mTextViewLogin = findViewById(R.id.textview_login);
        mTextname = findViewById(R.id.edittext_name);
        mTextGender = findViewById(R.id.edittext_gender);
        mButtonRegister = findViewById(R.id.button_register);


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mTextEmail.getText().toString().trim();
                final String name = mTextname.getText().toString().trim();
                final String gender = mTextGender.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) { //if Email field is empty
                    mTextEmail.setError("Please enter your email R1");
                    mTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(name)) { //if Email field is empty
                    mTextEmail.setError("Please enter your name");
                    mTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) { //if Password field is empty
                    mTextPassword.setError("Please enter your password R2");
                    mTextPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(cnf_pwd)) { //if Cnf Password field is empty
                    mTextCnfPassword.setError("Please confirm your password R3");
                    mTextCnfPassword.requestFocus();
                    return;
                }



                if ((pwd.isEmpty() && email.isEmpty() && cnf_pwd.isEmpty() && name.isEmpty())) { //if all fields are empty
                    Toast.makeText(RegisterActivity.this, "The Fields Are Empty R4", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( !(pwd.equals(cnf_pwd)) ) { //if Passwords dont match
                    mTextPassword.setError("Passwords do not match R5");
                    mTextCnfPassword.setError("Passwords do not match R6");
                    mTextPassword.requestFocus();
                    mTextCnfPassword.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Password is not matching R7", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mTextPassword.length() < 6) { // if passwords is less then 6 char
                    mTextPassword.setError("Password must be >= 6 characters R8");
                    return;
                }

                if (mFirebaseAuth.getCurrentUser() != null){
                    Toast.makeText(RegisterActivity.this, "Already Logged in 1", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

                // Set progress bar to visible while registration to firebase runs
                progressBar.setVisibility(View.VISIBLE);

                /*  if all fields are filled and Password matches Cnf Password and >= 6 char
                    create user from Email and Pwd and incase it doesn't go through then let user know about
                    registration error. Let user know if successful registration.
                */

                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /* Took out for  https://www.youtube.com/watch?v=K3mcK5fUIns instead of putting this in the firestore database
                            userID = mFirebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", userN);
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure " + e.toString());
                                }
                            });
                             */

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            // Get user email and uid from auth
                            String email = user.getEmail();
                            String uid= user.getUid();
                            // When user is registered store user info in firebase realtime database too
                            // Using Hashmap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            // Put info in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name); // Add later (e.g., edit the profile)
                            hashMap.put("image", "");
                            hashMap.put("gender", gender);
                            // Firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            // Path to store user data named "Users"
                            DatabaseReference reference = database.getReference("Users");
                            // Put data within hashmap in database
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Unsuccessful, Please Try Again: R11" + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        // Login clickable text to go back to login screen
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}