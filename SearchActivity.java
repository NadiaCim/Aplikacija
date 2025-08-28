package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.db.DatabaseHelper;
import com.example.androidproject.db.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private EditText etLocation;
    private Spinner spinnerCategory; // UKUSI
    private Button btnSearch;
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private BottomNavigationView bottomNav;

    private RestaurantAdapter adapter;
    private List<Restaurant> allRestaurants = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etLocation      = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSearch       = findViewById(R.id.btnSearch);
        recyclerView    = findViewById(R.id.recyclerView);
        tvEmpty         = findViewById(R.id.tvEmpty);
        bottomNav       = findViewById(R.id.bottomNavigationView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Spinner s MANJE filtera (mora se poklapati s vrijednostima u bazi)
        setupTasteSpinner();

        // Baza + seed (samo ako je prazno)
        dbHelper = new DatabaseHelper(this);
        dbHelper.seedIfEmpty();

        allRestaurants = dbHelper.getAllRestaurants();

        // Debug – vidi stvarne vrijednosti
        for (Restaurant r : allRestaurants) {
            Log.d("DBG_FILTER", "Loaded: " + r.getName() + " | type=" + r.getType() + " | loc=" + r.getLocation());
        }

        adapter = new RestaurantAdapter(allRestaurants);
        recyclerView.setAdapter(adapter);
        tvEmpty.setVisibility(allRestaurants.isEmpty() ? TextView.VISIBLE : TextView.GONE);

        btnSearch.setOnClickListener(v -> performSearch());

        setupBottomNavigation();
    }

    private void setupTasteSpinner() {
        String[] tastes = {
                "Svi", "Burger", "Pizza", "Sushi", "Vegansko", "Meksičko", "Roštilj", "Riba"
        };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tastes
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);
    }

    private void performSearch() {
        String locInput = norm(textOf(etLocation));
        String tasteSel = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "";
        String taste    = norm(tasteSel);

        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant r : allRestaurants) {
            String rLoc  = norm(r.getLocation());
            String rType = norm(r.getType()); // JEDAN tip u bazi

            boolean matchLocation = TextUtils.isEmpty(locInput) || rLoc.equals(locInput);
            boolean matchTaste    = TextUtils.isEmpty(taste) || "svi".equals(taste) || rType.equals(taste);

            if (matchLocation && matchTaste) filtered.add(r);
        }

        adapter.updateData(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? TextView.VISIBLE : TextView.GONE);
    }

    private String textOf(EditText et) {
        return et.getText() == null ? "" : et.getText().toString();
    }

    // trim + lowercase + ukloni dijakritiku (č, ć, đ, š, ž → c, c, dj, s, z) + normaliziraj razmake
    private String norm(String s) {
        if (s == null) return "";
        String trimmed = s.trim().toLowerCase(Locale.ROOT);
        String noAccents = Normalizer.normalize(trimmed, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return noAccents.replaceAll("\\s+", " ");
    }

    private void setupBottomNavigation() {
        if (bottomNav == null) return;
        bottomNav.setSelectedItemId(R.id.nav_search);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_search) {
                // Trenutna aktivnost
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
