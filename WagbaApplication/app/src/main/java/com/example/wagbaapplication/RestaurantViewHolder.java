package com.example.wagbaapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView name,rating;

    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.restaurantImage);
        name = itemView.findViewById(R.id.restaurantName);
        rating = itemView.findViewById(R.id.restaurantRating);
    }
}
