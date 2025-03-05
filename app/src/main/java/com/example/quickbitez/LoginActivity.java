package com.example.quickbitez;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private EditText emailInput, passwordInput;
    private Button loginButton, signUpButton;
    private Switch userTypeSwitch;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        loginButton.setOnClickListener(view -> loginUser());
        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean isCaterer = userTypeSwitch.isChecked();
        String expectedRole = isCaterer ? "Caterer" : "Customer";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getUserByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
            String storedRole = cursor.getString(cursor.getColumnIndex("role"));

            if (storedPassword.equals(password) && storedRole.equals(expectedRole)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = isCaterer ? new Intent(this, CatererDashboard.class) : new Intent(this, CustomerDashboard.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials or role mismatch", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}
