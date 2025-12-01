package com.oshu.store.Admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.oshu.store.DatabaseHelper;
import com.oshu.store.HelperClasses.FeaturedAdapter;
import com.oshu.store.HelperClasses.FeaturedHelperClass;
import com.oshu.store.HelperClasses.SelectListner;
import com.oshu.store.R;

import java.util.ArrayList;

public class DeleteItemFragment extends Fragment implements FeaturedAdapter.OnItemClickListener {
    private RecyclerView deleteItemRecycler;
    private FeaturedAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(requireContext());

        // Initialize RecyclerView
        deleteItemRecycler = view.findViewById(R.id.deleteItemRecycler);
        deleteItemRecycler.setHasFixedSize(true);
        deleteItemRecycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Populate RecyclerView with data from the database
        populateRecyclerView();

        return view;
    }

    private void populateRecyclerView() {
        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        // Retrieve data from the database
        Cursor cursor = databaseHelper.getAllProducts();

        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));

            FeaturedHelperClass featuredItem = new FeaturedHelperClass(1, imagePath, title, description);
            featuredLocations.add(featuredItem);
        }

        cursor.close();

        // Set up and attach the adapter
        adapter = new FeaturedAdapter(featuredLocations, requireContext(), null);
        deleteItemRecycler.setAdapter(adapter);


        // Set click listener
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(FeaturedHelperClass item) {
        // Create a new instance of DetailsFragment and pass data
        DetailsFragment detailsFragment = DetailsFragment.newInstance(item.getImage(), item.getTitle(), item.getDescription());

        // Replace the current fragment with DetailsFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, detailsFragment)
                .addToBackStack(null)
                .commit();
    }


}
