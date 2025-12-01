package com.oshu.store.ProductsMain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.widget.Toast;

import com.bumptech.glide.Glide;  // Import Glide library
import com.oshu.store.DatabaseHelper;
import com.oshu.store.HelperClasses.FeaturedHelperClass;
import com.oshu.store.R;
import java.util.ArrayList;
import java.util.List;
public class ProductDetailsFragment extends AppCompatActivity {

    //YA HAMOD this is used to the save the Id's which user will select to make them printed on the cartView
    private List<Integer> cartProductIds = new ArrayList<>();  // List to store added product IDs

    private ImageView productImageView;
    private TextView productTitleTextView;
    private TextView productDescriptionTextView;
    private Button addToCartButton;

    private DatabaseHelper databaseHelper;
    private int productId;  // You may need to pass the product ID from the previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_fragment);

        // Initialize views
        productImageView = findViewById(R.id.productImageView);
        productTitleTextView = findViewById(R.id.productTitleTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        addToCartButton = findViewById(R.id.addToCartButton);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve the product ID from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productId")) {
            productId = intent.getIntExtra("productId", -1);
        }

        // Populate UI elements with product details
        displayProductDetails();

        // Set a click listener for the "Add to Cart" button
        addToCartButton.setOnClickListener(v -> addToCart());
    }

    private void displayProductDetails() {
        // Retrieve the product details from the intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");

        // Populate UI elements with product details
        productTitleTextView.setText(title);
        productDescriptionTextView.setText(description);

        // Use Glide to load the image into ImageView
        Glide.with(this).load(image).into(productImageView);
    }

    private void addToCart() {
        // Retrieve the product ID from the intent
        int productId = getIntent().getIntExtra("productId", -1);

        // Check if the product ID is valid
        if (productId != -1) {
            // Check if the product ID is not already in the cart
            if (!cartProductIds.contains(productId)) {
                // Add the product ID to the cart list
                cartProductIds.add(productId);

                // Show a toast message indicating success
                Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                // Show a toast message indicating that the product is already in the cart
                Toast.makeText(this, "Product is already in the cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a toast message indicating an error
            Toast.makeText(this, "Error adding product to cart", Toast.LENGTH_SHORT).show();
        }
    }
}
