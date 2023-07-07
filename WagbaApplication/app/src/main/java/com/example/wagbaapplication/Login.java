package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button signUp, signIn;
    CheckBox rememberMe;

    String emailPattern = "(\\S*)@eng\\.asu\\.edu\\.eg";
    ProgressDialog progressDialog;
    FirebaseAuth myAuth;
    FirebaseUser myUser;
//    String UserName, PhoneNumber;
//    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.editTextEmail2);
        loginPassword = (EditText) findViewById(R.id.editTextPassword2);
        signIn = (Button) findViewById(R.id.signInButton);
        signUp = (Button) findViewById(R.id.signUpButton2);
        progressDialog = new ProgressDialog(this);
        myAuth = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        Paper.init(this);

        Intent intent01 = new Intent(this,Registeration.class);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent01);
            }
        });
    }

    private void login() {
        String inputEmail = loginEmail.getText().toString();
        String inputPass = loginPassword.getText().toString();

        if(rememberMe.isChecked()){
            Paper.book().write(Prevalent.emailKey,inputEmail);
            Paper.book().write(Prevalent.passwordKey,inputPass);
        }

        if(!inputEmail.matches(emailPattern)){
            loginEmail.setError("Please enter correct E-mail");
            loginEmail.requestFocus();
        }else if(inputPass.isEmpty() || inputPass.length()<6){
            loginPassword.setError("Please enter password not less than 6 characters");
            loginPassword.requestFocus();
        }else {
            progressDialog.setMessage("Please wait while logging");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            myAuth.signInWithEmailAndPassword(inputEmail,inputPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();

                        // Read from the database
//                        databaseReference.child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                UserName = dataSnapshot.child("UserName").getValue().toString();
//                                PhoneNumber = dataSnapshot.child("PhoneNumber").getValue().toString();
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError error) {
//                            }
//                        });

//                        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
//                        final UserDao userDao = userDatabase.userDao();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(!userDao.userExists(myUser.getUid())){
////                                    User user = new User(myUser.getUid(),UserName, myUser.getEmail(), PhoneNumber);
////                                    userDao.insert(user);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(Login.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        });

                        sendToNewActivity();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
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