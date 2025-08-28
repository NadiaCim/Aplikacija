package com.example.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.db.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> restaurants;

    public RestaurantAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void updateData(List<Restaurant> newRestaurants) {
        this.restaurants = newRestaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant r = restaurants.get(position);

        holder.textName.setText(r.getName());
        holder.textLocation.setText(r.getLocation());
        holder.textRating.setText("Ocjena: " + r.getRating());

        // ✅ Postavi sliku prema NAZIVU ili TIPU
        holder.imageRestaurant.setImageResource(pickImage(r));
    }

    @Override
    public int getItemCount() {
        return restaurants != null ? restaurants.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRestaurant;
        TextView textName, textLocation, textRating;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRestaurant = itemView.findViewById(R.id.imageRestaurant);
            textName        = itemView.findViewById(R.id.textName);
            textLocation    = itemView.findViewById(R.id.textLocation);
            textRating      = itemView.findViewById(R.id.textRating);
        }
    }

    // --- Mapiranje na drawables (po imenu i/ili po tipu) ---
    private int pickImage(Restaurant r) {
        String name = safe(r.getName());
        String type = safe(r.getType());

        // 1) Pokušaj po imenu (specifične marke)
        if (name.contains("mc"))              return R.drawable.mcdonalds;
        if (name.contains("kfc"))             return R.drawable.kfc;
        if (name.contains("burger king"))     return R.drawable.burger_king;
        if (name.contains("sakura"))          return R.drawable.sushi;       // primjer
        if (name.contains("napoli"))          return R.drawable.pizza;
        if (name.contains("taco"))            return R.drawable.taco;
        if (name.contains("bbq"))             return R.drawable.bbq;
        if (name.contains("vege") || name.contains("vegan"))
            return R.drawable.vegan;

        // 2) Inače po tipu (okusu)
        if (type.equals("burger"))            return R.drawable.burger;
        if (type.equals("pizza"))             return R.drawable.pizza;
        if (type.equals("sushi"))             return R.drawable.sushi;
        if (type.equals("vegansko"))          return R.drawable.vegan;
        if (type.equals("meksicko") || type.equals("meksicko ")) // normirano
            return R.drawable.taco;
        if (type.equals("rostilj") || type.equals("roštilj"))
            return R.drawable.bbq;

        // 3) Rezervna slika
        return R.drawable.sample_restaurant;
    }

    private String safe(String s) {
        if (s == null) return "";
        s = s.trim().toLowerCase();

        // gruba normalizacija dijakritike koja nam treba za naše tipove
        s = s.replace("č","c").replace("ć","c").replace("đ","dj")
                .replace("š","s").replace("ž","z");
        return s;
    }
}
