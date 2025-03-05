package com.example.quickbitez;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema change
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role"; // New column for role

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_EMAIL + " TEXT PRIMARY KEY, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_ROLE + " TEXT);"); // Added role column
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Register a new user with email, password, and role
    public boolean registerUser(String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // If -1, insertion failed
    }

    // Validate login based on email, password, and role
    public boolean validateUser(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?",
                new String[]{email, password, role});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}
