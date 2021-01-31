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

import com.developer.mechanicrooms.Model.ServerResponse;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;

public class UpdateMobileOtpActivity extends MyActivity implements View.OnClickListener {
    Button submit_otp;
    TextView resend_opt_link;
    AppCompatEditText opt_edt;
    String phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobile_otp);
        phone_no = getIntent().getStringExtra("phone");

        opt_edt = findViewById(R.id.opt_edt);
        resend_opt_link = findViewById(R.id.resend_opt_link);
        resend_opt_link.setOnClickListener(this);

        submit_otp = findViewById(R.id.submit_otp);
        submit_otp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submit_otp:

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
                        submit_otp.setEnabled(false);

                    }
                    else {
                        connected = false;
                        Toast.makeText(UpdateMobileOtpActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(UpdateMobileOtpActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(UpdateMobileOtpActivity.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();

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

        String customerId = pref.getString("customer_id","");
       // String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ServerResponse> otp_call = service.postcheckOtp(phone_no,otp,customerId);

        otp_call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true)
                {
                    uploading.dismiss();
                    submit_otp.setEnabled(true);
                    Intent ii = new Intent(UpdateMobileOtpActivity.this, Userinfo.class);
                    startActivity(ii);
                    finish();

                    pref.edit().putString("phone",phone_no).commit();

                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    submit_otp.setEnabled(true);
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
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void showToastmsg(String error_msg) {
        Toast.makeText(this, ""+error_msg, Toast.LENGTH_SHORT).show();

    }
}