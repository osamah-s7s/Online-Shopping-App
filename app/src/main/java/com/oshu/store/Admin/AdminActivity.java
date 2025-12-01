package com.oshu.store.Admin;

// AdminActivity.java

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import com.oshu.store.R;



import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.oshu.store.DatabaseHelper;
import com.oshu.store.Login;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

// AdminActivity.java

// ... (existing imports)

public class AdminActivity extends AppCompatActivity {

    // Existing code...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize UI elements
        Button logoutButton = findViewById(R.id.logoutButton);

        // Initialize database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Add bottom navigation item selected listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == MenuConstants.ACTION_ADD) {
                loadFragment(new AddItemFragment());
                return true;
            } else if (item.getItemId() == MenuConstants.ACTION_DELETE) {
                loadFragment(new DeleteItemFragment());
                return true;
            } else if (item.getItemId() == MenuConstants.ACTION_EDIT) {
                loadFragment(new EditItemFragment());
                return true;
            } else {
                return false;
            }
        });


        // Logout button click listener
        logoutButton.setOnClickListener(view -> {
            // Logout the user
            FirebaseAuth.getInstance().signOut();

            // Navigate back to the login activity
            Intent intent = new Intent(AdminActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Load the default fragment
        loadFragment(new DefaultFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


