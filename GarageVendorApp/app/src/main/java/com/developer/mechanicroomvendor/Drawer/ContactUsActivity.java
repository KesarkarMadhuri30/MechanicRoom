package com.developer.mechanicroomvendor.Drawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.developer.mechanicroomvendor.HomeActivity;
import com.developer.mechanicroomvendor.R;

public class ContactUsActivity extends AppCompatActivity {
    TextView phone_no,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        phone_no = findViewById(R.id.phone_no);
        email = findViewById(R.id.email);

        phone_no.setTextColor(ContextCompat.getColor(this,R.color.black));
        phone_no.setText(Html.fromHtml("<a href=\"tel:+917798983838\">+91 7798983838</a>"));
        phone_no.setMovementMethod(LinkMovementMethod.getInstance());


        email.setTextColor(ContextCompat.getColor(this,R.color.black));
        email.setText(Html.fromHtml("<a href=\"mailto:contactus@mechanicrooms.com\">contactus@mechanicrooms.com</a>"));
        email.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ContactUsActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}