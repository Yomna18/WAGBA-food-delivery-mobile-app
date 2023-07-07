package com.example.wagbaapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user_table WHERE user_id=:uid")
    User getUser(String uid);

    @Update
    void update(User user);
}
