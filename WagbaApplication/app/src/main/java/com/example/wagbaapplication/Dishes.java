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
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

public class Dishes extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<DishObject> options;
    FirebaseRecyclerAdapter<DishObject, DishViewHolder> adapter;
    DatabaseReference databaseReference;
    String RestaurantId = "";
    String RestaurantName = "";
    TextView RestaurantTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);

        RestaurantTitle = (TextView) findViewById(R.id.dishesListTitle);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //Getting the intent from restaurant page
        if (getIntent() != null) {
            RestaurantId = getIntent().getStringExtra("RestaurantId");
            RestaurantName = getIntent().getStringExtra("RestaurantName");
        }

        if (!RestaurantId.isEmpty() && RestaurantId != null)
            LoadDishes(RestaurantId);

    }

    private void LoadDishes(String restaurantId) {
        RestaurantTitle.setText(RestaurantName+ " Menu");

        // Select * from dishes where DishId == Restaurant Id
        options = new FirebaseRecyclerOptions.Builder<DishObject>().setQuery(databaseReference.child("Dishes").orderByChild("DishId").equalTo(restaurantId),DishObject.class).build();
        adapter = new FirebaseRecyclerAdapter<DishObject, DishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DishViewHolder holder, int position, @NonNull DishObject model) {

                holder.name.setText(model.getName());
                holder.availability.setText(model.getAvailability());
                Picasso.get().load(model.getImage()).into(holder.image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Dishes.this, DishDetail.class);
                        intent.putExtra("DishId",getRef(holder.getAbsoluteAdapterPosition()).getKey());
                        startActivity(intent);
                    }
                });

                holder.favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference myRef = databaseReference.child("AllFavourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getRef(holder.getAbsoluteAdapterPosition()).getKey());

                        myRef.child("Image").setValue(model.getImage());
                        myRef.child("DishName").setValue(model.getName());
                        myRef.child("Price").setValue(model.getPrice());

                        Toast.makeText(Dishes.this, "Dish has been added to favourites", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_view,parent,false);
                return new DishViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}