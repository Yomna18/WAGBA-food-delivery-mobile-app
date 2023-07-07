package com.example.wagbaapplication;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView name, price;
    ImageButton delete;

    public FavouriteViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.orderImageFavourite);
        name = itemView.findViewById(R.id.orderNameFavourite);
        price = itemView.findViewById(R.id.orderPriceFavourite);
        delete = itemView.findViewById(R.id.deleteFavouriteButton);

    }
}
