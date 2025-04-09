package com.example.quickbitez;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class CustomerDashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton navButton;
    Button navHome, navYourProfile, navFavourites, navPastOrders, navSettings, navSendFeedback, navLogout;

    ViewPager2 imageSlider;
    SliderAdapter sliderAdapter;
    DatabaseHelper dbHelper;

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

        navHome = findViewById(R.id.navHome);
        navFavourites = findViewById(R.id.navFavourites);
        navPastOrders = findViewById(R.id.navPastOrders);
        navSettings = findViewById(R.id.navSettings);
        navSendFeedback = findViewById(R.id.navSendFeedback);
        navLogout = findViewById(R.id.navLogout);

        navHome.setOnClickListener(v -> startActivity(new Intent(CustomerDashboard.this, CustomerDashboard.class)));
        navFavourites.setOnClickListener(v -> startActivity(new Intent(CustomerDashboard.this, CustomerDashboard.class)));
        navPastOrders.setOnClickListener(v -> startActivity(new Intent(CustomerDashboard.this, CustomerDashboard.class)));
        navSettings.setOnClickListener(v -> startActivity(new Intent(CustomerDashboard.this, CustomerDashboard.class)));
        navSendFeedback.setOnClickListener(v -> startActivity(new Intent(CustomerDashboard.this, CustomerDashboard.class)));
        navLogout.setOnClickListener(v -> {
            startActivity(new Intent(CustomerDashboard.this, LoginActivity.class));
            finish();
        });

        // Initialize image slider
        imageSlider = findViewById(R.id.imageSlider);
        dbHelper = new DatabaseHelper(this);

        List<SliderItem> sliderItems = getSliderItemsFromDB();
        sliderAdapter = new SliderAdapter(sliderItems);
        imageSlider.setAdapter(sliderAdapter);
    }

    private List<SliderItem> getSliderItemsFromDB() {
        List<SliderItem> items = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCatererItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                items.add(new SliderItem(imageBitmap, name));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return items;
    }
}
