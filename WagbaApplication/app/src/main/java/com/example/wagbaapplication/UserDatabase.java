package com.example.wagbaapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String dbName = "user_table";
    private static UserDatabase usersDatabase;

    public static synchronized UserDatabase getUserDatabase(Context context){

        if(usersDatabase == null){
            usersDatabase = Room.databaseBuilder(context, UserDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return usersDatabase;
    }
    public abstract UserDao userDao();
}

