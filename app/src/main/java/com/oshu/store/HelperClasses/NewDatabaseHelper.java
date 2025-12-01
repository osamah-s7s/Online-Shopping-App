package com.oshu.store.HelperClasses;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Any_special";
    private static final int DATABASE_VERSION = 1;

    public NewDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, imagePath TEXT, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }
}
