package com.example.sit708_task_91p;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Lost_Found extends AppCompatActivity {

    // RecyclerView to display the list of lost and found items
    private RecyclerView recyclerViewLostFound;
    // Adapter for the RecyclerView
    private LostFoundAdapter adapter;
    // List to hold the lost and found items
    private List<LostFoundItem> itemList;

    // Launcher for starting an activity for result
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        boolean itemRemoved = data.getBooleanExtra("ITEM_REMOVED", false);
                        int position = data.getIntExtra("POSITION", -1);
                        if (itemRemoved && position != -1) {
                            adapter.removeItem(position);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        // Initialize UI elements
        initUI();

        // Load items from SharedPreferences
        itemList = loadItems();

        // Set up RecyclerView and its adapter
        setupRecyclerView();
    }

    // Initialize UI elements
    private void initUI() {
        recyclerViewLostFound = findViewById(R.id.recyclerViewLostFound);
        recyclerViewLostFound.setLayoutManager(new LinearLayoutManager(this));
    }

    // Load items from SharedPreferences
    private List<LostFoundItem> loadItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItem>>() {}.getType();
        return new Gson().fromJson(itemsJson, type);
    }

    // Set up RecyclerView and its adapter
    private void setupRecyclerView() {
        adapter = new LostFoundAdapter(this, itemList, position -> {
            Intent intent = new Intent(Lost_Found.this, ItemData.class);
            intent.putExtra("TITLE", itemList.get(position).getTitle());
            intent.putExtra("DESCRIPTION", itemList.get(position).getDescription());
            intent.putExtra("PHONE", itemList.get(position).getPhone());
            intent.putExtra("DATE", itemList.get(position).getDate());
            intent.putExtra("LOCATION", itemList.get(position).getLocation());
            intent.putExtra("POSITION", position);
            activityResultLauncher.launch(intent);
        });

        recyclerViewLostFound.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload items and update the adapter
        itemList.clear();
        itemList.addAll(loadItems());
        adapter.notifyDataSetChanged();
    }
}
