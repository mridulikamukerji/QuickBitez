package com.example.quickbitez;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends Activity {
    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private ImageView profileImageView;
    private EditText fullName, email, phone, password, businessName, businessAddress;
    private Switch userTypeSwitch;
    private Button registerButton, uploadProfileButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseHelper = new DatabaseHelper(this);
        profileImageView = findViewById(R.id.profileImageView);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        businessName = findViewById(R.id.businessDetails);
        businessAddress = findViewById(R.id.businessAddress);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);
        registerButton = findViewById(R.id.registerButton);
        uploadProfileButton = findViewById(R.id.uploadProfileButton);

        userTypeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                businessName.setVisibility(View.VISIBLE);
                businessAddress.setVisibility(View.VISIBLE);
            } else {
                businessName.setVisibility(View.GONE);
                businessAddress.setVisibility(View.GONE);
            }
        });

        uploadProfileButton.setOnClickListener(view -> selectImage());
        registerButton.setOnClickListener(view -> registerUser());
    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = Intent.createChooser(pickPhoto, "Select or take a new Picture");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhoto});
        startActivityForResult(chooser, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null) {
                Uri selectedImage = data.getData();
                profileImageView.setImageURI(selectedImage);
            } else if (requestCode == CAPTURE_IMAGE && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profileImageView.setImageBitmap(photo);
            }
        }
    }

    private void registerUser() {
        String userFullName = fullName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        boolean isCaterer = userTypeSwitch.isChecked();
        String role = isCaterer ? "Caterer" : "Customer";

        if (userFullName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Full Name, Email, and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isCaterer && !userEmail.endsWith("@nmims.in")) {
            Toast.makeText(this, "Customers must use @nmims.in email", Toast.LENGTH_SHORT).show();
            return;
        }

        String userBusinessName = isCaterer ? businessName.getText().toString().trim() : "";
        String userBusinessAddress = isCaterer ? businessAddress.getText().toString().trim() : "";

        if (isCaterer && (userBusinessName.isEmpty() || userBusinessAddress.isEmpty())) {
            Toast.makeText(this, "Business Name and Address are required for caterers", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] profileImage = getProfileImageAsBytes();
        boolean success = databaseHelper.insertUser(userFullName, userEmail, userPhone, userPassword, role, profileImage, userBusinessName, userBusinessAddress);

        if (success) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getProfileImageAsBytes() {
        if (profileImageView.getDrawable() == null) {
            return new byte[0];
        }
        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
