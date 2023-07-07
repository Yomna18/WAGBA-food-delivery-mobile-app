package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Favourites extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noItems;
    FirebaseRecyclerOptions<OrderObject> options;
    FirebaseRecyclerAdapter<OrderObject, FavouriteViewHolder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        noItems = (TextView) findViewById(R.id.noItems);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("AllFavourites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        LoadFavouritesList();
        CheckIfFavouritesIsEmpty();
    }

    private void CheckIfFavouritesIsEmpty() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    noItems.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void LoadFavouritesList() {
        options = new FirebaseRecyclerOptions.Builder<OrderObject>().setQuery(databaseReference,OrderObject.class).build();
        adapter = new FirebaseRecyclerAdapter<OrderObject, FavouriteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position, @NonNull OrderObject model) {

                holder.name.setText(model.getDishName());
                holder.price.setText("EGP "+model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Favourites.this, DishDetail.class);
                        intent.putExtra("DishId",getRef(holder.getAbsoluteAdapterPosition()).getKey());
                        startActivity(intent);
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this)
                                .setMessage("Are you sure you want to delete this item?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        databaseReference.child(getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(Favourites.this, "Item has been removed", Toast.LENGTH_LONG).show();
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
            public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_view,parent,false);
                return new FavouriteViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}