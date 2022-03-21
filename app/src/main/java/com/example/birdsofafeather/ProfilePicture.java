package com.example.birdsofafeather;

import android.app.Activity;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.db.AccountDao;

public class ProfilePicture {
    private Activity activity;
    private ImageView photo;
    private String accountId;
    private AccountDao accountDao;

    public ProfilePicture(Activity activity, ImageView photo, String accountId, AccountDao accountDao) {
        this.activity = activity;
        this.photo = photo;
        this.accountId = accountId;
        this.accountDao = accountDao;

        setPicture(accountDao.get(accountId).getProfilePictureURL());
    }

    public void setPicture(String newProfilePictureURL) {
        // Validate the URL and update the Database accordingly
        if (newProfilePictureURL.isEmpty() || URLUtil.isValidUrl(newProfilePictureURL)) {
            accountDao.updateProfilePictureURLFromId(accountId, newProfilePictureURL);
        } else {
            Utilities.showAlert(activity,"INVALID URL", "You must enter a valid URL!");
            return;
        }

        // Update the PhotoView, checking if the default pfp is needed
        if (newProfilePictureURL.isEmpty()) {
            photo.setImageResource(R.drawable.default_profile_picture);
        } else {
            Glide.with(activity).load(newProfilePictureURL).into(photo);
        }
    }
  }
