package com.example.birdsofafeather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.Course;
import com.example.birdsofafeather.db.Favorites;
import com.example.birdsofafeather.db.IAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailActivity extends AppCompatActivity {
    private static AppDatabase db;
    private IAccount account;

    private ImageView profilePictureView;

    private Button favoriteButton;
    private Button waveButton;
    private boolean favorited;
    private boolean waved;

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;

    private String userAccountId;
    private String accountId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        Intent intent = getIntent();
        accountId = intent.getExtras().getString("account_id", ""); // Default value should never be returned

        db = AppDatabase.singleton(this);
        account = db.accountDao().get(accountId);

        // Set up profile picture
        profilePictureView = findViewById(R.id.profile_picture_view);
        String profilePictureURL = account.getProfilePictureURL();
        if (profilePictureURL.equals("")) {
            this.profilePictureView.setImageResource(R.drawable.default_profile_picture);
        } else {
            Glide.with(this).load(profilePictureURL).into(this.profilePictureView);
        }

        setTitle(account.getName());

        // Get courses shared with the user
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        userAccountId = preferences.getString("user_account_id", "");

        // Filter for shared courses only
        List<Integer> sharedCourseIds = db.joinDao().getSharedCourseIds(userAccountId, accountId);
        List<Course> sharedCourses = new ArrayList<Course>();
        for(int id : sharedCourseIds) {
            sharedCourses.add(db.courseDao().get(id));
        }

        //Initialize favorite Button
        favorited = db.favoritesDao().checkFavorited( userAccountId ,accountId);
        favoriteButton = findViewById(R.id.favorite_button);
        if(favorited){
            favoriteButton.setText("Unfavorite");
        }
        else{
            favoriteButton.setText("favorite");
        }

        //Initialize Wave Button
        // TODO - get waved status from db
        // waved = (grab from account)
        waved = false; //TEMP, REPLACE WITH ABOVE STATEMENT!
        waveButton = findViewById(R.id.wave_button);
        if(waved){
            waveButton.setEnabled(false);
            waveButton.setText("waved");
        }
        else{
            waveButton.setEnabled(true);
            waveButton.setText("wave");
        }

        coursesRecyclerView = findViewById(R.id.shared_courses_view);

        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        coursesViewAdapter = new CoursesViewAdapter(sharedCourses);
        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }

    public void onGoBackClicked(View view) { finish(); }

    public void onClickedFavorite(View view) {
        if(favorited){
            Toast.makeText(AccountDetailActivity.this, "Unfavorited: " + account.getName(), Toast.LENGTH_SHORT).show();
            favorited = false;
            favoriteButton.setText("favorite");
            db.favoritesDao().delete(userAccountId ,accountId);
        }
        else{
            Toast.makeText(AccountDetailActivity.this, "Favorited: " + account.getName(), Toast.LENGTH_SHORT).show();
            favorited = true;
            favoriteButton.setText("Unfavorite");
            db.favoritesDao().insert(new Favorites(userAccountId ,accountId));
        }
    }

    public void onClickedWave(View view) {
        waveButton.setText("waved");
        waveButton.setEnabled(false);
        waved = true;
        Toast.makeText(AccountDetailActivity.this, "Waved To: " + account.getName(), Toast.LENGTH_SHORT).show();
        //TODO - Send the actual wave (Mock waving)
    }
}