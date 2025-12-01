package com.oshu.store.ProductsMain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.oshu.store.DatabaseHelper;
import com.oshu.store.HelperClasses.FeaturedAdapter;
import com.oshu.store.HelperClasses.FeaturedHelperClass;
import com.oshu.store.HelperClasses.SelectListner;
import com.oshu.store.R;

import java.util.ArrayList;

public class AllProductsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    RecyclerView viewALLRecycler;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        viewALLRecycler = findViewById(R.id.viewALLRecycler); // Moved this line inside onCreate

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

        viewALLRecycler(allProducts);
    }

    private void viewALLRecycler(ArrayList<FeaturedHelperClass> featuredLocations) {
        viewALLRecycler.setHasFixedSize(true);
        viewALLRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new FeaturedAdapter(featuredLocations, this, null);
        viewALLRecycler.setAdapter(adapter);
    }

}
