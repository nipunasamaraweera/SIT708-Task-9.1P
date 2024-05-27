package com.example.sit708_task_91p;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // GoogleMap instance to manage the map
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        // Initialize the map fragment and set the callback
        initMapFragment();
    }

    // Initialize the map fragment and set the callback
    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls and gestures on the map
        enableMapSettings();

        // Load items from SharedPreferences and add markers to the map
        loadItemsAndAddMarkers();
    }

    // Enable zoom controls and gestures on the map
    private void enableMapSettings() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    // Load items from SharedPreferences and add markers to the map
    private void loadItemsAndAddMarkers() {
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItem>>(){}.getType();
        ArrayList<LostFoundItem> itemList = new Gson().fromJson(itemsJson, type);

        if (!itemList.isEmpty()) {
            // Move camera to the location of the first item
            moveCameraToFirstItem(itemList.get(0));

            // Add markers for each item
            addMarkersForItems(itemList);

            // Set marker click listener
            mMap.setOnMarkerClickListener(this);
        }
    }

    // Move camera to the location of the first item
    private void moveCameraToFirstItem(LostFoundItem firstItem) {
        LatLng location = new LatLng(firstItem.getLatitude(), firstItem.getLongitude());
        float zoomLevel = 10.0f; // Set zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    // Add markers for each item on the map
    private void addMarkersForItems(ArrayList<LostFoundItem> itemList) {
        for (LostFoundItem item : itemList) {
            LatLng location = new LatLng(item.getLatitude(), item.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(item.getTitle()));
            marker.setTag(item); // Associate item with the marker
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Display details of the item associated with the clicked marker
        displayItemDetails(marker);
        return true;
    }

    // Display details of the item associated with the clicked marker
    private void displayItemDetails(Marker marker) {
        LostFoundItem item = (LostFoundItem) marker.getTag();
        if (item != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(item.getTitle());
            builder.setMessage("Description: " + item.getDescription() +
                    "\nPhone: " + item.getPhone() +
                    "\nDate: " + item.getDate() +
                    "\nLocation: " + item.getLocation());
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(this, "Details unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
