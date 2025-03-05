package com.example.quickbitez;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullName, email, phone, password;
    private Switch userTypeSwitch;
    private ImageView profileImageView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseHelper = new DatabaseHelper(this);

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);
        profileImageView = findViewById(R.id.profileImageView);
        Button uploadProfileButton = findViewById(R.id.uploadProfileButton);
        Button registerButton = findViewById(R.id.registerButton);

        uploadProfileButton.setOnClickListener(v -> selectProfileImage());

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void selectProfileImage() {
        // Implement an intent to pick an image
    }

    private void registerUser() {
        String userFullName = fullName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String role = userTypeSwitch.isChecked() ? "Caterer" : "Customer";

        byte[] profileImage = getProfileImageAsBytes();

        boolean success = databaseHelper.insertUser(userFullName, userEmail, userPhone, userPassword, role, profileImage);

        if (success) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getProfileImageAsBytes() {
        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
