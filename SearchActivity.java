package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// ✅ VAŽNO: importaj DatabaseHelper iz paketa db
import com.example.androidproject.db.DatabaseHelper;

public class SearchActivity extends AppCompatActivity {

    private EditText etLocation;
    private Spinner spinnerCategory;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private TextView tvEmpty;

    private RestaurantAdapter adapter;
    private List<Restaurant> allRestaurants = new ArrayList<>();

    private DatabaseHelper dbHelper; // ✅ polje za bazu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Povezivanje s XML komponentama
        etLocation = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Inicijalizacija baze i dohvat podataka
        dbHelper = new DatabaseHelper(this);
        allRestaurants = dbHelper.getAllRestaurants();

        // Adapter i prikaz
        adapter = new RestaurantAdapter(allRestaurants);
        recyclerView.setAdapter(adapter);
        tvEmpty.setVisibility(allRestaurants.isEmpty() ? View.VISIBLE : View.GONE);

        // Logika pretrage
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = etLocation.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem() != null
                        ? spinnerCategory.getSelectedItem().toString()
                        : "";

                List<Restaurant> filtered = new ArrayList<>();
                for (Restaurant r : allRestaurants) {
                    boolean matchLocation = location.isEmpty()
                            || (r.getLocation() != null && r.getLocation().equalsIgnoreCase(location));
                    boolean matchCategory = category.isEmpty()
                            || category.equalsIgnoreCase("Svi")
                            || (r.getType() != null && r.getType().equalsIgnoreCase(category));

                    if (matchLocation && matchCategory) {
                        filtered.add(r);
                    }
                }

                adapter.updateData(filtered);
                tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
}
