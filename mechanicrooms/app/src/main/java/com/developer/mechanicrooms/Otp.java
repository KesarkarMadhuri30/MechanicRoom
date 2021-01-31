package com.developer.mechanicrooms;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;

public class Otp extends MyActivity implements View.OnClickListener {
    AppCompatEditText opt_edt;
    Button login;
    TextView resend_opt_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


            opt_edt = findViewById(R.id.opt_edt);
            resend_opt_link = findViewById(R.id.resend_opt_link);
            resend_opt_link.setOnClickListener(this);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:

                    if (opt_edt.getText().toString().isEmpty())
                {
                    opt_edt.setError("Enter Otp");
                    opt_edt.requestFocus();
                }
                else if (opt_edt.getText().toString().length() != 4 )
                {
                    opt_edt.setError("Enter 4 digit otp");
                    opt_edt.requestFocus();
                }
                else {
                        boolean connected = false;
                        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            //we are connected to a network
                            connected = true;

                            String otp = opt_edt.getText().toString();

                    otpUserUrl(otp);
                    login.setEnabled(false);

                    }
                else {
                        connected = false;
                        Toast.makeText(Otp.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
                    }
                }


                break;

            case R.id.resend_opt_link:
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;

                    resendOtpUserUrl();
                resend_opt_link.setEnabled(false);
                }
                else {
                    connected = false;
                    Toast.makeText(Otp.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void resendOtpUserUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegModel> resend_otp_call = service.postResendOtpUser(phone_no);

        resend_otp_call.enqueue(new Callback<RegModel>() {
            @Override
            public void onResponse(Call<RegModel> call, Response<RegModel> response) {
                Log.d("###", "onsuccess");
                if (response.body()!=null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        resend_opt_link.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);

                    } else if (response.body().isStatus() == false) {
                        Log.d("###", "false");
                        uploading.dismiss();
                        resend_opt_link.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        Log.d("###", "none");
                        uploading.dismiss();
                    }
                }
                else
                {
                    uploading.dismiss();
                    Toast.makeText(Otp.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RegModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void otpUserUrl(String otp) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegModel> otp_call = service.postOtpUser(phone_no,otp);

        otp_call.enqueue(new Callback<RegModel>() {
            @Override
            public void onResponse(Call<RegModel> call, Response<RegModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true)
                {
                    uploading.dismiss();
                    login.setEnabled(true);
                    Intent ii = new Intent(Otp.this, RegProfileActivity.class);
                    startActivity(ii);
                    finish();

                    //pref.edit().putString("phone",mobile).commit();

                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    login.setEnabled(true);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    Log.d("###","none");
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RegModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void showToastmsg(String error_msg) {
        Toast.makeText(this, ""+error_msg, Toast.LENGTH_SHORT).show();

    }
}
