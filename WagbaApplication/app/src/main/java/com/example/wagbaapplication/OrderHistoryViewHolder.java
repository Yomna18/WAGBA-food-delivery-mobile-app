package com.example.wagbaapplication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

    TextView orderId, deliveryTime, gateNumber, orderTime, status, totalAmount, paymentMethod;
    Button trackOrder;

    public OrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        orderId = itemView.findViewById(R.id.orderHistoryNumber);
        deliveryTime = itemView.findViewById(R.id.orderHistoryTime);
        gateNumber = itemView.findViewById(R.id.orderHistoryGate);
        orderTime = itemView.findViewById(R.id.orderHistoryDate);
        status = itemView.findViewById(R.id.orderHistoryStatus);
        totalAmount = itemView.findViewById(R.id.orderHistoryTotal);
        paymentMethod = itemView.findViewById(R.id.orderHistoryPaymentMethod);
        trackOrder = itemView.findViewById(R.id.trackOrderButton);
    }
}
