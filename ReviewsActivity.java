package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private RecyclerView rv;
    private TextView tvEmpty;
    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews); // koristi tvoj postojeći layout

        // --- Dinamično pronađi postojeće ID-jeve (izbjegavamo "Cannot resolve symbol") ---
        rv = findRecycler("rvTopPerCity", "rvTopPerCityNew");
        tvEmpty = findText("tvEmpty", "tvEmptyNew");

        if (rv == null) {
            Toast.makeText(this, "RecyclerView nije pronađen u activity_reviews.xml", Toast.LENGTH_LONG).show();
            return;
        }

        db = new DatabaseHelper(this);

        // Dohvati #1 restoran po svakom gradu
        List<Restaurant> tops = db.fetchTopPerCity();

        if (tops == null || tops.isEmpty()) {
            if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rv.setAdapter(new RestaurantAdapter(tops));
        }

        // --- Bottom nav (ako postoji u layoutu) ---
        BottomNavigationView bottomNav = findBottomNav("bottomNav");
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.nav_search) {
                    startActivity(new Intent(this, SearchActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.nav_profile) {
                    // ako ti je "profil" zapravo LoginActivity, promijeni na LoginActivity.class
                    startActivity(new Intent(this, LoginActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            });
        }
    }

    // ---------- helpers: sigurni finderi bez compile-time R.id ovisnosti ----------

    private RecyclerView findRecycler(String... ids) {
        for (String id : ids) {
            int resId = getId(id);
            if (resId != 0) {
                RecyclerView v = findViewById(resId);
                if (v != null) return v;
            }
        }
        return null;
    }

    private TextView findText(String... ids) {
        for (String id : ids) {
            int resId = getId(id);
            if (resId != 0) {
                TextView v = findViewById(resId);
                if (v != null) return v;
            }
        }
        return null;
    }

    private BottomNavigationView findBottomNav(String id) {
        int resId = getId(id);
        if (resId != 0) {
            return findViewById(resId);
        }
        return null;
    }

    private int getId(String idName) {
        return getResources().getIdentifier(idName, "id", getPackageName());
    }
}
