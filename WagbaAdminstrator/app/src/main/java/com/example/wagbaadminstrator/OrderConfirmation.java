package com.example.wagbaadminstrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderConfirmation extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noItems;
    FirebaseRecyclerOptions<OrderObject> options;
    FirebaseRecyclerAdapter<OrderObject, OrderViewHolder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        noItems = (TextView) findViewById(R.id.noItems);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllOrders");

        LoadOrders();
        CheckIfOrdersIsEmpty();

    }

    private void CheckIfOrdersIsEmpty() {
        databaseReference.orderByChild("Status")
                .equalTo("Placed").addValueEventListener(new ValueEventListener() {
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

    private void LoadOrders() {
        options = new FirebaseRecyclerOptions.Builder<OrderObject>()
                .setQuery(databaseReference.orderByChild("Status")
                        .equalTo("Placed"), OrderObject.class).build();

        adapter = new FirebaseRecyclerAdapter<OrderObject, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderObject model) {

                holder.orderId.setText("Order Number #" + getRef(holder.getAbsoluteAdapterPosition()).getKey());
                holder.status.setText(model.getStatus());
                holder.orderTime.setText(model.getOrderTime());
                holder.gateNumber.setText(model.getGateNumber());
                holder.deliveryTime.setText(model.getDeliveryTime());
                holder.paymentMethod.setText(model.getPaymentMethod());
                holder.totalAmount.setText("EGP " + model.getTotalAmount());

                holder.confirmOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Order must be confirmed before 10:30 for noon delivery and before 1:30 for 3:00 pm delivery.
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("Hmm");
                        String currentTime = simpleDateFormat2.format(new Date());
                        String time1 = "600";
                        String time2 = "1030";
                        String time3 = "1330";

                        String testTime1 = "100";
                        String testTime2 = "500";


                        if((model.getDeliveryTime().equals("12:00 pm")) && (Integer.valueOf(currentTime) < Integer.valueOf(time1) || Integer.valueOf(currentTime) > Integer.valueOf(time2))){
                            Toast.makeText(OrderConfirmation.this, "To confirm a 12 pm order, you must confirm order between 6 am and 10:30 am", Toast.LENGTH_SHORT).show();
                        }else if((model.getDeliveryTime().equals("3:00 pm")) && (Integer.valueOf(currentTime) < Integer.valueOf(time1) || Integer.valueOf(currentTime) > Integer.valueOf(time3))){
                            Toast.makeText(OrderConfirmation.this, "To confirm a 3 pm order, you must confirm order between 6 am and 1:30 pm", Toast.LENGTH_SHORT).show();
                        }else{
                            databaseReference.child(getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                    .child("Status").setValue("Confirmed");
                            Toast.makeText(OrderConfirmation.this, "Order #"+getRef(holder.getAbsoluteAdapterPosition()).getKey().toString()+" has been confirmed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_view, parent, false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}