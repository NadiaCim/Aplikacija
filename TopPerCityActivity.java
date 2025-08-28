package com.example.androidproject;


import com.example.androidproject.db.DatabaseHelper;
import com.example.androidproject.db.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TopPerCityActivity extends AppCompatActivity {
    private RecyclerView rv;
    private TextView tvEmpty;
    private DatabaseHelper db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_per_city);

        rv = findViewById(R.id.rvTopPerCityNew);
        tvEmpty = findViewById(R.id.tvEmptyNew);
        db = new DatabaseHelper(this);

        List<Restaurant> tops = db.fetchTopPerCity(); // metoda koju smo dodali u DB
        if (tops == null || tops.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rv.setAdapter(new RestaurantAdapter(tops)); // tvoj postojeći adapter
        }
        // Bottom Navigation — isti kao na LoginActivity
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        // Ako želiš označiti neki postojeći tab kao aktivan, npr. profil ili pretraga, odkomentiraj:
        // bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                // Promijeni ako ti je druga klasа za profil
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }
            return false;
        });
    }
}

