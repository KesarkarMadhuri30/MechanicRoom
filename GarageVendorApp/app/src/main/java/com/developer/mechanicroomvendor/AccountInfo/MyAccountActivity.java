package com.developer.mechanicroomvendor.AccountInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Utils.MyActivity;

public class MyAccountActivity extends MyActivity implements View.OnClickListener {
    RelativeLayout owner_lyt,garage_lyt,address_lyt,op_clo_garage_lyt,gallery_lyt,business_lyt,merchant_lyt;
    String logged_mobileno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        logged_mobileno = pref.getString("phone","");
        //Toast.makeText(this, ""+logged_mobileno, Toast.LENGTH_SHORT).show();

        owner_lyt = findViewById(R.id.owner_lyt);
        owner_lyt.setOnClickListener(this);

        garage_lyt= findViewById(R.id.garage_lyt);
        garage_lyt.setOnClickListener(this);
        address_lyt = findViewById(R.id.address_lyt);
        address_lyt.setOnClickListener(this);

        op_clo_garage_lyt = findViewById(R.id.op_clo_garage_lyt);
        op_clo_garage_lyt.setOnClickListener(this);

        gallery_lyt = findViewById(R.id.gallery_lyt);
        gallery_lyt.setOnClickListener(this);

        business_lyt = findViewById(R.id.business_lyt);
        business_lyt.setOnClickListener(this);

        merchant_lyt = findViewById(R.id.merchant_lyt);
        merchant_lyt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.owner_lyt:
                Intent intent = new Intent(getApplicationContext(), OwnerInfo.class);
                startActivity(intent);
                break;

            case R.id.garage_lyt:
                Intent g_intent = new Intent(getApplicationContext(), GarageInfoActivity.class);
                startActivity(g_intent);
                break;
            case R.id.address_lyt:
                Intent ad_intent = new Intent(getApplicationContext(), AddresInfoActivity.class);
                startActivity(ad_intent);
                break;

            case R.id.op_clo_garage_lyt:
                Intent time_intent = new Intent(getApplicationContext(), GarageOpenCloseTimeActivity.class);
                startActivity(time_intent);
                break;

            case R.id.gallery_lyt:
                Intent gallery_intent = new Intent(getApplicationContext(), GalleryActivity.class);
                startActivity(gallery_intent);
                break;

            case R.id.business_lyt:
                Intent business_intent = new Intent(getApplicationContext(), BusinessActivity.class);
                startActivity(business_intent);
                break;
            case R.id.merchant_lyt:
                Intent merchant_intent = new Intent(getApplicationContext(), MerchantActivity.class);
                startActivity(merchant_intent);
                break;
        }
    }
}