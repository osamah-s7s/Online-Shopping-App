package com.oshu.store.Admin;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.oshu.store.DatabaseHelper;
import com.oshu.store.R;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.oshu.store.DatabaseHelper;
import com.oshu.store.R;

public class DetailsFragment extends Fragment {

    private ImageView detailsImage;
    private TextView detailsTitle;
    private TextView detailsDescription;
    private Button deleteButton;

    public static DetailsFragment newInstance(String image, String title, String description) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("image", image);
        args.putString("title", title);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    private static class DeleteItemTask extends AsyncTask<String, Void, Boolean> {

        private final Context context;
        private final DetailsFragment fragment;

        DeleteItemTask(Context context, DetailsFragment fragment) {
            this.context = context;
            this.fragment = fragment;
        }

        @Override
        protected Boolean doInBackground(String... titles) {
            if (titles.length > 0) {
                String title = titles[0];
                return new DatabaseHelper(context).deleteProduct(title);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean deletionSuccessful) {
            fragment.handleDeletionResult(deletionSuccessful);
        }
    }

    private void handleDeletionResult(boolean deletionSuccessful) {
        String message;
        if (deletionSuccessful) {
            message = "Item deleted successfully";
            // Delay the navigation or use a Handler
            new Handler().postDelayed(() -> requireActivity().onBackPressed(), 1000);
        } else {
            message = "Failed to delete item";
        }
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailsImage = view.findViewById(R.id.details_image);
        detailsTitle = view.findViewById(R.id.details_title);
        detailsDescription = view.findViewById(R.id.details_description);
        deleteButton = view.findViewById(R.id.delete_Item);

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
            detailsTitle.setText(title);
            detailsDescription.setText(args.getString("description", ""));

            deleteButton.setOnClickListener(v -> deleteItemInBackground(title));
        }
    }

    private void deleteItemInBackground(String title) {
        // Check for null title
        if (title == null || title.isEmpty()) {
            Toast.makeText(requireContext(), "Invalid item title", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform deletion in the background using AsyncTask
        new DeleteItemTask(requireContext(), this).execute(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.with(this).clear(detailsImage);
    }
}
