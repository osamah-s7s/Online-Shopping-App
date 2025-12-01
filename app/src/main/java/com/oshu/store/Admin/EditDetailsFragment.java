package com.oshu.store.Admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.oshu.store.DatabaseHelper;
import com.oshu.store.HelperClasses.FeaturedHelperClass;
import com.oshu.store.R;

public class EditDetailsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picker

    private Uri selectedImageUri; // To store the selected image URI
    private ImageView detailsImage;
    private EditText productNameEditText;
    private EditText productDescriptionEditText;
    private Button selectImageButton;
    private Button addButton;
    private FeaturedHelperClass currentProduct; // To store the details of the current product
    private DatabaseHelper databaseHelper;

    public static EditDetailsFragment newInstance(String image, String title, String description) {
        EditDetailsFragment fragment = new EditDetailsFragment();
        Bundle args = new Bundle();
        args.putString("image", image);
        args.putString("title", title);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        detailsImage = view.findViewById(R.id.details_image);
        productNameEditText = view.findViewById(R.id.productNameEditText);
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        addButton = view.findViewById(R.id.addButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(requireContext());

        // Retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title", "");

            // Load image using Glide
            Glide.with(requireContext())
                    .load(args.getString("image", ""))
                    .placeholder(R.drawable.default_product_image)
                    .into(detailsImage);

            // Set title and description
            productNameEditText.setText(title);
            productDescriptionEditText.setText(args.getString("description", ""));

            // Initialize current product details
            currentProduct = new FeaturedHelperClass(1, args.getString("image", ""), title, args.getString("description", ""));
        }

        // Set click listener for selecting an image
        selectImageButton.setOnClickListener(v -> {
            // Open the image picker
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Set click listener for adding/updating the product
        addButton.setOnClickListener(v -> updateProductDetails());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the picked image
            selectedImageUri = data.getData();

            // Load the selected image into ImageView using Glide or any other library
            Glide.with(requireContext())
                    .load(selectedImageUri)
                    .placeholder(R.drawable.default_product_image)
                    .into(detailsImage);
        }
    }

    private void updateProductDetails() {
        // Get the updated details from the UI
        String newTitle = productNameEditText.getText().toString().trim();
        String newDescription = productDescriptionEditText.getText().toString().trim();

        // Check if any field is empty
        if (newTitle.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the product details in the database
        boolean isUpdated = databaseHelper.updateProduct(
                currentProduct.getTitle(),
                newTitle,
                newDescription,
                selectedImageUri // Pass the selected image URI
        );

        if (isUpdated) {
            Toast.makeText(requireContext(), "Product details updated successfully", Toast.LENGTH_SHORT).show();
            // You may navigate back to the product list or perform any other action
            requireActivity().onBackPressed(); // Go back to the previous fragment
        } else {
            Toast.makeText(requireContext(), "Failed to update product details", Toast.LENGTH_SHORT).show();
        }
    }
}
