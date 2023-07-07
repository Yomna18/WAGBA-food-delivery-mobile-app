package com.example.wagbaadminstrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Registeration extends AppCompatActivity {

    EditText registerEmail, registerPassword, confirmPassword, registerUserName, registerPhoneNumber;
    Button signUp, signIn;
    String emailPattern = "(\\S*)@eng\\.asu\\.edu\\.eg";
    String phoneNumberPattern = "^01[0125][0-9]{8}$";
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        registerEmail = (EditText) findViewById(R.id.editTextEmail2);
        registerPassword = (EditText) findViewById(R.id.editTextPassword2);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPass);
        registerUserName = (EditText) findViewById(R.id.editTextUserName);
        registerPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        signUp = (Button) findViewById(R.id.signUpButton2);
        signIn = (Button) findViewById(R.id.signInButton2);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child("Accounts");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registeration.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void register() {
        String inputEmail = registerEmail.getText().toString();
        String inputPass = registerPassword.getText().toString();
        String confirmPass = confirmPassword.getText().toString();
        String inputUserName = registerUserName.getText().toString();
        String inputPhoneNumber = registerPhoneNumber.getText().toString();

        if(!inputEmail.matches(emailPattern)){
            registerEmail.setError("Please enter correct active faculty email");
            registerEmail.requestFocus();
        }else if(inputPass.isEmpty() || inputPass.length()<6){
            registerPassword.setError("Please enter password not less than 6 characters");
            registerPassword.requestFocus();
        }else if(!inputPass.equals(confirmPass)){
            confirmPassword.setError("Password does not match");
            confirmPassword.requestFocus();
        }else if(inputUserName.isEmpty()){
            registerUserName.setError("Please enter your name");
            registerUserName.requestFocus();
        }else if(!inputPhoneNumber.matches(phoneNumberPattern)){
            registerPhoneNumber.setError("Please enter correct phone number");
            registerPhoneNumber.requestFocus();
        }else{
            progressDialog.setMessage("Please wait while registering");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!(dataSnapshot.hasChild(inputPhoneNumber))){
                        HashMap<String, Object> adminAccount = new HashMap<>();
                        adminAccount.put("Password", inputPass);
                        adminAccount.put("Name", inputUserName);
                        adminAccount.put("Email", inputEmail);
                        databaseReference.child(inputPhoneNumber).updateChildren(adminAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    sendToNewActivity();
                                }
                            }
                        });
                    }
                    else{
                        progressDialog.dismiss();
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