
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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.developer.mechanicroomvendor.HomeActivity;
import com.developer.mechanicroomvendor.Model.AccountInfoModel;
import com.developer.mechanicroomvendor.Model.GarageModel;
import com.developer.mechanicroomvendor.Model.UserModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;

public class RegProfile extends MyActivity implements View.OnClickListener {
    Button nexxxxxt;
    AppCompatEditText name_edt,email_edt;
    String vendor_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_profile);
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
            Toast.makeText(RegProfile.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRegisterDataUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        final String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageModel> reg_call = service.getAccountInfoUrl(phone_no);

        reg_call.enqueue(new Callback<GarageModel>() {
            @Override
            public void onResponse(Call<GarageModel> call, Response<GarageModel> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        ArrayList<AccountInfoModel> accountInfo = response.body().getAccountInfoModel();
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
                    Toast.makeText(RegProfile.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GarageModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void setData(ArrayList<AccountInfoModel> accountInfo) {
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



        if(accountInfo.get(0).getVendor_id()!=null)
        {
            if (!accountInfo.get(0).getVendor_id().isEmpty())
            {
                vendor_id = accountInfo.get(0).getVendor_id();
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

    private void userDataUrl(final String name, final String email) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        final String phone_no = pref.getString("phone","");
        String user_type ="vendor";
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<UserModel> user_prof_call = service.postUserProfile(phone_no,name,email,user_type);

        user_prof_call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        nexxxxxt.setEnabled(true);
                        Intent ii = new Intent(RegProfile.this, HomeActivity.class);
                        startActivity(ii);
                        finish();

                        pref.edit().putString("vendor_id", vendor_id).commit();

                        // pref.edit().putString("id",response.body().getCustomerInfo().getId()).commit();
                        pref.edit().putBoolean("logged_in", true).commit();
                        pref.edit().putString("name", name).commit();
                        pref.edit().putString("email", email).commit();
                        pref.edit().putString("phone", phone_no).commit();


                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        nexxxxxt.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                        nexxxxxt.setEnabled(true);
                    }
                }
                else
                {
                    uploading.dismiss();
                    nexxxxxt.setEnabled(true);
                    Toast.makeText(RegProfile.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }
}