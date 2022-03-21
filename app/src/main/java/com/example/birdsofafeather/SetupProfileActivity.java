package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.IAccount;

import java.net.MalformedURLException;

public class SetupProfileActivity extends AppCompatActivity {

    AppDatabase db;
    AccountDao accountDao;

    private String accountId;

    private TextView displayPhotoURL;
    private TextView nameDisplay;
    private ImageView photoView;
    private ProfilePicture userPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        db = AppDatabase.singleton(this);
        accountDao = db.accountDao();

        // Get user's account and setup name and photo URL fields accordingly
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        this.accountId = preferences.getString("user_account_id", "");
        IAccount userAccount = accountDao.get(accountId);
        String profilePictureURL = userAccount.getProfilePictureURL();

        nameDisplay = findViewById(R.id.textView2);
        nameDisplay.setText(userAccount.getName());

        displayPhotoURL = findViewById(R.id.photoURL);
        displayPhotoURL.setText(profilePictureURL);

        photoView = findViewById(R.id.imageView2);
        this.userPicture = new ProfilePicture(this, photoView, accountId, accountDao);
    }

    public void onConfirmProfileClicked(View view) throws MalformedURLException {
        userPicture.setPicture(displayPhotoURL.getText().toString());
    }

    public void onContinueClicked(View view) {
        Intent continueIntent = new Intent(this, CourseInsertActivity.class);
        startActivity(continueIntent);
    }

    public void onCancelClicked(View view) {
        nameDisplay = findViewById(R.id.textView2);
        nameDisplay.setText(accountDao.get(accountId).getName());

        displayPhotoURL = findViewById(R.id.photoURL);
        displayPhotoURL.setText("");

        userPicture.setPicture("");
    }
}