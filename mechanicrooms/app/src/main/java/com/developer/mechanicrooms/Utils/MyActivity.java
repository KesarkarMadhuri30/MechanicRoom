package com.developer.mechanicrooms.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {
    public static SharedPreferences pref;

    //    // Shared preferences file name
    private static final String PREF_NAME = "garage_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }
}
