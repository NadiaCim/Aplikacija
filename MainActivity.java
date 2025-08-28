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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        btnFindRestaurants.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SearchActivity.class)));
        btnViewReviews.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReviewsActivity.class))
        );


        bottomNavigationView = findViewById(R.id.bottomNavigationView);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Trenutna aktivnost
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