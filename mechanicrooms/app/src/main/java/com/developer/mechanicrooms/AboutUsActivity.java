package com.developer.mechanicrooms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }
//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(AboutUsActivity.this, Userinfo.class);
////        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
////                Intent.FLAG_ACTIVITY_CLEAR_TASK |
////                Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        finish();
//        super.onBackPressed();
//    }
}