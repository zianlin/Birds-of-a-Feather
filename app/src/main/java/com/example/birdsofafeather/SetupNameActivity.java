package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SetupNameActivity extends AppCompatActivity {

    AppDatabase db;
    AccountDao accountDao;

    private TextView displayNameView; // The EditText view that will display their name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_name);

        db = AppDatabase.singleton(this);
        accountDao = db.accountDao();

        // Fetch user's account id
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        String accountId = preferences.getString("user_account_id", "");

        // Fetch name associated with user's Google Account from the LoginActivity
        Intent intent = getIntent();
        String displayName = intent.hasExtra("displayName") ? intent.getStringExtra("displayName") : "";

        // Show the user's Google display name
        displayNameView = findViewById(R.id.display_name_textview);
        displayNameView.setText(displayName);
    }


    // Confirm Name Button
    public void onConfirmNameClicked(View view){

        // Validate that the display name text field is NOT empty
        if (displayNameView.getText().toString().isEmpty()){
            Utilities.showAlert(this,"NO NAME ENTERED", "You must enter a preferred name!");
        } else {
            // Create intent to swap activities
            Intent continueIntent = new Intent(this, SetupProfileActivity.class);

            // Fetch user's account id
            SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
            String accountId = preferences.getString("user_account_id", "");

            // Update account name
            String preferredName = displayNameView.getText().toString();
            accountDao.updateNameFromId(accountId, preferredName);

            // Launch Activity
            startActivity(continueIntent);
        }
    }
}