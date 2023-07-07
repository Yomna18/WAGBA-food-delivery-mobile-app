package com.example.wagbaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class Welcome extends AppCompatActivity {

    Button start;
    ProgressDialog progressDialog;
    FirebaseAuth myAuth;
    FirebaseUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        start = (Button) findViewById(R.id.getStartedButton);
        progressDialog = new ProgressDialog(this);
        myAuth = FirebaseAuth.getInstance();
        myUser = myAuth.getCurrentUser();

        Paper.init(this);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
            }
        });

        String emailKey = Paper.book().read(Prevalent.emailKey);
        String passwordKey = Paper.book().read(Prevalent.passwordKey);

        if(emailKey != "" && passwordKey != ""){
            if(!TextUtils.isEmpty(emailKey) && !TextUtils.isEmpty(passwordKey)){
                AllowAccess(emailKey, passwordKey);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void AllowAccess(String emailKey, String passwordKey) {

        progressDialog.setMessage("Please wait while logging");
        progressDialog.setTitle("Already logged in");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        myAuth.signInWithEmailAndPassword(emailKey,passwordKey).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendToNewActivity();
                }
            }
        });
    }

    private void sendToNewActivity() {
        Intent intent02 = new Intent(this, Restaurants.class);
        intent02.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent02);
        finish();
    }


}