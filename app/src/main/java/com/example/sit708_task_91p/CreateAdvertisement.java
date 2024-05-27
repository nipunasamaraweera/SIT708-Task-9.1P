package com.example.sit708_task_91p;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvertisement extends Activity {
    // Constant for location permission request code
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000;
    // Constant for autocomplete request code
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    // UI elements
    private RadioGroup radioPostType;
    private RadioButton radioLost, radioFound;
    private EditText editTextName, editTextPhone, editTextDescription, editTextDate, editTextLocation;
    private Button buttonSave, buttonGetCurrentLocation;

    // Location variables
    private double Latitude;
    private double Longitude;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize UI elements
        initUI();

        // Initialize Places API
        initPlaces();

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up location autocomplete
        setupLocationAutocomplete();

        // Set up current location button
        setupGetCurrentLocationButton();

        // Set up save button
        setupSaveButton();
    }

    // Initialize UI elements
    private void initUI() {
        radioPostType = findViewById(R.id.radioGroupPostType);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonSave = findViewById(R.id.buttonSave);
        buttonGetCurrentLocation = findViewById(R.id.buttonGetCurrentLocation);
    }

    // Initialize Places API
    private void initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyB7TUWgWniTjEP8JnImV8BsJs41Eg52u1Q");
        }
    }

    // Set up location autocomplete
    private void setupLocationAutocomplete() {
        editTextLocation.setFocusable(false);
        editTextLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(CreateAdvertisement.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    // Set up current location button
    private void setupGetCurrentLocationButton() {
        buttonGetCurrentLocation.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                getLocation();
            }
        });
    }

    // Set up save button
    private void setupSaveButton() {
        buttonSave.setOnClickListener(v -> {
            String postType = radioLost.isChecked() ? "Lost" : "Found";
            String name = editTextName.getText().toString();
            String phone = editTextPhone.getText().toString();
            String description = editTextDescription.getText().toString();
            String date = editTextDate.getText().toString();
            String location = editTextLocation.getText().toString();

            // Validate input fields
            if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                Toast.makeText(CreateAdvertisement.this, "Please fill out required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new LostFoundItem object
            LostFoundItem item = new LostFoundItem(postType + " " + name, description, phone, date, location, Latitude, Longitude);

            // Save the item
            saveItem(item);

            // Clear input fields
            clearFields();

            // Notify the user
            Toast.makeText(CreateAdvertisement.this, "Saved!", Toast.LENGTH_SHORT).show();
        });
    }

    // Save the LostFoundItem object to SharedPreferences with try-catch for error handling
    private void saveItem(LostFoundItem item) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Retrieve the existing items
            String itemsJson = sharedPreferences.getString("items", "[]");
            Type type = new TypeToken<ArrayList<LostFoundItem>>() {}.getType();
            ArrayList<LostFoundItem> itemList = new Gson().fromJson(itemsJson, type);

            // Add the new item
            itemList.add(item);

            // Save the updated list
            itemsJson = new Gson().toJson(itemList);
            editor.putString("items", itemsJson);
            editor.apply();

            Log.d("SaveItem", "Item saved successfully");
        } catch (Exception e) {
            Log.e("SaveItem", "Error saving item", e);
            Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
        }
    }

    // Get the current location and update the UI with try-catch for error handling
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Log.d("Location", "Location: " + location.toString());
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(CreateAdvertisement.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String addressFragments = address.getMaxAddressLineIndex() >= 0 ? address.getAddressLine(0) : "";
                        runOnUiThread(() -> editTextLocation.setText(addressFragments));
                    } else {
                        Toast.makeText(CreateAdvertisement.this, "No address found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | IllegalArgumentException e) {
                    Toast.makeText(CreateAdvertisement.this, "Service unavailable or invalid data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateAdvertisement.this, "Location not detected", Toast.LENGTH_SHORT).show();
                Log.d("Location", "Location not detected");
            }
        });
    }

    // Handle the result from location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Clear input fields after saving an item
    private void clearFields() {
        editTextName.setText("");
        editTextPhone.setText("");
        editTextDescription.setText("");
        editTextDate.setText("");
        editTextLocation.setText("");
        radioPostType.clearCheck();
    }

    // Handle the result from the autocomplete activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                editTextLocation.setText(place.getAddress());
                if (place.getLatLng() != null) {
                    Latitude = place.getLatLng().latitude;
                    Longitude = place.getLatLng().longitude;
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Address selection canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
