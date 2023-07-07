package com.example.wagbaapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryContentViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView name, price,quantity, totalPrice;

    public OrderHistoryContentViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.orderHistoryContentImage);
        name = itemView.findViewById(R.id.orderHistoryContentName);
        price = itemView.findViewById(R.id.orderHistoryContentPrice);
        quantity = itemView.findViewById(R.id.orderHistoryContentQuantity);
        totalPrice = itemView.findViewById(R.id.orderHistoryContentTotal);
    }
}
