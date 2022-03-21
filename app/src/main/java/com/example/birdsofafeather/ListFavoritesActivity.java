package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.Course;
import com.example.birdsofafeather.db.IAccount;

import java.util.ArrayList;
import java.util.List;

public class ListFavoritesActivity extends AppCompatActivity {
    protected static AppDatabase db;

    protected RecyclerView favoritesRecyclerView;
    protected RecyclerView.LayoutManager favoritesLayoutManager;
    protected AccountsViewAdapter favoritesViewAdapter;

    List<Account> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorites);
        setTitle("Favorites");

        // Temporary- add accounts and courses to database
        db = AppDatabase.singleton(this);


        favoritesRecyclerView = findViewById(R.id.favorites_view);

        favoritesLayoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(favoritesLayoutManager);

        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        String userAccountId = preferences.getString("user_account_id", "");
        Account userAccount = db.accountDao().get(userAccountId);

        favoriteList = new ArrayList<Account>();
        List<String> favoriteIdList = db.favoritesDao().getAll();
        for(String id : favoriteIdList)
        {
            favoriteList.add(db.accountDao().get(id));
        }

        favoritesViewAdapter = new AccountsViewAdapter(favoriteList);
        favoritesRecyclerView.setAdapter(favoritesViewAdapter);
    }

    public void onClickedGoBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        String userAccountId = preferences.getString("user_account_id", "");
        Account userAccount = db.accountDao().get(userAccountId);

        favoriteList = new ArrayList<Account>();
        List<String> favoriteIdList = db.favoritesDao().getAll();
        for(String id : favoriteIdList)
        {
            favoriteList.add(db.accountDao().get(id));
        }

        favoritesViewAdapter = new AccountsViewAdapter(favoriteList);
        favoritesRecyclerView.setAdapter(favoritesViewAdapter);
    }
}