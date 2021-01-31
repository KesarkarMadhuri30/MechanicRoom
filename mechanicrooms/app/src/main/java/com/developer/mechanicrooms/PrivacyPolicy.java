package com.developer.mechanicrooms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(PrivacyPolicy.this, Userinfo.class);
////        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
////                Intent.FLAG_ACTIVITY_CLEAR_TASK |
////                Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        finish();
//        super.onBackPressed();
//    }
}