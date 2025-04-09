package com.example.quickbitez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

public class CatererDashboard extends Activity {
    DrawerLayout drawerLayout;
    ImageButton navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caterer_dashboard);
        drawerLayout = findViewById(R.id.drawerLayout);
        navButton = findViewById(R.id.navButton);
        navButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}