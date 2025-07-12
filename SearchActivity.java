package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.db.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText etSearchQuery, etLocation;
    Spinner spinnerCategory;
    Button btnSearch;
    RecyclerView recyclerView;

    DatabaseHelper dbHelper;
    RestaurantAdapter adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearchQuery = findViewById(R.id.etSearchQuery);
        etLocation = findViewById(R.id.etLocation);  // novo polje
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        dbHelper = new DatabaseHelper(this);

        // Spinner setup
        String[] categories = {"Sve", "Burgeri", "Piletina", "Meksička", "Vegetarijanska"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapterSpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSearch.setOnClickListener(v -> {
            String query = etSearchQuery.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String selectedType = spinnerCategory.getSelectedItem().toString();

            List<String> results = dbHelper.searchRestaurants(query, selectedType, location);

            if (results.isEmpty()) {
                Toast.makeText(this, "Nema rezultata", Toast.LENGTH_SHORT).show();
            }

            adapter = new RestaurantAdapter(results);
            recyclerView.setAdapter(adapter);
        });

        // Bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_search) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
