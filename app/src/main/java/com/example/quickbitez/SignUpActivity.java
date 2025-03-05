package com.example.quickbitez;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    private boolean isCaterer = false; // Default role: Student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button registerButton = findViewById(R.id.registerButton);
        Switch userTypeSwitch = findViewById(R.id.userTypeSwitch);

        // Toggle between Student and Caterer
        userTypeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> isCaterer = isChecked);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String role = isCaterer ? "Caterer" : "Student";

                if (isValidEmail(email, role) && !password.isEmpty()) {
                    if (dbHelper.registerUser(email, password, role)) {
                        Toast.makeText(SignUpActivity.this, "Registered Successfully as " + role, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email, String role) {
        if (role.equals("Student")) {
            return email.endsWith("@nmims.in"); // Only students need to have @nmims.in
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Caterers need any valid email
    }
};