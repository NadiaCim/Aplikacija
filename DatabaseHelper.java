package com.example.androidproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurants.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TBL_USERS = "users";
    private static final String U_ID      = "id";
    private static final String U_EMAIL   = "email";
    private static final String U_PASS    = "password";
    private static final String U_CREATED = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tablica restorana
        db.execSQL("CREATE TABLE IF NOT EXISTS restaurants (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "location TEXT NOT NULL, " +
                "rating REAL NOT NULL)");


        // Seed pri prvoj kreaciji
        seedIfEmpty(db);
        // Tablica korisnika
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TBL_USERS + " (" +
                        U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        U_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        U_PASS + " TEXT NOT NULL, " +
                        U_CREATED + " INTEGER NOT NULL)"
        );

        // Indeksi za users
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_email ON " + TBL_USERS + "(" + U_EMAIL + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS restaurants");
        onCreate(db);
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TBL_USERS + " (" +
                        U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        U_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        U_PASS + " TEXT NOT NULL, " +
                        U_CREATED + " INTEGER NOT NULL)"
        );
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_email ON " + TBL_USERS + "(" + U_EMAIL + ")");


}

    // SEED

    public void seedIfEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            seedIfEmpty(db);
        } finally {
            db.close();
        }
    }


    private void seedIfEmpty(SQLiteDatabase db) {
        int count = 0;
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM restaurants", null);
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();

        if (count == 0) {

            insertRestaurant(db, "McDonald's",      "Burger",   "Zagreb", 4.2f);
            insertRestaurant(db, "KFC",             "Burger",   "Split",  4.0f);
            insertRestaurant(db, "Burger King",     "Burger",   "Osijek", 4.3f);
            insertRestaurant(db, "Pizzeria Napoli", "Pizza",    "Zagreb", 4.6f);
            insertRestaurant(db, "Tokyo Bites",     "Sushi",    "Split",  4.7f);
            insertRestaurant(db, "Vege Bar",        "Vegansko", "Zagreb", 4.5f);
            insertRestaurant(db, "Taco King",       "Meksičko", "Rijeka", 4.1f);
            insertRestaurant(db, "BBQ House",       "Roštilj",  "Osijek", 4.4f);
            insertRestaurant(db, "Fresco Pizza",      "Pizza",    "Zagreb", 4.5f);
            insertRestaurant(db, "Marinero",          "Riba",     "Split",  4.2f);   // ako ne koristiš "Riba" u spinneru, stavi "Sushi" ili drugi tip
            insertRestaurant(db, "Vegan Corner",      "Vegansko", "Zadar",  4.3f);
            insertRestaurant(db, "El Sombrero",       "Meksičko", "Zagreb", 4.1f);
            insertRestaurant(db, "Smokey Grill",      "Roštilj",  "Karlovac", 4.4f);
            insertRestaurant(db, "Burger Point",      "Burger",   "Rijeka",  4.2f);
            insertRestaurant(db, "Pizza Tempo",       "Pizza",    "Osijek",  4.0f);
            insertRestaurant(db, "Sakura Sushi",      "Sushi",    "Zagreb",  4.6f);
            insertRestaurant(db, "Green Spoon",       "Vegansko", "Varaždin",4.2f);
            insertRestaurant(db, "Taco Loco",         "Meksičko", "Split",   4.3f);
            insertRestaurant(db, "Grill Masters",     "Roštilj",  "Zagreb",  4.5f);
            insertRestaurant(db, "Burger House",      "Burger",   "Zadar",   4.1f);
            insertRestaurant(db, "La Pizza",          "Pizza",    "Rijeka",  4.2f);
            insertRestaurant(db, "Sushi Box",         "Sushi",    "Osijek",  4.4f);
            insertRestaurant(db, "Plant Power",       "Vegansko", "Pula",    4.3f);
            insertRestaurant(db, "Cantina Viva",      "Meksičko", "Vinkovci",4.0f);
            insertRestaurant(db, "BBQ Station",       "Roštilj",  "Šibenik", 4.2f);
        }
    }

    private void insertRestaurant(SQLiteDatabase db, String name, String type, String location, float rating) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("type", type);
        cv.put("location", location);
        cv.put("rating", rating);
        db.insert("restaurants", null, cv);
    }

    public void insertRestaurant(String name, String type, String location, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            insertRestaurant(db, name, type, location, rating);
        } finally {
            db.close();
        }
    }

    // UPITI

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, type, location, rating FROM restaurants", null);

        if (cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndexOrThrow("id");
            int idxName = cursor.getColumnIndexOrThrow("name");
            int idxType = cursor.getColumnIndexOrThrow("type");
            int idxLocation = cursor.getColumnIndexOrThrow("location");
            int idxRating = cursor.getColumnIndexOrThrow("rating");

            do {
                int id = cursor.getInt(idxId);
                String name = cursor.getString(idxName);
                String type = cursor.getString(idxType);
                String location = cursor.getString(idxLocation);
                float rating = cursor.getFloat(idxRating);
                restaurants.add(new Restaurant(id, name, type, location, rating));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return restaurants;
    }

    public List<Restaurant> fetchTopPerCity() {
        SQLiteDatabase db = getReadableDatabase();
        String sql =
                "SELECT r1.id, r1.name, r1.type, r1.location, r1.rating " +
                        "FROM restaurants r1 " +
                        "WHERE NOT EXISTS ( " +
                        "  SELECT 1 FROM restaurants r2 " +
                        "  WHERE r2.location = r1.location AND " +
                        "       (r2.rating > r1.rating OR (r2.rating = r1.rating AND r2.name < r1.name)) " +
                        ") " +
                        "ORDER BY r1.location ASC";

        List<Restaurant> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, null);
        try {
            while (c.moveToNext()) {
                list.add(new Restaurant(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getFloat(4)
                ));
            }
        } finally {
            c.close();
        }
        return list;
    }


    //login provjera
    public enum AuthResult {
        NEW_USER,    // korisnik je kreiran (prvi login)
        OK,          // korisnik postoji i lozinka je ispravna
        WRONG_PASS   // korisnik postoji ali lozinka ne valja
    }

    /**
     * Pokuša prijaviti korisnika. Ako ne postoji -> kreira ga.
     * @return NEW_USER | OK | WRONG_PASS
     */
    public AuthResult loginOrCreateUser(String emailRaw, String password) {
        if (emailRaw == null) emailRaw = "";
        if (password == null) password = "";
        final String email = emailRaw.trim().toLowerCase();

        SQLiteDatabase db = getWritableDatabase();

        // Postoji li korisnik?
        String sql = "SELECT " + U_PASS + " FROM " + TBL_USERS + " WHERE " + U_EMAIL + " = ?";
        try (Cursor c = db.rawQuery(sql, new String[]{ email })) {
            if (c.moveToFirst()) {
                String stored = c.getString(0);
                if (stored != null && stored.equals(password)) {
                    return AuthResult.OK; // točna lozinka
                } else {
                    return AuthResult.WRONG_PASS; // postoji, ali lozinka pogrešna
                }
            }
        }

        // Ne postoji -> kreiraj
        ContentValues cv = new ContentValues();
        cv.put(U_EMAIL, email);
        cv.put(U_PASS, password);
        cv.put(U_CREATED, System.currentTimeMillis());
        db.insert(TBL_USERS, null, cv);
        return AuthResult.NEW_USER;
    }

}
