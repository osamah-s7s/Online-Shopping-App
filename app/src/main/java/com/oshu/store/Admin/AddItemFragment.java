package com.oshu.store.Admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.oshu.store.DatabaseHelper;
import com.oshu.store.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddItemFragment extends Fragment {

    private EditText productNameEditText;
    private EditText productDescriptionEditText;
    private ImageView productImageView;

    private DatabaseHelper databaseHelper;

    // Constant for identifying the image selection intent
    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        // Initialize UI elements
        productNameEditText = view.findViewById(R.id.productNameEditText);
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText);
        productImageView = view.findViewById(R.id.productImageView);

        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button addButton = view.findViewById(R.id.addButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(requireContext());

        // Set click listener for the select image button
        selectImageButton.setOnClickListener(v -> openFileChooser());

        // Set click listener for the add button
        addButton.setOnClickListener(v -> addProduct());

        return view;
    }

    private void openFileChooser() {
        // Create an Intent to open the file picker
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Specify the type of files to be displayed (in this case, images)
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the file picker activity was successful and if the request code matches
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri imageUri = data.getData();
            // Set the image URI to the ImageView
            productImageView.setImageURI(imageUri);
        }
    }

    private void addProduct() {
        // Get product details from EditText fields
        String name = productNameEditText.getText().toString().trim();
        String description = productDescriptionEditText.getText().toString().trim();
        String imagePath = saveImageToInternalStorage(); // Get image path

        // Validate input
        if (name.isEmpty() || description.isEmpty() || imagePath.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the product to the database
        long productId = databaseHelper.insertProduct(name, description, imagePath);

        if (productId != -1) {
            Toast.makeText(requireContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
            // Clear the EditText fields and ImageView
            productNameEditText.setText("");
            productDescriptionEditText.setText("");
            productImageView.setImageURI(null);
        } else {
            Toast.makeText(requireContext(), "Failed to add product.", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImageToInternalStorage() {
        // Get the Bitmap from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) productImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Get the application's internal storage directory
        File storageDir = requireContext().getFilesDir();

        // Generate a unique filename for the image
        String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        // Create a file object for the image
        File imageFile = new File(storageDir, imageFileName);

        try {
            // Create a FileOutputStream to write the bitmap data to the file
            FileOutputStream fos = new FileOutputStream(imageFile);

            // Compress the bitmap and write it to the FileOutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            // Close the FileOutputStream
            fos.close();

            // Return the absolute path of the saved image
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return an empty string if there's an error
        }
    }
}
