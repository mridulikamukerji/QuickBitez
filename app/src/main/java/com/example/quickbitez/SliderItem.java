package com.example.quickbitez;

import android.graphics.Bitmap;

public class SliderItem {

    private Bitmap imageBitmap;
    private String name;

    // Constructor
    public SliderItem(Bitmap imageBitmap, String name) {
        this.imageBitmap = imageBitmap;
        this.name = name;
    }

    // Getter for image
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    // Setter for image
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }
}
