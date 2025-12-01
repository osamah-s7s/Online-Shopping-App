package com.oshu.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.oshu.store.Admin.AdminActivity;

public class Login extends AppCompatActivity {

    // TAG for logging
    private static final String TAG = "LoginActivity";

    // Firebase Authentication instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        EditText uEmail = findViewById(R.id.Email);
        EditText uPassword = findViewById(R.id.password);
        TextView textView = findViewById(R.id.Already);
        MaterialButton loginButton = findViewById(R.id.LoginButton);

        // Already have an account? Click listener
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        });

        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {

            public boolean isUserAdmin(String email) {
                // Compare the user's email to the admin email
                String adminEmail = "email@example.com";
                return email.equalsIgnoreCase(adminEmail);
            }

            @Override
            public void onClick(View view) {
                // Get email and password from EditText fields
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                // Check if email and password are not empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early if fields are empty
                }

                // Sign in with email and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    boolean isAdmin = isUserAdmin(email);

                                    // Log the isAdmin value to see if the comparison is working
                                    Log.d(TAG, "isAdmin: " + isAdmin);

                                    // Navigate based on user role
                                    if (isAdmin) {
                                        // Admin login, navigate to admin activity
                                        Intent adminIntent = new Intent(Login.this, AdminActivity.class);
                                        startActivity(adminIntent);
                                    } else {
                                        // Regular user login, navigate to user activity
                                        Intent userIntent = new Intent(Login.this, MainScreen.class);
                                        startActivity(userIntent);
                                    }

                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user and log the error.
                                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed: " + errorMessage,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}
