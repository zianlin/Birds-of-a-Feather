package com.example.birdsofafeather;

import android.app.Activity;
import android.app.AlertDialog;

import com.example.birdsofafeather.db.Course;
import com.example.birdsofafeather.db.IAccount;

import java.util.List;

public class Utilities {
    public static void showAlert(Activity activity, String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
