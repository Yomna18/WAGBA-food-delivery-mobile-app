package com.example.wagbaadminstrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button orderConfirmation, orderProcessing, orderDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        orderConfirmation = (Button) findViewById(R.id.orderConfirmation);
        orderProcessing = (Button) findViewById(R.id.orderProcessing);
        orderDelivery = (Button) findViewById(R.id.orderDelivery);

        orderConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, OrderConfirmation.class);
                startActivity(intent);
            }
        });

        orderProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, OrderProcessing.class);
                startActivity(intent);
            }
        });

        orderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, OrderDelivery.class);
                startActivity(intent);
            }
        });
    }
}