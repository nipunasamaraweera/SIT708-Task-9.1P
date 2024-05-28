package com.example.sit708_task_91p;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ItemData extends Activity {

    // UI elements for displaying item details and removing the item
    private TextView textViewDetailName;
    private TextView textViewDetailPhone;
    private TextView textViewDetailDescription;
    private TextView textViewDetailDate;
    private TextView textViewDetailLocation;
    private Button buttonRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize UI elements
        initUI();

        // Retrieve data from the intent
        populateDataFromIntent();

        // Set up the remove button
        setupRemoveButton();
    }

    // Initialize UI elements
    private void initUI() {
        textViewDetailName = findViewById(R.id.textViewDetailName);
        textViewDetailPhone = findViewById(R.id.textViewDetailPhone);
        textViewDetailDescription = findViewById(R.id.textViewDetailDescription);
        textViewDetailDate = findViewById(R.id.textViewDetailDate);
        textViewDetailLocation = findViewById(R.id.textViewDetailLocation);
        buttonRemove = findViewById(R.id.buttonRemove);
    }

    // Retrieve data from the intent and set it to the UI elements
    @SuppressLint("SetTextI18n")
    private void populateDataFromIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String phone = intent.getStringExtra("PHONE");
        String date = intent.getStringExtra("DATE");
        String location = intent.getStringExtra("LOCATION");

        textViewDetailName.setText(title);
        textViewDetailPhone.setText("Contact on : " + phone);
        textViewDetailDescription.setText("Description: " + description);
        textViewDetailDate.setText("Date: " + date);
        textViewDetailLocation.setText("At " + location);
    }

    // Set up the remove button to return the result when clicked
    private void setupRemoveButton() {
        int position = getIntent().getIntExtra("POSITION", -1);
        buttonRemove.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ITEM_REMOVED", true);
            returnIntent.putExtra("POSITION", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}
