package com.example.quickbitez;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 imageSlider;
    private List<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toggle Switch (For Dine-in / Takeaway - Not Functional Yet)
        Switch toggleSwitch = findViewById(R.id.toggleSwitch);

        // Image Slider Setup
        imageSlider = findViewById(R.id.imageSlider);
        imageList = new ArrayList<>();
        imageList.add(R.drawable.special_1);
        imageList.add(R.drawable.special_2);
        imageList.add(R.drawable.special_3);

        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageList);
        imageSlider.setAdapter(adapter);

        // Future Category Click Handlers
        ImageView breakfastButton = findViewById(R.id.breakfastButton);
        breakfastButton.setOnClickListener(v -> {
            // Future implementation
        });

        ImageView southIndianButton = findViewById(R.id.southIndianButton);
        southIndianButton.setOnClickListener(v -> {
            // Future implementation
        });

        // Footer Click Handlers
        TextView fullMenu = findViewById(R.id.fullMenu);
        fullMenu.setOnClickListener(v -> {
            // Future implementation
        });

        TextView cart = findViewById(R.id.cart);
        cart.setOnClickListener(v -> {
            // Future implementation
        });

        TextView myProfile = findViewById(R.id.myProfile);
        myProfile.setOnClickListener(v -> {
            // Future implementation
        });
    }
}
