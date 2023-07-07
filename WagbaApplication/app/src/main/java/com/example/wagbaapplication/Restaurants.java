package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Restaurants extends AppCompatActivity {

    EditText searchRestaurant;
    RecyclerView recyclerView;
    TextView profile, cart, orderHistory, favourites;
    FirebaseRecyclerOptions<RestaurantObject> options;
    FirebaseRecyclerAdapter<RestaurantObject, RestaurantViewHolder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        searchRestaurant = (EditText) findViewById(R.id.searchRestaurant);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        profile = (TextView) findViewById(R.id.profileButton);
        cart = (TextView) findViewById(R.id.cartButton);
        orderHistory = (TextView) findViewById(R.id.orderHistoryButton);
        favourites = (TextView) findViewById(R.id.favouritesButton);

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Restaurants.this, OrderHistory.class);
                startActivity(intent);
            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Restaurants.this, Cart.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Restaurants.this, Profile.class);
                startActivity(intent);
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Restaurants.this, Favourites.class);
                startActivity(intent);
            }
        });


        
        LoadRestaurants("");

        searchRestaurant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null)
                    LoadRestaurants(editable.toString());
                else
                    LoadRestaurants("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Restaurants.this)
                .setMessage("Are you want to exit the application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        builder.show();

    }

    private void LoadRestaurants(String data) {
        Query query = databaseReference.orderByChild("Name").startAt(data).endAt(data+"\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<RestaurantObject>().setQuery(query,RestaurantObject.class).build(); //here we set the query we want to make from the database
        adapter = new FirebaseRecyclerAdapter<RestaurantObject, RestaurantViewHolder>(options) { //the adapter use the options to know which objects to bind based on the query you made
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull RestaurantObject model) {

                holder.name.setText(model.getName());
                holder.rating.setText(model.getRating());
                Picasso.get().load(model.getImage()).into(holder.image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Restaurants.this, Dishes.class);
                        intent.putExtra("RestaurantId",getRef(holder.getAbsoluteAdapterPosition()).getKey());
                        intent.putExtra("RestaurantName",model.getName());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_view,parent,false);
                return new RestaurantViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}