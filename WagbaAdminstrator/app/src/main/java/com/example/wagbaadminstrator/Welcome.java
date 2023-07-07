package com.example.wagbaadminstrator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Welcome extends AppCompatActivity {

    EditText adminCode;
    Button getStartedButton;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        adminCode = (EditText) findViewById(R.id.adminCode);
        getStartedButton = (Button) findViewById(R.id.getStartedButton);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputCode = adminCode.getText().toString();


                progressDialog.setMessage("Please wait checking the admin code");
                progressDialog.setTitle("Admin code");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.child("Key").getValue().toString();
                        if(inputCode.equals(value)){

                            progressDialog.dismiss();
                            Intent intent = new Intent(Welcome.this, Login.class);
                            startActivity(intent);

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Welcome.this, "Incorrect admin code", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}