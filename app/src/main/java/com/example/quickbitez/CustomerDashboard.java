package com.example.quickbitez;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class CustomerDashboard extends AppCompatActivity {

    private ViewPager2 imageSlider;
    private List<Integer> imageList;
    private List<String> dishNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        imageSlider = findViewById(R.id.imageSlider);

        // Image list
        imageList = new ArrayList<>();
        imageList.add(R.drawable.special_1);
        imageList.add(R.drawable.special_2);
        imageList.add(R.drawable.special_3);
        imageList.add(R.drawable.special_4);

        // Dish name list
        dishNames = new ArrayList<>();
        dishNames.add("Spaghetti Carbonara");
        dishNames.add("Sabudana Khichdi");
        dishNames.add("Onion Rings");
        dishNames.add("Paneer Tikka");

        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageList, dishNames);
        imageSlider.setAdapter(adapter);
    }
}
