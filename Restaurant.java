package com.example.androidproject.db;

public class Restaurant {
    private int id;
    private String name;
    private String type;
    private String location;
    private float rating;

    public Restaurant(int id, String name, String type, String location, float rating) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.rating = rating;
    }

    // Getteri
    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public float getRating() { return rating; }
}
