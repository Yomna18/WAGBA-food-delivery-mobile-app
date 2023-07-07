package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noItems;
    FirebaseRecyclerOptions<OrderHistoryObject> options;
    FirebaseRecyclerAdapter<OrderHistoryObject, OrderHistoryViewHolder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        noItems = (TextView) findViewById(R.id.noItems);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllOrders");

        LoadHistory();
        CheckIfHistoryIsEmpty();


    }

    private void CheckIfHistoryIsEmpty() {
        databaseReference.orderByChild("OrderUserId")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    noItems.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void LoadHistory() {
        options = new FirebaseRecyclerOptions.Builder<OrderHistoryObject>()
                .setQuery(databaseReference.orderByChild("OrderUserId")
                        .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), OrderHistoryObject.class).build();

        adapter = new FirebaseRecyclerAdapter<OrderHistoryObject, OrderHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position, @NonNull OrderHistoryObject model) {

                holder.orderId.setText("Order Number #" + getRef(holder.getAbsoluteAdapterPosition()).getKey());
                holder.status.setText(model.getStatus());
                holder.orderTime.setText(model.getOrderTime());
                holder.gateNumber.setText(model.getGateNumber());
                holder.deliveryTime.setText(model.getDeliveryTime());
                holder.paymentMethod.setText(model.getPaymentMethod());
                holder.totalAmount.setText("EGP " + model.getTotalAmount());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderHistory.this, OrderHistoryContent.class);
                        intent.putExtra("OrderId",getRef(holder.getAbsoluteAdapterPosition()).getKey());
                        startActivity(intent);
                    }
                });

                holder.trackOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderHistory.this, OrderStatus.class);
                        intent.putExtra("OrderId",getRef(holder.getAbsoluteAdapterPosition()).getKey());
                        intent.putExtra("OrderGate",model.getGateNumber());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view, parent, false);
                return new OrderHistoryViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}




