package com.example.wagbaadminstrator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText loginNumber, loginPassword;
    Button signUp, signIn;

    String phoneNumberPattern = "^01[0125][0-9]{8}$";
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginNumber = (EditText) findViewById(R.id.editTextEmail);
        loginPassword = (EditText) findViewById(R.id.editTextPassword);
        signIn = (Button) findViewById(R.id.signInButton);
        signUp = (Button) findViewById(R.id.signUpButton);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child("Accounts");



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(Login.this, Registeration.class);
                startActivity(intent01);
            }
        });


    }

    private void login() {
        String inputNumber = loginNumber.getText().toString();
        String inputPass = loginPassword.getText().toString();


        if(!inputNumber.matches(phoneNumberPattern)){
            loginNumber.setError("Please enter correct phone number");
            loginNumber.requestFocus();
        }else if(inputPass.isEmpty() || inputPass.length()<6){
            loginPassword.setError("Please enter password not less than 6 characters");
            loginPassword.requestFocus();
        }else {
            progressDialog.setMessage("Please wait while logging");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Read from the database
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(inputNumber).exists()){
                        if(inputPass.equals(dataSnapshot.child(inputNumber).child("Password").getValue().toString())){
                            progressDialog.dismiss();
                            sendToNewActivity();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_LONG).show();}
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });

        }
    }
    private void sendToNewActivity() {
        Intent intent02 = new Intent(this, HomePage.class);
        intent02.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent02);
        finish();
    }
}