package com.developer.mechanicrooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.RegProfileModel;
import com.developer.mechanicrooms.Model.UserInfoModel;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;

import java.util.ArrayList;

import static com.developer.mechanicrooms.Utils.MyActivity.pref;

public class RegProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button nexxxxxt, skip;
    AppCompatEditText name_edt,email_edt;
    TextView textView;
    String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_profile);


        //skip = findViewById(R.id.skip);
        //skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Mainscreen.class);
//                startActivity(intent);
//            }
//        });
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            getRegisterDataUrl();

        name_edt = findViewById(R.id.name_edt);
        email_edt = findViewById(R.id.email_edt);

        nexxxxxt = findViewById(R.id.nexxxxxt);
        nexxxxxt.setOnClickListener(this);

        }
        else {
            connected = false;
            Toast.makeText(RegProfileActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRegisterDataUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        final String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegProfileModel> reg_call = service.getAccountInfoUrl(phone_no);

        reg_call.enqueue(new Callback<RegProfileModel>() {
            @Override
            public void onResponse(Call<RegProfileModel> call, Response<RegProfileModel> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        ArrayList<UserInfoModel> accountInfo = response.body().getCustomerInfo();
                        setData(accountInfo);

                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                    }
                }
                else
                {
                    uploading.dismiss();
                    Toast.makeText(RegProfileActivity.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegProfileModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void setData(ArrayList<UserInfoModel> accountInfo) {
        if (accountInfo.get(0).getName() != null) {
            if (!accountInfo.get(0).getName().isEmpty()) {
                name_edt.setText(accountInfo.get(0).getName());
            }
        }
        ///Toast.makeText(RegProfile.this, "" + accountInfo.get(0).getName(), Toast.LENGTH_SHORT).show();
        if (accountInfo.get(0).getEmail() !=null) {
            if (!accountInfo.get(0).getEmail().isEmpty()) {
                email_edt.setText(accountInfo.get(0).getEmail());
            }
        }
        //  Toast.makeText(RegProfile.this, "" +  accountInfo.get(0).getEmail(), Toast.LENGTH_SHORT).show();



        if(accountInfo.get(0).getCustomer_id()!=null)
        {
            if (!accountInfo.get(0).getCustomer_id().isEmpty())
            {
                customerId = accountInfo.get(0).getCustomer_id();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.nexxxxxt:
                if (name_edt.getText().toString().isEmpty())
                {
                    name_edt.setError("Enter name");
                    name_edt.requestFocus();
                }
                else if (email_edt.getText().toString().isEmpty())
                {
                    email_edt.setError("Enter Email!");
                    email_edt.requestFocus();

                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email_edt.getText().toString()).matches())
                {
                    email_edt.setError("Enter Valid Email!");
                    email_edt.requestFocus();

                }
                else {
                    String name = name_edt.getText().toString();
                    String email = email_edt.getText().toString();

                    userDataUrl(name,email);
                    nexxxxxt.setEnabled(false);
                }
                break;
        }
    }

    private void userDataUrl(String name, String email) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String phone_no = pref.getString("phone","");
        String user_type ="user";
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegProfileModel> user_prof_call = service.postUserProfile(phone_no,name,email,user_type);

        user_prof_call.enqueue(new Callback<RegProfileModel>() {
            @Override
            public void onResponse(Call<RegProfileModel> call, Response<RegProfileModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true)
                {
                    uploading.dismiss();
                    nexxxxxt.setEnabled(true);
                    Intent ii = new Intent(RegProfileActivity.this, Addlocation.class);
                    startActivity(ii);
                    finish();

                    //pref.edit().putString("phone",mobile).commit();
                   // pref.edit().putString("id",response.body().getCustomerInfo().getId()).commit();
                    pref.edit().putString("customer_id", customerId).commit();
                    pref.edit().putBoolean("logged_in", true).commit();
                    pref.edit().putString("name",name).commit();
                    pref.edit().putString("email",email).commit();
                    pref.edit().putString("phone",phone_no).commit();


                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    nexxxxxt.setEnabled(true);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    uploading.dismiss();
                    nexxxxxt.setEnabled(true);
                    Toast.makeText(RegProfileActivity.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RegProfileModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }
}
