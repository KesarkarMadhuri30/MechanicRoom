package com.developer.mechanicrooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
    TextView phone_no,email;
    ImageView facebook_img;

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

        facebook_img = findViewById(R.id.facebook_img);
        facebook_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.facebook_img:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/"));
               // intent.setData(Uri.parse("https://www.facebook.com/Mahakal-Cricket-Prediction-Tips-113332187244731/"));
                startActivity(intent);
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(ContactUs.this, Userinfo.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        finish();
//        super.onBackPressed();
//    }
}