package com.example.wagbaapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Profile extends AppCompatActivity {

    Button signOutButton;
    TextView numberOfFavouriteDishes, profileNameTitle, profileMail, profileNumber, profileName,editProfile;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        signOutButton = (Button) findViewById(R.id.signOutButton);
        numberOfFavouriteDishes = (TextView) findViewById(R.id.numberOfFavouriteDishes);
        profileNameTitle = (TextView) findViewById(R.id.profileNameTitle);
        profileMail = (TextView) findViewById(R.id.profileMail);
        profileNumber = (TextView) findViewById(R.id.profileNumber);
        profileName = (TextView) findViewById(R.id.profileName);
        editProfile = (TextView) findViewById(R.id.editProfile);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllFavourites");

        //Getting favorites from Firebase
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String favoritesTotal = String.valueOf(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getChildrenCount());
                numberOfFavouriteDishes.setText(favoritesTotal+ " Dishes");
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        //Performing Room database query
        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        final UserDao userDao = userDatabase.userDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = userDao.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profileNameTitle.setText(user.getUserName());
                        profileName.setText(user.getUserName());
                        profileMail.setText(user.getUserEmail());
                        profileNumber.setText(user.getUserPhone());
                    }
                });
            }
        }).start();


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this)
                        .setMessage("Are you sure want to sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Profile.this, Login.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                builder.show();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, UpdateProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}