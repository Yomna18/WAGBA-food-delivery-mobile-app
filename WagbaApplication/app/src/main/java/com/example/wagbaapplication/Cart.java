package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    Button proceedButton;
    TextView noItems;
    FirebaseRecyclerOptions<OrderObject> options;
    FirebaseRecyclerAdapter<OrderObject, OrderViewHolder> adapter;
    DatabaseReference databaseReference;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        proceedButton = (Button) findViewById(R.id.proceedToCheckout);
        noItems = (TextView) findViewById(R.id.noItems);
        recyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("CartList").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders");

        LoadCartList();
        CheckIfCartIsEmpty();

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this, ConfirmOrder.class);
                startActivity(intent);
            }
        });

    }

    private void CheckIfCartIsEmpty() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    noItems.setVisibility(View.VISIBLE);
                    proceedButton.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void LoadCartList() {
        options = new FirebaseRecyclerOptions.Builder<OrderObject>().setQuery(databaseReference,OrderObject.class).build();
        adapter = new FirebaseRecyclerAdapter<OrderObject, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderObject model) {
                holder.name.setText(model.getDishName());
                holder.price.setText("EGP "+model.getPrice());
                holder.quantity.setText(model.getQuantity());
                Picasso.get().load(model.getImage()).into(holder.image);

                //This is for each separate item on cart
                int price_of_one_item = Integer.valueOf(model.getPrice());
                int final_price_perItem = price_of_one_item*Integer.valueOf(model.getQuantity());
                holder.totalPrice.setText("EGP "+String.valueOf(final_price_perItem));

                holder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!model.getQuantity().equals("1")){
                            int currentQuantity = Integer.parseInt(model.getQuantity());
                            currentQuantity = currentQuantity - 1;
                            String current_quantity_string = String.valueOf(currentQuantity);
                            databaseReference
                                    .child(getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                    .child("Quantity").setValue(current_quantity_string);
                        }
                    }
                });

                holder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentQuantity = Integer.parseInt(model.getQuantity());
                        currentQuantity = currentQuantity + 1;
                        String current_quantity_string = String.valueOf(currentQuantity);
                        databaseReference
                                .child(getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                .child("Quantity").setValue(current_quantity_string);
                    }
                });


                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this)
                                .setMessage("Are you sure you want to delete this item?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        databaseReference.child(getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(Cart.this, "Item has been removed", Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       //do nothing
                                    }
                                });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view,parent,false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}