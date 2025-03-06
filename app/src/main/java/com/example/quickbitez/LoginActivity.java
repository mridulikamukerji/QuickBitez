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
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            boolean isCaterer = userTypeSwitch.isChecked();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = databaseHelper.getUser(email, password);

            if (cursor == null) {
                Toast.makeText(LoginActivity.this, "Error: Cursor is null", Toast.LENGTH_SHORT).show();
            } else if (!cursor.moveToFirst()) {
                Toast.makeText(LoginActivity.this, "No user found with provided credentials", Toast.LENGTH_SHORT).show();
            } else {
                int userTypeIndex = cursor.getColumnIndex("userType");
                if (userTypeIndex == -1) {
                    Toast.makeText(LoginActivity.this, "Error: 'userType' column not found", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "'userType' column not found in cursor");
                } else {
                    String userType = cursor.getString(userTypeIndex);
                    cursor.close();

                    if (isCaterer && "Caterer".equals(userType)) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity", "Navigating to CatererDashboard");
                        Intent intent = new Intent(LoginActivity.this, CatererDashboard.class);
                        startActivity(intent);
                        // finish();
                    } else if (!isCaterer && "Customer".equals(userType)) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity", "Navigating to CustomerDashboard");
                        Intent intent = new Intent(LoginActivity.this, CustomerDashboard.class);
                        startActivity(intent);
                        // finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid user type or switch state", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
