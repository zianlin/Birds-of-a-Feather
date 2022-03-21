package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchStudentSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_student_search);
    }


    public void onClickedLaunch(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ListBirdsOfAFeatherActivity.class);
        context.startActivity(intent);
    }

    public void onClickedEditCourses(View view) {
        Intent continueIntent = new Intent(this, CourseInsertActivity.class);
        startActivity(continueIntent);
    }

    public void onClickedViewFavorites(View view) {
        Intent continueIntent = new Intent(this, ListFavoritesActivity.class);
        startActivity(continueIntent);
    }
}