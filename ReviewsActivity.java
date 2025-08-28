package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.db.DatabaseHelper;
import com.example.androidproject.db.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView rvTopPerCity;
    private TextView tvEmpty;
    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reviews);

        // View reference
        rvTopPerCity = findViewById(R.id.rvTopPerCity);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Data
        db = new DatabaseHelper(this);
        List<Restaurant> tops = db.fetchTopPerCity(); // #1 po svakom gradu

        // prikaži listu ili "nema podataka"
        if (tops == null || tops.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvTopPerCity.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvTopPerCity.setVisibility(View.VISIBLE);
            rvTopPerCity.setLayoutManager(new LinearLayoutManager(this));
            rvTopPerCity.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rvTopPerCity.setAdapter(new RestaurantAdapter(tops));
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }

            return false;
        });
    }
}
