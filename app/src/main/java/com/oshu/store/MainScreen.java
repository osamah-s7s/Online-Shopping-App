package com.oshu.store;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oshu.store.HelperClasses.FeaturedAdapter;
import com.oshu.store.HelperClasses.FeaturedHelperClass;
import com.oshu.store.HelperClasses.SelectListner;
import com.oshu.store.HelperClasses.SimpleAdapter;
import com.oshu.store.HelperClasses.SimpleHelperClass;
import com.oshu.store.ProductsMain.AllProductsActivity;
import com.oshu.store.ProductsMain.CartView;
import com.oshu.store.ProductsMain.ProductDetailsFragment;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements SelectListner {
    private RecyclerView featuredRecycler;
    private RecyclerView AnyRecyclerView;
    private RecyclerView.Adapter adapter;
    private TextView view;
    private DatabaseHelper databaseHelper;
    

    private ArrayList<Integer> cartProductIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Hooks
        featuredRecycler = findViewById(R.id.mostLiked);
        AnyRecyclerView = findViewById(R.id.AnyRecyclerView);
        view = findViewById(R.id.ViewALL);

        // Set OnClickListener on the ViewALL TextView
        view.setOnClickListener(v -> viewAllProducts());

        // Find the FAB by ID
        // Find the FAB by ID for order details
        FloatingActionButton orderDetailsButton = findViewById(R.id.menu_icon);
        orderDetailsButton.setOnClickListener(v -> showOrderOptionsMenu(v));

        // Find the button by ID for logout
        View logoutButton = findViewById(R.id.logout_menu);
        logoutButton.setOnClickListener(v -> showLogoutMenuOptions(v));

        // Initialize RecyclerViews
        featuredRecycler();
        AnyRecyclerView();


    }

    // For menu_icon
    private void showOrderOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.END);
        popupMenu.inflate(R.menu.fab_menu);

        // Set a click listener on the menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_view_cart) {
                openCartView();
                return true;
            } else if (itemId == R.id.menu_order_details) {
                // openOrderDetails(); // Implement this method if needed
                return true;
            } else {
                return false;
            }
        });

        // Show the popup menu
        popupMenu.show();
    }

    // For logout_menu
    private void showLogoutMenuOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.END);
        popupMenu.inflate(R.menu.logout_menu);

        // Set a click listener on the menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_logout) {
                logout();
                return true;
            } else if (itemId == R.id.profile) {
                openProfile(); // Implement openProfile() method if needed
                return true;
            } else {
                return false;
            }
        });

        // Show the popup menu
        popupMenu.show();
    }

    private void openProfile() {
        //Open Profile
    }


    private void openCartView() {
        // Create an Intent to start the CartViewActivity
        Intent intent = new Intent(MainScreen.this, CartView.class);

        // Pass the list of cartProductIds to the CartViewActivity
        intent.putIntegerArrayListExtra("cartProductIds", cartProductIds);

        // Start the CartViewActivity
        startActivity(intent);
    }

    private void viewAllProducts() {
        // Retrieve all products from the database
        Cursor cursor = databaseHelper.getAllProducts();
        ArrayList<FeaturedHelperClass> allProducts = new ArrayList<>();

        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));

            FeaturedHelperClass featuredItem = new FeaturedHelperClass(0, imagePath, title, description);
            allProducts.add(featuredItem);
        }

        cursor.close();

        // Create an Intent to start a new activity (you need to create the activity)
        Intent intent = new Intent(this, AllProductsActivity.class);
        startActivity(intent);
    }

    private void featuredRecycler(ArrayList<FeaturedHelperClass> featuredLocations) {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new FeaturedAdapter(featuredLocations, this, this);
        featuredRecycler.setAdapter(adapter);
    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        // Retrieve data from the database
        Cursor cursor = databaseHelper.getAllProducts();

        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));

            FeaturedHelperClass featuredItem = new FeaturedHelperClass(0, imagePath, title, description);
            featuredLocations.add(featuredItem);
        }

        cursor.close();

        adapter = new FeaturedAdapter(featuredLocations, this, this);
        featuredRecycler.setAdapter(adapter);
    }

    private void AnyRecyclerView() {
        AnyRecyclerView.setHasFixedSize(true);
        AnyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<SimpleHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new SimpleHelperClass(R.drawable.iphone, "Iphone 13", "Apple iPhone 13 Pro Max - 6.7 / Inch Display - Dual Physical Sim - PTA Approved"));
        featuredLocations.add(new SimpleHelperClass(R.drawable.samsung, "Samsung S22", "Samsung Galaxy S22 8GB-128GB ROM PTA APPROVED OFFICIAL BRAND WARRANTY"));
        featuredLocations.add(new SimpleHelperClass(R.drawable.huawei, "Huawei", "Huawei P40 black | 128GB Storage | 4GB RAM | Octa-Core Processor | 3000 mAh Battery"));

        // Instantiate SimpleAdapter with the required parameters
        adapter = new SimpleAdapter(featuredLocations, this);
        AnyRecyclerView.setAdapter(adapter);
    }

    private void logout() {
        // Add your logout logic here
        // For example, you might want to clear user data and navigate to the login screen
        // Clear any user session or preferences as needed

        // For demonstration purposes, let's assume you want to navigate to the MainScreen after logout
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    @Override
    public void onitemClick(FeaturedHelperClass featuredHelperClass) {
        // Create an Intent to start the ProductDetailsActivity
        Intent intent = new Intent(MainScreen.this, ProductDetailsFragment.class);

        // Pass the necessary data to the new activity using Intent extras
        intent.putExtra("productId", featuredHelperClass.getId());
        intent.putExtra("title", featuredHelperClass.getTitle());
        intent.putExtra("description", featuredHelperClass.getDescription());
        intent.putExtra("image", featuredHelperClass.getImage());

        // Start the new activity
        startActivity(intent);
    }

    @Override
    public void onItemClick(SimpleHelperClass simpleHelperClass) {
        // Replace "YourActivity.this" with the actual reference to your activity
        Toast.makeText(MainScreen.this, "SSSSSsssssafdfdf", Toast.LENGTH_SHORT).show();
    }


}
