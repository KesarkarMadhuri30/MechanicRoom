package com.developer.mechanicroomvendor.StartPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.developer.mechanicroomvendor.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    CheckBox showcheckBox;
    AppCompatEditText edt_pass;
    TextView forgotpasslink;
    Button txt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_pass = findViewById(R.id.edt_password);

        showcheckBox = findViewById(R.id.show_checkbox);
        showcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edt_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Log.i("checker", "true");
                } else {
                    edt_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Log.i("checker", "false");
                }
            }
        });

        forgotpasslink = findViewById(R.id.forgotpasslink);
        forgotpasslink.setOnClickListener(this);
//
//        txt_register = findViewById(R.id.txt_register);
//        txt_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.forgotpasslink:
                Intent intent = new Intent(this, ForgotPassActivity.class);
                startActivity(intent);
                break;

//            case R.id.txt_register:
//                Intent i = new Intent(this, RegisterActivity.class);
//                startActivity(i);
//            break ;

        }
    }
}