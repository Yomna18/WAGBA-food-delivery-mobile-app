package com.example.wagbaapplication;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DishViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView name,availability;
    ImageButton favourite;

    public DishViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.dishImage);
        name = itemView.findViewById(R.id.dishName);
        availability = itemView.findViewById(R.id.dishAvailability);
        favourite = itemView.findViewById(R.id.heartButton);
    }
}
