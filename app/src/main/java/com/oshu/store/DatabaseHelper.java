package com.oshu.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


import com.oshu.store.HelperClasses.FeaturedHelperClass;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProductDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";

    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    // Insert a new product into the database
    public long insertProduct(String name, String description, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, imagePath); // Use the correct column name (COLUMN_IMAGE) for image path

        long productId = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return productId;
    }

    // Delete a product from the database
    public boolean deleteProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the WHERE clause
        String selection = COLUMN_NAME + " = ?";
        // Specify the value for the WHERE clause
        String[] selectionArgs = {productName};
        // Perform the deletion
        int deletedRows = db.delete(TABLE_PRODUCTS, selection, selectionArgs);
        db.close();
        // Return true if at least one row was deleted, indicating successful deletion
        return deletedRows > 0;
    }

    // Update a product in the database
    public boolean updateProduct(String currentTitle, String newTitle, String newDescription, Uri newImageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newTitle);
        values.put(COLUMN_DESCRIPTION, newDescription);

        if (newImageUri != null) {
            // Use the correct column name (COLUMN_IMAGE) for image path
            values.put(COLUMN_IMAGE, newImageUri.toString());
        }

        // Define the WHERE clause
        String selection = COLUMN_NAME + " = ?";
        // Specify the value for the WHERE clause
        String[] selectionArgs = {currentTitle};

        // Perform the update
        int updatedRows = db.update(TABLE_PRODUCTS, values, selection, selectionArgs);
        db.close();

        // Return true if at least one row was updated, indicating successful update
        return updatedRows > 0;
    }

    // Retrieve all products from the database
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
    }

    // Retrieve a product by its ID
    public FeaturedHelperClass getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_IMAGE
        };

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        FeaturedHelperClass product = null;

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

            // Assuming FeaturedHelperClass constructor takes image, title, description as parameters
            product = new FeaturedHelperClass(productId, image, name, description);

        }

        if (cursor != null) {
            cursor.close();
        }

        return product;
    }
}
