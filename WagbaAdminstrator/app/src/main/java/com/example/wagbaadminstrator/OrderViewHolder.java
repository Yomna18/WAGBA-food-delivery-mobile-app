package com.example.wagbaadminstrator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder{

    TextView orderId, deliveryTime, gateNumber, orderTime, status, totalAmount, paymentMethod;
    Button confirmOrder,processOrder, deliverOrder;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderId = itemView.findViewById(R.id.orderNumber);
        deliveryTime = itemView.findViewById(R.id.orderTime);
        gateNumber = itemView.findViewById(R.id.orderGate);
        orderTime = itemView.findViewById(R.id.orderDate);
        status = itemView.findViewById(R.id.orderStatus);
        totalAmount = itemView.findViewById(R.id.orderTotal);
        paymentMethod = itemView.findViewById(R.id.orderPaymentMethod);
        confirmOrder = itemView.findViewById(R.id.confirmButton);
        processOrder = itemView.findViewById(R.id.processButton);
        deliverOrder = itemView.findViewById(R.id.deliverButton);
    }
}
