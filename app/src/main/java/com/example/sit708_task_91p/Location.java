package com.example.sit708_task_91p;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable EdgeToEdge display
        setupEdgeToEdge();

        // Set the content view to the layout for this activity
        initContentView();
    }

    // Method to enable EdgeToEdge display
    private void setupEdgeToEdge() {
        EdgeToEdge.enable(this);
    }

    // Method to set the content view to the layout for this activity
    private void initContentView() {
        setContentView(R.layout.activity_location);
    }
}
