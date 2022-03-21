package com.example.birdsofafeather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao
{
    @Query("SELECT * FROM accounts")
    List<Account> getAll();

    @Query("SELECT * FROM accounts WHERE id=:id")
    Account get(String id);

    @Query("SELECT COUNT(*) FROM accounts")
    int count();

    @Insert
    void insert(Account account);

    @Query("UPDATE accounts SET name = :name WHERE id = :id")
    void updateNameFromId(String id, String name);

    @Query("UPDATE accounts SET profile_picture_URL = :profilePictureURL WHERE id = :id")
    void updateProfilePictureURLFromId(String id, String profilePictureURL);

    @Update
    void update(Account account);

    @Delete
    void delete(Account account);
}