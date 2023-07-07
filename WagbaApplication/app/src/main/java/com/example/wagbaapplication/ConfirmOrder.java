package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmOrder extends AppCompatActivity {

    Spinner gateNumber, deliveryTime;
    RadioGroup paymentMethod;
    RadioButton paymentCash;
    RadioButton paymentCard;
    String selectedRadioButton;
    EditText cardNumber,cardExpirationDate, cardCVV;
    TextView subTotal, deliveryFee, orderTotalPrice;
    Button confirmOrder;
    DatabaseReference databaseReference;
    final int deliveryCost = 22;
    String expirationDatePattern = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$";
    String gate, timeToDeliver, orderTime;
    int Total;
    long maxId = 0;
    int CartTotal = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        paymentMethod = (RadioGroup) findViewById(R.id.radioGroup);
        paymentCash = (RadioButton) findViewById(R.id.payCash);
        paymentCard = (RadioButton) findViewById(R.id.payCard);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        cardExpirationDate = (EditText) findViewById(R.id.cardExpirationDate);
        cardCVV = (EditText) findViewById(R.id.cardCVV);
        subTotal = (TextView) findViewById(R.id.subTotal);
        deliveryFee = (TextView) findViewById(R.id.deliveryFee);
        orderTotalPrice = (TextView) findViewById(R.id.orderTotalPrice);
        confirmOrder = (Button) findViewById(R.id.confirmOrderButton);

        // Read from the database
        databaseReference.child("CartList").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String priceString = String.valueOf(snapshot.child("Price").getValue());
                            String quantityString = String.valueOf(snapshot.child("Quantity").getValue());
                            CartTotal = CartTotal +(Integer.valueOf(priceString)*Integer.valueOf(quantityString));

                        }
                        subTotal.setText("EGP "+ String.valueOf(CartTotal));
                        orderTotalPrice.setText("EGP "+ String.valueOf(deliveryCost+CartTotal));
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });



        gateNumber = findViewById(R.id.gateNumber);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.gate, R.layout.spinner_selected_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gateNumber.setAdapter(adapter1);

        deliveryTime = findViewById(R.id.deliveryTime);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.timeDelivery, R.layout.spinner_selected_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryTime.setAdapter(adapter2);

        // Read from the database
        databaseReference.child("AllOrders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId=(dataSnapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });




        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOrderConfirmation();
            }
        });

        cardNumber.setEnabled(false);
        cardExpirationDate.setEnabled(false);
        cardCVV.setEnabled(false);

        paymentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber.setEnabled(true);
                cardExpirationDate.setEnabled(true);
                cardCVV.setEnabled(true);
            }
        });

        paymentCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber.setEnabled(false);
                cardExpirationDate.setEnabled(false);
                cardCVV.setEnabled(false);
            }
        });




    }


    private void CheckOrderConfirmation() {
        gate = gateNumber.getSelectedItem().toString();
        timeToDeliver = deliveryTime.getSelectedItem().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        orderTime = simpleDateFormat.format(new Date());

        //Here we get the current time in the 24 hours format, while HH:mm a gives you format of 12:00 pm
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("Hmm");
        String currentTime = simpleDateFormat2.format(new Date());
        String time1 = "600";
        String time2 = "1000";
        String time3 = "1300";

        String testTime1 = "100";
        String testTime2 = "500";

        if((timeToDeliver.equals("12:00 pm")) && (Integer.valueOf(currentTime) < Integer.valueOf(time1) || Integer.valueOf(currentTime) > Integer.valueOf(time2))){
            Toast.makeText(this,"To deliver at 12 pm, you must order between 6 am and 10 am", Toast.LENGTH_LONG).show();
        }else if((timeToDeliver.equals("3:00 pm")) && (Integer.valueOf(currentTime) < Integer.valueOf(time1) || Integer.valueOf(currentTime) > Integer.valueOf(time3))){
            Toast.makeText(this,"To deliver at 3 pm, you must order between 6 am and 1 pm", Toast.LENGTH_LONG).show();
        }else if(paymentMethod.getCheckedRadioButtonId() == -1){
            paymentMethod.requestFocus();
            Toast.makeText(this,"Please select a payment method", Toast.LENGTH_LONG).show();
        }else if(paymentCard.isChecked() && (cardNumber.getText().toString().length()<16)){
            cardNumber.setError("Please enter correct card number");
            cardNumber.requestFocus();
        }else if(paymentCard.isChecked() && (cardCVV.getText().toString().length()<3)){
            cardCVV.setError("Please enter correct cvv number");
            cardCVV.requestFocus();
        }else if(paymentCard.isChecked() && (!cardExpirationDate.getText().toString().matches(expirationDatePattern))){
            cardExpirationDate.setError("Please enter correct expiration date MM/YY");
            cardExpirationDate.requestFocus();
        }else{ //now all the validations are over, here we will add data to firebase
            addDataToAllOrders();
        }


    }

    private void addDataToAllOrders() {
        maxId += 1;
        copyOrders();

        if(paymentCard.isChecked()){
            selectedRadioButton = "Payment with card";
        }else if(paymentCash.isChecked()){
            selectedRadioButton = "Payment with cash";
        }

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("OrderUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        orderMap.put("GateNumber", gate);
        orderMap.put("DeliveryTime", timeToDeliver);
        orderMap.put("PaymentMethod", selectedRadioButton);
        orderMap.put("Status", "Placed");
        orderMap.put("OrderTime", orderTime);
        orderMap.put("TotalAmount", String.valueOf(deliveryCost+CartTotal));

        databaseReference.child("AllOrders").
                child(String.valueOf(maxId))
                .updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseReference.child("CartList").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Orders").removeValue();


                        CharSequence options[] = new CharSequence[]
                                {
                                        "Return"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOrder.this);
                        builder.setTitle("Your order has been placed");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    Intent intent = new Intent(ConfirmOrder.this, Restaurants.class);
                                    intent.putExtra("OrderId", maxId);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });

    }

    private void copyOrders() {
        DatabaseReference cartListOrderNode = databaseReference.child("CartList").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Orders");
        DatabaseReference AllOrdersContentNode = databaseReference.child("AllOrdersContent").child(String.valueOf(maxId));

        // Read from the database
        cartListOrderNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot cartCode: dataSnapshot.getChildren()){
                    String cartCodeKey = cartCode.getKey();
                    String DishName = cartCode.child("DishName").getValue(String.class);
                    String Image = cartCode.child("Image").getValue(String.class);
                    String Price = cartCode.child("Price").getValue(String.class);
                    String Quantity = cartCode.child("Quantity").getValue(String.class);

                    AllOrdersContentNode.child(cartCodeKey).child("DishName").setValue(DishName);
                    AllOrdersContentNode.child(cartCodeKey).child("Image").setValue(Image);
                    AllOrdersContentNode.child(cartCodeKey).child("Price").setValue(Price);
                    AllOrdersContentNode.child(cartCodeKey).child("Quantity").setValue(Quantity);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }


}