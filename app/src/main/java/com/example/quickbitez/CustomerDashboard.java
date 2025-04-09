package com.example.quickbitez;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

public class CustomerDashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton navButton;
    Button navHome, navYourProfile, navFavourites, navPastOrders, navSettings, navSendFeedback, navLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        drawerLayout = findViewById(R.id.drawerLayout);
        navButton = findViewById(R.id.navButton);
        navButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navHome=findViewById(R.id.navHome);
        navYourProfile=findViewById(R.id.navYourProfile);
        navFavourites=findViewById(R.id.navFavourites);
        navPastOrders=findViewById(R.id.navPastOrders);
        navSettings=findViewById(R.id.navSettings);
        navSendFeedback=findViewById(R.id.navSendFeedback);
        navLogout=findViewById(R.id.navLogout);
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, CustomerDashboard.class);
            startActivity(intent);
        });
        navYourProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, CustomerDashboard.class);
            startActivity(intent);
        });
        navFavourites.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, CustomerDashboard.class);
            startActivity(intent);
        });
        navPastOrders.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, CustomerDashboard.class);
            startActivity(intent);
        });
        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, CustomerDashboard.class);
            startActivity(intent);
        });
        navSendFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, CustomerDashboard.class);
            startActivity(intent);
        });
        navLogout.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboard.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
