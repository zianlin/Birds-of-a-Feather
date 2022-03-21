package com.example.birdsofafeather.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "accounts")
public class Account implements IAccount
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "profile_picture_URL")
    private String profilePictureURL;

    public Account(String name, String email, String profilePictureURL)
    {
        this.id = email;
        this.name = name;
        this.email = email;
        this.profilePictureURL = profilePictureURL;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void setName(String name) { this.name = name; }

    @Override
    public String getEmail()
    {
        return this.email;
    }

    @Override
    public void setEmail(String email) { this.email = email; }

    @Override
    public String getProfilePictureURL()
    {
        return this.profilePictureURL;
    }

    @Override
    public void setProfilePictureURL(String profilePictureURL) { this.profilePictureURL = profilePictureURL; }

    @Override
    public boolean equals(IAccount other)
    {
        return this.id.equals(other.getId());
    }
}
