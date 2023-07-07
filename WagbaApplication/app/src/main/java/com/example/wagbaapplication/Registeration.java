package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registeration extends AppCompatActivity {

    EditText registerEmail, registerPassword, confirmPassword, registerUserName, registerPhoneNumber;
    Button signUp, signIn;


      //normal email pattern
//    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //eng.asu.edu.eg email pattern
    String emailPattern = "(\\S*)@eng\\.asu\\.edu\\.eg";
    String phoneNumberPattern = "^01[0125][0-9]{8}$";
    ProgressDialog progressDialog;
    FirebaseAuth myAuth;
    FirebaseUser myUser;
//    DatabaseReference databaseReference;


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
        myAuth = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        Intent intent01 = new Intent(this, Login.class);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent01);
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

            myAuth.createUserWithEmailAndPassword(inputEmail,inputPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();

//                        final HashMap<String, Object> userMap = new HashMap<>();
//                        userMap.put("UserName", inputUserName);
//                        userMap.put("PhoneNumber", inputPhoneNumber);
//                        databaseReference.child(myUser.getUid()).updateChildren(userMap);


                        //Adding to user info to Room database

                        // 1) creating new user
                        User user = new User(myUser.getUid(), inputUserName, inputEmail, inputPhoneNumber);

                        // 2) Initializing the database
                        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());

                        // 3) Initializing the Dao
                        final UserDao userDao = userDatabase.userDao();

                        // 4) Inserting to database in a new thread so we don't block the UI
                        new Thread(new Runnable() {
                            @Override
                            public void run(){
                                userDao.insert(user);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Registeration.this, "You have registered successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();

                        sendToNewActivity();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Registeration.this, "Account with the same email is already made", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private void sendToNewActivity() {
        Intent intent02 = new Intent(this, Restaurants.class);
        intent02.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent02);
        finish();
    }
}