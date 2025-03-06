package com.example.quickbitez;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private EditText fullName, email, phone, password, dietaryPreferences, allergies, businessDetails, businessAddress;
    private Switch userTypeSwitch;
    private LinearLayout customerFields, catererFields;
    private ImageView profileImageView;
    private Button uploadProfileButton, registerButton;
    private byte[] profileImage;
    private SQLiteDatabase db;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SQLiteOpenHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);
        customerFields = findViewById(R.id.customerFields);
        catererFields = findViewById(R.id.catererFields);
        dietaryPreferences = findViewById(R.id.dietaryPreferences);
        allergies = findViewById(R.id.allergies);
        businessDetails = findViewById(R.id.businessDetails);
        businessAddress = findViewById(R.id.businessAddress);
        profileImageView = findViewById(R.id.profileImageView);
        uploadProfileButton = findViewById(R.id.uploadProfileButton);
        registerButton = findViewById(R.id.registerButton);

        userTypeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // Caterer
                customerFields.setVisibility(View.GONE);
                catererFields.setVisibility(View.VISIBLE);
            } else { // Customer
                customerFields.setVisibility(View.VISIBLE);
                catererFields.setVisibility(View.GONE);
            }
        });

        uploadProfileButton.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, PICK_IMAGE);
        });

        registerButton.setOnClickListener(v -> registerUser());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
                profileImage = convertBitmapToByteArray(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void registerUser() {
        String name = fullName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        boolean isCaterer = userTypeSwitch.isChecked();

        if (name.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isCaterer && !userEmail.endsWith("@nmims.in")) {
            Toast.makeText(this, "Customers must use an @nmims.in email", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues userValues = new ContentValues();
        userValues.put("email", userEmail);
        userValues.put("password", userPass); // Store password as plaintext for now
        userValues.put("userType", isCaterer ? "Caterer" : "Customer");
        long userId = db.insert("Users", null, userValues);

        if (userId == -1) {
            Toast.makeText(this, "User registration failed: Email might already be in use", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isCaterer) {
            ContentValues catererValues = new ContentValues();
            catererValues.put("user_id", userId);
            catererValues.put("businessName", businessDetails.getText().toString());
            catererValues.put("businessAddress", businessAddress.getText().toString());
            long catererId = db.insert("Caterer", null, catererValues);

            if (catererId == -1) {
                Toast.makeText(this, "Caterer registration failed", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            ContentValues customerValues = new ContentValues();
            customerValues.put("user_id", userId);
            customerValues.put("dietary_preferences", dietaryPreferences.getText().toString());
            customerValues.put("allergies", allergies.getText().toString());
            long customerId = db.insert("Customer", null, customerValues);

            if (customerId == -1) {
                Toast.makeText(this, "Customer registration failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (profileImage != null) {
            ContentValues imageValues = new ContentValues();
            imageValues.put("user_id", userId);
            imageValues.put("photo", profileImage);
            long photoId = db.insert("ProfilePhoto", null, imageValues);

            if (photoId == -1) {
                Toast.makeText(this, "Profile photo upload failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
