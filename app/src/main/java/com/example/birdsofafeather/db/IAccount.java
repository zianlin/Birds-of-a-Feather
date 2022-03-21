package com.example.birdsofafeather.db;

import java.util.List;

public interface IAccount {
    String getId();
    void setId(String id);
    String getName();
    void setName(String name);
    String getEmail();
    void setEmail(String email);
    String getProfilePictureURL();
    void setProfilePictureURL(String profilePictureURL);
    boolean equals(IAccount other);
}