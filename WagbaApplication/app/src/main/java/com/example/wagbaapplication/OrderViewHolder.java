package com.example.wagbaapplication;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    ImageView image, minus, plus;
    TextView name, price,quantity, totalPrice;
    ImageButton delete;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.orderImage);
        name = itemView.findViewById(R.id.orderName);
        price = itemView.findViewById(R.id.orderPrice);
        quantity = itemView.findViewById(R.id.orderQuantity);
        totalPrice = itemView.findViewById(R.id.orderTotal);
        minus = itemView.findViewById(R.id.minus2);
        plus = itemView.findViewById(R.id.plus2);
        delete = itemView.findViewById(R.id.deleteButton);
    }
}
