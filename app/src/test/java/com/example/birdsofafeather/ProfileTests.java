package com.example.birdsofafeather;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;


import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;

@RunWith(AndroidJUnit4.class)
public class ProfileTests {
    @Test
    public void display_correct_profile(){
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        AppDatabase db = AppDatabase.singleton(context);
        AccountDao accountDao = db.accountDao();

        Intent fromLogin = new Intent(context, SetupProfileActivity.class);
        String profileUrl = "https://images.unsplash.com/photo-1508921912186-1d1a45ebb3c1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cGhvdG98ZW58MHx8MHx8&w=1000&q=80";
        Account a1 = new Account("John Doe", "jd@gmail.com", profileUrl);
        accountDao.insert(a1);

        fromLogin.putExtra("PhotoURL", profileUrl);

        // Write account ID to sharedPreferences
        SharedPreferences preferences = getInstrumentation().getContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_account_id", a1.getId());
        editor.apply();

        ActivityScenario<SetupProfileActivity> scenario = ActivityScenario.launch(fromLogin);

        scenario.onActivity(activity -> {
            EditText displayURLText = activity.findViewById(R.id.photoURL);

            assertEquals(profileUrl, displayURLText.getText().toString());
        });

        scenario.close();
    }
}
