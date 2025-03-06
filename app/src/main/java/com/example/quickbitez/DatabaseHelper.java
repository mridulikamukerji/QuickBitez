package com.example.quickbitez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuickBites.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "Users";
    private static final String TABLE_CUSTOMER = "Customer";
    private static final String TABLE_CATERER = "Caterer";
    private static final String TABLE_PROFILE_PHOTO = "ProfilePhoto";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "userType TEXT)";

        String createCustomerTable = "CREATE TABLE " + TABLE_CUSTOMER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "dietary_preferences TEXT, " +
                "allergies TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)";

        String createCatererTable = "CREATE TABLE " + TABLE_CATERER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "businessName TEXT, " +
                "businessAddress TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)";

        String createProfilePhotoTable = "CREATE TABLE " + TABLE_PROFILE_PHOTO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "photo BLOB, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)";

        db.execSQL(createUsersTable);
        db.execSQL(createCustomerTable);
        db.execSQL(createCatererTable);
        db.execSQL(createProfilePhotoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE_PHOTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATERER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public boolean insertUser(String email, String password, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password); // Store password as plaintext (for now)
        values.put("userType", userType);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?", new String[]{email, password});
            if (cursor != null && cursor.moveToFirst()) {
                return cursor;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching user", e);
        }
        return null;
    }
}
