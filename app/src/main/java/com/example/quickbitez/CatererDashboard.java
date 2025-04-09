package com.example.quickbitez;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CatererDashboard extends Activity {
    DrawerLayout drawerLayout;
    ImageButton navButton;
    Button navHome, navPastOrders, navSettings, navViewFeedback, navLogout;
    Button btnAddItem;
    DatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri = null;
    ImageView imgPreview;
    EditText editItemName;

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

        navHome = findViewById(R.id.navHome);
        navPastOrders = findViewById(R.id.navPastOrders);
        navSettings = findViewById(R.id.navSettings);
        navViewFeedback = findViewById(R.id.navViewFeedback);
        navLogout = findViewById(R.id.navLogout);
        btnAddItem = findViewById(R.id.btnAddItem);

        dbHelper = new DatabaseHelper(this);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, CatererDashboard.class)));
        navPastOrders.setOnClickListener(v -> startActivity(new Intent(this, CatererDashboard.class)));
        navSettings.setOnClickListener(v -> startActivity(new Intent(this, CatererDashboard.class)));
        navViewFeedback.setOnClickListener(v -> startActivity(new Intent(this, CatererDashboard.class)));
        navLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnAddItem.setOnClickListener(v -> showAddItemDialog());
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_item, null);

        imgPreview = view.findViewById(R.id.imgPreview);
        editItemName = view.findViewById(R.id.editItemName);
        Button btnUploadImage = view.findViewById(R.id.btnUploadImage);
        Button btnSaveItem = view.findViewById(R.id.btnSaveItem);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnUploadImage.setOnClickListener(v -> openImagePicker());

        btnSaveItem.setOnClickListener(v -> {
            String name = editItemName.getText().toString().trim();
            if (name.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Please enter name and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageBytes = stream.toByteArray();

                boolean inserted = dbHelper.insertSliderItem(imageBytes, name);
                if (inserted) {
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    editItemName.setText("");
                    imgPreview.setImageDrawable(null);
                    imageUri = null;
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image error", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (imgPreview != null) {
                imgPreview.setImageURI(imageUri);
            }
        }
    }
}
