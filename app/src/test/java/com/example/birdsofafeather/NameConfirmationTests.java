package com.example.birdsofafeather;

import org.checkerframework.checker.units.qual.A;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;

@RunWith(AndroidJUnit4.class)
public class NameConfirmationTests {

    @Test
    public void display_correct_name(){
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        AppDatabase db = AppDatabase.singleton(context);
        AccountDao accountDao = db.accountDao();

        Intent fromLogin = new Intent(context, SetupNameActivity.class);

        Account a1 = new Account("John Doe", "jd@gmail.com", "DEFAULT_PROFILE_PICTURE");
        accountDao.insert(a1);

        fromLogin.putExtra("displayName", "John Doe");
        fromLogin.putExtra("accountId", a1.getId());

        ActivityScenario<SetupNameActivity> scenario = ActivityScenario.launch(fromLogin);

        scenario.onActivity(activity -> {

            EditText displayNameText = activity.findViewById(R.id.display_name_textview);

            assertEquals("John Doe", displayNameText.getText().toString());
        });

        scenario.close();
    }
}
