package com.developer.mechanicroomvendor.StartPages;

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
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.RegModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

public class RegisterActivity extends MyActivity implements View.OnClickListener {
    Button continuee;
    AppCompatEditText mobile_edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_first_page);


        mobile_edt = findViewById(R.id.mobile_edt);
        continuee = findViewById(R.id.continuee);
            continuee.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.continuee:

                if (mobile_edt.getText().toString().isEmpty())
                {
                    mobile_edt.setError("Enter mobile number");
                    mobile_edt.requestFocus();
                }
                else if (mobile_edt.getText().toString().length() !=10 )
                {
                    mobile_edt.setError("Enter 10 digit mobile number");
                    mobile_edt.requestFocus();
                }
                else {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        connected = true;

                        String mobile = mobile_edt.getText().toString();

                        registerUserUrl(mobile);
                        continuee.setEnabled(false);
                    }
                    else {
                        connected = false;
                        Toast.makeText(RegisterActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
    private void registerUserUrl(final String mobile) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String user_type ="vendor";
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegModel> reg_call = service.postRegUser(mobile,user_type);

        reg_call.enqueue(new Callback<RegModel>() {
            @Override
            public void onResponse(Call<RegModel> call, Response<RegModel> response) {
                Log.d("###", "onsuccess");
                if(response.body() !=null)
                {
                if (response.body().isStatus()== true)
                {
                    uploading.dismiss();
                    continuee.setEnabled(true);
                    Intent ii = new Intent(RegisterActivity.this, Otp.class);
                    startActivity(ii);
                    finish();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);

                    pref.edit().putString("phone",mobile).commit();

                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    continuee.setEnabled(true);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else {
                    uploading.dismiss();
                    continuee.setEnabled(true);
                }
            }
                else
                {
                    uploading.dismiss();
                    continuee.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();
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