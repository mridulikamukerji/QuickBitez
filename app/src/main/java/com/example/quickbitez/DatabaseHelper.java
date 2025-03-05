package com.example.quickbitez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 3; // Updated version

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USER_TYPE = "user_type"; // Student or Caterer
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture"; // Now a BLOB

    // Customers Table
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String COLUMN_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_DIETARY_PREFERENCES = "dietary_preferences";
    private static final String COLUMN_ALLERGIES = "allergies";

    // Caterers Table
    private static final String TABLE_CATERERS = "caterers";
    private static final String COLUMN_CATERER_ID = "caterer_id";
    private static final String COLUMN_BUSINESS_NAME = "business_name";
    private static final String COLUMN_BUSINESS_ADDRESS = "business_address";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FULL_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_USER_TYPE + " TEXT, "
                + COLUMN_PROFILE_PICTURE + " BLOB)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + " ("
                + COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_DIETARY_PREFERENCES + " TEXT, "
                + COLUMN_ALLERGIES + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_CUSTOMERS_TABLE);

        String CREATE_CATERERS_TABLE = "CREATE TABLE " + TABLE_CATERERS + " ("
                + COLUMN_CATERER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_BUSINESS_NAME + " TEXT, "
                + COLUMN_BUSINESS_ADDRESS + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_CATERERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATERERS);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String email, String phone, String password, String userType, byte[] profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USER_TYPE, userType);
        values.put(COLUMN_PROFILE_PICTURE, profilePicture);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public HashMap<String, String> getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        HashMap<String, String> userData = new HashMap<>();
        if (cursor.moveToFirst()) {
            userData.put("full_name", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)));
            userData.put("phone", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
        }
        cursor.close();
        return userData;
    }
}
