package com.developer.mechanicroomvendor.StartPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.developer.mechanicroomvendor.R;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {
    Button forgotpassbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        forgotpassbtn = findViewById(R.id.forgot_btn);
        forgotpassbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.forgot_btn:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}