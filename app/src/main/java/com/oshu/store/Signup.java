package com.oshu.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        //initializing
        TextView textView = findViewById(R.id.Already);
        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.Email);
        EditText password = findViewById(R.id.password);
        EditText rePassword = findViewById(R.id.rePassword);
        MaterialButton button = findViewById(R.id.signup);
        FirebaseAuth mAuth;
        Button backButton = findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();


        //Back button to login page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        //when user click on register button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uEmail, uName, uPass, uRPass;
                uEmail = String.valueOf(email.getText());
                uName = String.valueOf(username.getText());
                uPass = String.valueOf(password.getText());
                uRPass = String.valueOf(rePassword.getText());

                if (TextUtils.isEmpty(uEmail) || TextUtils.isEmpty(uName) || TextUtils.isEmpty(uPass) || TextUtils.isEmpty(uRPass)) {
                    Toast.makeText(Signup.this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!uPass.equals(uRPass)) {
                   Toast.makeText(Signup.this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                   return;
                }


                //database
                mAuth.createUserWithEmailAndPassword(uEmail, uPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Signup.this, "Account Created .",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Signup.this, Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Signup.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}