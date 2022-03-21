package com.example.birdsofafeather;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.CourseDao;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase db;
    AccountDao accountDao;

    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestPermissions();

        AppDatabase.useTestSingleton(this);
        db = AppDatabase.singleton(this);
        accountDao = db.accountDao();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Click listener for buttons
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.continue_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.continue_button:
                continueSetup();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // deprecated, but still works
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    // Launched upon pressing the "Continue Button"
    private void continueSetup() {

        // create intent to swap activities
        Intent continueIntent = new Intent(this, SetupNameActivity.class);

        // Fetch name associated with user's Google Account
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        String userDisplayName = googleAccount.getDisplayName();
        String userEmail = googleAccount.getEmail();
        String userUrl = "";
        if (googleAccount.getPhotoUrl() != null) {
            userUrl = googleAccount.getPhotoUrl().toString();
        }
        // Save new account to database
        Account myAccount = new Account(userDisplayName, userEmail, userUrl);
        accountDao.insert(myAccount);

        // Set the currently logged in user to the new account
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_account_id", myAccount.getId());
        editor.apply();

        // Send userDisplayName to the SetupNameActivity
        continueIntent.putExtra("displayName", userDisplayName);

        // Launch Activity
        startActivity(continueIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /*
    // For verifying sign-in
    ActivityResultLauncher<Intent> arl = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                TextView displayNameView = findViewById(R.id.textView5);
                displayNameView.setText(String.valueOf(result.getResultCode()));
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            }
        }
    );
    */

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginActivity", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        // TODO - stub code, mostly
        if (account != null) {
            TextView tv = findViewById(R.id.indicator);
            tv.setText(R.string.signed_in_string);

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.continue_button).setVisibility(View.VISIBLE);
        } else {
            TextView tv = findViewById(R.id.indicator);
            tv.setText(R.string.signed_out_string);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            findViewById(R.id.continue_button).setVisibility(View.GONE);
        }
    }

    private void requestPermissions() {
        //ask for bluetooth permission
        if ((Build.VERSION.SDK_INT >= 23) && (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }

        if (!mBtAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Error")
                        .setMessage("App requires Bluetooth to function! This can be changed in Settings.")
                        .setNegativeButton("OK", null)
                        .show();
                return;
            }
            mBtAdapter.enable();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if ((Build.VERSION.SDK_INT >= 23) && (ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Error")
                            .setMessage("App requires Bluetooth to function! This can be changed in Settings.")
                            .setNegativeButton("OK", null)
                            .show();
                    Toast.makeText(LoginActivity.this, "Permission denied for bluetooth", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Permission granted for bluetooth", Toast.LENGTH_SHORT).show();
                return;
            }

            default: { //case 0, error
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Error")
                        .setMessage("Bluetooth functionality could not be implemented.")
                        .setNegativeButton("OK", null)
                        .show();
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}