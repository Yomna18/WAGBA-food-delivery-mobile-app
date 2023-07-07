package com.example.wagbaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderStatus extends AppCompatActivity {

    TextView orderGateNumber, orderNumber;
    TextView orderConfirmed, orderConfirmed2, orderProcessed, orderProcessed2, orderDelivered;
    DatabaseReference databaseReference;
    String OrderId = "";
    String OrderGate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        orderGateNumber = (TextView) findViewById(R.id.orderGate);
        orderNumber = (TextView) findViewById(R.id.orderNumber);
        orderConfirmed = (TextView) findViewById(R.id.orderConfirmed);
        orderConfirmed2 = (TextView) findViewById(R.id.orderConfirmed2);
        orderProcessed = (TextView) findViewById(R.id.orderProcessed);
        orderProcessed2 = (TextView) findViewById(R.id.orderProcessed2);
        orderDelivered = (TextView) findViewById(R.id.orderDelivered);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllOrders");

        //Getting the intent from order history page
        if (getIntent() != null){
            OrderId = getIntent().getStringExtra("OrderId");
            OrderGate = getIntent().getStringExtra("OrderGate");
        }

        if (!OrderId.isEmpty() && OrderId != null)
            LoadOrderTrackingDetails(OrderId, OrderGate);


    }

    private void LoadOrderTrackingDetails(String orderId, String orderGate) {

        orderGateNumber.setText(orderGate);
        orderNumber.setText("#" + orderId);

        // Read from the database
        databaseReference.child(orderId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Status").getValue().toString().equals("Placed")){
                    orderConfirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                    orderConfirmed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_remaining));
                    orderProcessed.setBackground(getResources().getDrawable(R.drawable.shape_status_remaining));
                    orderProcessed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_remaining));
                    orderDelivered.setBackground(getResources().getDrawable(R.drawable.shape_status_remaining));

                }else if(dataSnapshot.child("Status").getValue().toString().equals("Confirmed")){
                    orderConfirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                    orderConfirmed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_completed));
                    orderProcessed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                    orderProcessed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_remaining));
                    orderDelivered.setBackground(getResources().getDrawable(R.drawable.shape_status_remaining));


                }else if(dataSnapshot.child("Status").getValue().toString().equals("Processed")){
                    orderConfirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                    orderConfirmed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_completed));
                    orderProcessed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                    orderProcessed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_completed));
                    orderDelivered.setBackground(getResources().getDrawable(R.drawable.shape_status_current));

                }else if(dataSnapshot.child("Status").getValue().toString().equals("Delivered")){
                    orderConfirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                    orderConfirmed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_completed));
                    orderProcessed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                    orderProcessed2.setBackground(getResources().getDrawable(R.drawable.shape_bar_completed));
                    orderDelivered.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

}