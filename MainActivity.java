package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Button btnFindRestaurants, btnViewReviews, btnProfile;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFindRestaurants = findViewById(R.id.btnFindRestaurants);
        btnViewReviews = findViewById(R.id.btnViewReviews);
        btnProfile = findViewById(R.id.btnProfile);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        btnFindRestaurants.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SearchActivity.class)));

        btnViewReviews.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReviewsActivity.class)));

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        // Umjesto switch-case koristimo if-else
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            } else if (itemId == R.id.nav_reviews) {
                startActivity(new Intent(MainActivity.this, ReviewsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }
}
