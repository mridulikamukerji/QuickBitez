package com.example.quickbitez;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private Switch userTypeSwitch;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.emailInput);
        editTextPassword = findViewById(R.id.passwordInput);
        buttonLogin = findViewById(R.id.loginButton);
        textViewRegister = findViewById(R.id.signUpButton);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);
        databaseHelper = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(v -> {
            try {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                boolean isCaterer = userTypeSwitch.isChecked();

                Log.d("LoginActivity", "Attempting login with email: " + email + ", isCaterer: " + isCaterer);

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = databaseHelper.getUser(email, password);
                if (cursor == null) {
                    Log.e("LoginActivity", "Database query returned a null cursor");
                    Toast.makeText(LoginActivity.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!cursor.moveToFirst()) {
                    Log.e("LoginActivity", "No user found with provided credentials");
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                int userTypeIndex = cursor.getColumnIndex("userType");
                if (userTypeIndex == -1) {
                    Log.e("LoginActivity", "'userType' column not found in database");
                    Toast.makeText(LoginActivity.this, "Database error: 'userType' column missing", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }

                String userType = cursor.getString(userTypeIndex);
                Log.d("LoginActivity", "Fetched userType from database: " + userType);
                cursor.close();

                // Verify if the selected switch matches the user type
                if (isCaterer && "Caterer".equals(userType)) {
                    Log.d("LoginActivity", "Redirecting to CatererDashboard");
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, CatererDashboard.class);
                    startActivity(intent);
                    finish();
                } else if (!isCaterer && "Customer".equals(userType)) {
                    Log.d("LoginActivity", "Redirecting to CustomerDashboard");
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, CustomerDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "User type mismatch: isCaterer=" + isCaterer + ", userType=" + userType);
                    Toast.makeText(LoginActivity.this, "Incorrect user type selection", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("LoginActivity", "Exception occurred during login", e);
                Toast.makeText(LoginActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        textViewRegister.setOnClickListener(v -> {
            Log.d("LoginActivity", "Navigating to SignUpActivity");
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
