package com.example.wagbaapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class UpdateProfile extends AppCompatActivity {

    EditText updateProfileEmail, updateProfileName, updateProfilePhone;
    Button saveChangesButton;
    String emailPattern = "(\\S*)@eng\\.asu\\.edu\\.eg";
    String phoneNumberPattern = "^01[0125][0-9]{8}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        updateProfileEmail = (EditText) findViewById(R.id.updateProfileEmail);
        updateProfileName = (EditText) findViewById(R.id.updateProfileName);
        updateProfilePhone = (EditText) findViewById(R.id.updateProfilePhone);
        saveChangesButton = (Button) findViewById(R.id.saveChangesButton);


        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                update();
            }
        });
    }

    private void update() {
        String inputEmail = updateProfileEmail.getText().toString();
        String inputUserName = updateProfileName.getText().toString();
        String inputPhoneNumber = updateProfilePhone.getText().toString();

        if(!inputEmail.matches(emailPattern)){
            updateProfileEmail.setError("Please enter correct active faculty email");
            updateProfileEmail.requestFocus();
        }else if(inputUserName.isEmpty()){
            updateProfileName.setError("Please enter your name");
            updateProfileName.requestFocus();
        }else if(!inputPhoneNumber.matches(phoneNumberPattern)){
            updateProfilePhone.setError("Please enter correct phone number");
            updateProfilePhone.requestFocus();
        }else{

            //update email in Firebase
            FirebaseAuth.getInstance().getCurrentUser().updateEmail(inputEmail);

            //update in Room database
            // 1) creating new user
            User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), inputUserName, inputEmail, inputPhoneNumber);

            // 2) Initializing the database
            UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());

            // 3) Initializing the Dao
            final UserDao userDao = userDatabase.userDao();

            // 4) Inserting to database in a new thread so we don't block the UI
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userDao.update(user);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateProfile.this, "Profile has been updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();

            Intent intent = new Intent(UpdateProfile.this, Profile.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this)
                .setMessage("Are you sure want to exit without saving your changes")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(UpdateProfile.this, Profile.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        builder.show();
    }
}