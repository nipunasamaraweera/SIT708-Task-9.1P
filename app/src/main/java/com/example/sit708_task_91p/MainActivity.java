package com.example.sit708_task_91p;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    // Buttons for navigating to different activities
    private Button btnCreateAdvert;
    private Button btnShowItems;
    private Button btnShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        initUI();

        // Set up click listeners for the buttons
        setupButtonListeners();
    }

    // Initialize UI elements
    private void initUI() {
        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems = findViewById(R.id.btnShowItems);
        btnShowOnMap = findViewById(R.id.btnShowOnMap);
    }

    // Set up click listeners for the buttons
    private void setupButtonListeners() {
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CreateAdvertisement activity
                Intent intentCreateAdvert = new Intent(MainActivity.this, CreateAdvertisement.class);
                startActivity(intentCreateAdvert);
            }
        });

        btnShowItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Lost_Found activity
                Intent intentShowItems = new Intent(MainActivity.this, Lost_Found.class);
                startActivity(intentShowItems);
            }
        });

        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MapActivity
                Intent intentShowOnMap = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intentShowOnMap);
            }
        });
    }
}
