package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class DishDetail extends AppCompatActivity {

    TextView dishTitle, dishPrice, dishQuantity, dishDescription;
    ImageView dishImage, minus, plus;
    Button addToCart;
    DatabaseReference databaseReference;
    String DishId = "";
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        dishTitle = (TextView) findViewById(R.id.dishDetailTitle);
        dishPrice = (TextView) findViewById(R.id.dishDetailPrice);
        dishQuantity = (TextView) findViewById(R.id.orderQuantity);
        dishDescription = (TextView) findViewById(R.id.dishDescription);
        dishImage = (ImageView) findViewById(R.id.dishDetailImage);
        minus = (ImageView) findViewById(R.id.minus);
        plus = (ImageView) findViewById(R.id.plus);
        addToCart = (Button) findViewById(R.id.addCart);

        //activating the plus and minus buttons
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = dishQuantity.getText().toString();
                if(!quantity.equals("1")){
                    int quantityNumber = Integer.parseInt(quantity);
                    quantityNumber = quantityNumber - 1;
                    dishQuantity.setText(String.valueOf(quantityNumber));
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = dishQuantity.getText().toString();
                int quantityNumber = Integer.parseInt(quantity);
                quantityNumber = quantityNumber + 1;
                dishQuantity.setText(String.valueOf(quantityNumber));
            }
        });

        //Getting the intent from dish page
        if (getIntent() != null)
            DishId = getIntent().getStringExtra("DishId");

        if (!DishId.isEmpty() && DishId != null)
            LoadDishesDetails(DishId);

        databaseReference.child("Dishes").child(DishId).child("Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                imageURL = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DishId.isEmpty() && DishId != null)
                    addToCartListFireBase(DishId);
            }
        });

    }

    private void addToCartListFireBase(String dishId) {
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("DishName", dishTitle.getText().toString());
        cartMap.put("Quantity", dishQuantity.getText().toString());
        cartMap.put("Price", dishPrice.getText().toString());
        cartMap.put("Image", imageURL);

        databaseReference.child("CartList").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Orders")
                .child(dishId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DishDetail.this, "Order has been added to the cart", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void LoadDishesDetails(String dishId) {
        databaseReference.child("Dishes").child(dishId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //DishObject contains same key values of each dish that in the firebase

                DishObject dishObject = dataSnapshot.getValue(DishObject.class);

                dishTitle.setText(dishObject.getName());
                dishPrice.setText(dishObject.getPrice());
                dishDescription.setText(dishObject.getDescription());
                Picasso.get().load(dishObject.getImage()).into(dishImage);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }
}