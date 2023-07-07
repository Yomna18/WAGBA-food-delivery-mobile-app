package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderHistoryContent extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<OrderObject> options;
    FirebaseRecyclerAdapter<OrderObject, OrderHistoryContentViewHolder> adapter;
    DatabaseReference databaseReference;
    String OrderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_content);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllOrdersContent");

        //Getting the intent from order history page
        if (getIntent() != null)
            OrderId = getIntent().getStringExtra("OrderId");

        if (!OrderId.isEmpty() && OrderId != null)
            LoadOrderHistoryDetails(OrderId);


    }

    private void LoadOrderHistoryDetails(String orderId) {
        options = new FirebaseRecyclerOptions.Builder<OrderObject>().setQuery(databaseReference.child(orderId), OrderObject.class).build();
        adapter = new FirebaseRecyclerAdapter<OrderObject, OrderHistoryContentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryContentViewHolder holder, int position, @NonNull OrderObject model) {

                holder.name.setText(model.getDishName());
                holder.price.setText("EGP "+model.getPrice());
                holder.quantity.setText("x"+model.getQuantity());
                Picasso.get().load(model.getImage()).into(holder.image);
                holder.totalPrice.setText("EGP "+String.valueOf(Integer.valueOf(model.getPrice())*Integer.valueOf(model.getQuantity())));
            }

            @NonNull
            @Override
            public OrderHistoryContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_content_view,parent,false);
                return new OrderHistoryContentViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}