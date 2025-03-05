package com.example.quickbitez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuickBitez.db";
    private static final int DATABASE_VERSION = 1;

    // Customers table
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String COLUMN_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    // Caterers table
    private static final String TABLE_CATERERS = "caterers";
    private static final String COLUMN_CATERER_ID = "caterer_id";
    private static final String COLUMN_BUSINESS_NAME = "business_name";
    private static final String COLUMN_BUSINESS_ADDRESS = "business_address";

    // Profile images table
    private static final String TABLE_PROFILE_IMAGES = "profile_images";
    private static final String COLUMN_IMAGE_ID = "image_id";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image";

    // Create customers table
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE " + TABLE_CUSTOMERS + " ("
            + COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FULL_NAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
            + COLUMN_PHONE + " TEXT, "
            + COLUMN_PASSWORD + " TEXT NOT NULL);";

    // Create caterers table
    private static final String CREATE_TABLE_CATERERS = "CREATE TABLE " + TABLE_CATERERS + " ("
            + COLUMN_CATERER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FULL_NAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
            + COLUMN_PHONE + " TEXT, "
            + COLUMN_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_BUSINESS_NAME + " TEXT NOT NULL, "
            + COLUMN_BUSINESS_ADDRESS + " TEXT NOT NULL);";

    // Create profile images table
    private static final String CREATE_TABLE_PROFILE_IMAGES = "CREATE TABLE " + TABLE_PROFILE_IMAGES + " ("
            + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL, "
            + COLUMN_PROFILE_IMAGE + " BLOB, "
            + "FOREIGN KEY(" + COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_CUSTOMERS + "(" + COLUMN_EMAIL + ") ON DELETE CASCADE, "
            + "FOREIGN KEY(" + COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_CATERERS + "(" + COLUMN_EMAIL + ") ON DELETE CASCADE);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOMERS);
        db.execSQL(CREATE_TABLE_CATERERS);
        db.execSQL(CREATE_TABLE_PROFILE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATERERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String email, String phone, String password, String role, byte[] profileImage, String businessName, String businessAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);

        long userId;
        if (role.equals("Customer")) {
            userId = db.insert(TABLE_CUSTOMERS, null, values);
        } else {
            values.put(COLUMN_BUSINESS_NAME, businessName);
            values.put(COLUMN_BUSINESS_ADDRESS, businessAddress);
            userId = db.insert(TABLE_CATERERS, null, values);
        }

        if (userId == -1) return false;

        // Insert profile image
        ContentValues imageValues = new ContentValues();
        imageValues.put(COLUMN_USER_EMAIL, email);
        imageValues.put(COLUMN_PROFILE_IMAGE, profileImage);
        db.insert(TABLE_PROFILE_IMAGES, null, imageValues);

        return true;
    }

    public Cursor getUserByEmail(String email, boolean isCaterer) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = isCaterer ? TABLE_CATERERS : TABLE_CUSTOMERS;
        return db.query(table, null, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    public Cursor getProfileImageByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PROFILE_IMAGES, new String[]{COLUMN_PROFILE_IMAGE}, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
    }
}
