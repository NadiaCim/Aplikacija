package com.example.androidproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidproject.db.DatabaseHelper;
import com.example.androidproject.db.Restaurant;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.robolectric.annotation.Config;

/**
 * Medium test: lokalno (Robolectric) testira DB logiku za top po gradu.
 */
@SuppressWarnings("deprecation")
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class DatabaseHelperMediumTest {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        Context ctx = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();

        // očisti tablicu restaurants
        db.execSQL("CREATE TABLE IF NOT EXISTS restaurants (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT NOT NULL," +
                " type TEXT NOT NULL," +
                " location TEXT NOT NULL," +
                " rating REAL NOT NULL CHECK(rating>=0 AND rating<=5))");
        db.delete("restaurants", null, null);


        insertRestaurant("A Grill", "BBQ", "Zagreb", 4.2f);
        insertRestaurant("B Steak", "BBQ", "Zagreb", 4.7f); // top Zagreb

        insertRestaurant("C Sushi", "Sushi", "Split", 4.5f); // top Split
        insertRestaurant("D Sushi", "Sushi", "Split", 4.1f);

        insertRestaurant("E Vegan", "Vegan", "Rijeka", 3.9f);
        insertRestaurant("F Vegan", "Vegan", "Rijeka", 4.3f); // top Rijeka
    }

    private void insertRestaurant(String name, String type, String city, float rating) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("type", type);
        cv.put("location", city);
        cv.put("rating", rating);
        db.insert("restaurants", null, cv);
    }

    @Test
    public void fetchTopPerCity_returnsOnePerCity_andPicksHighest() {
        List<Restaurant> tops = dbHelper.fetchTopPerCity();
        assertEquals(3, tops.size());

        Map<String, Float> expected = new HashMap<>();
        expected.put("Zagreb", 4.7f);
        expected.put("Split", 4.5f);
        expected.put("Rijeka", 4.3f);

        for (Restaurant r : tops) {
            String city = r.getLocation();   // KORISTIMO GETTER
            float rating = r.getRating();   // KORISTIMO GETTER

            assertTrue("Neočekivan grad: " + city, expected.containsKey(city));
            assertEquals(expected.get(city), rating, 0.0001f);
        }
    }
}
