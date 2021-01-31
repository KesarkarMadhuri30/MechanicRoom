package com.developer.mechanicroomvendor.AccountInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.AccountInfoModel;
import com.developer.mechanicroomvendor.Model.GarageModel;
import com.developer.mechanicroomvendor.Model.GarageUpdateModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;

public class OwnerInfo extends MyActivity implements View.OnClickListener {
    AppCompatEditText owner_name_edt,owner_mobile_edt,owner_email_edt;
    Button btn_owner_info_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_info);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

        owner_name_edt = findViewById(R.id.owner_name_edt);
        owner_mobile_edt = findViewById(R.id.owner_mobile_edt);
        owner_email_edt = findViewById(R.id.owner_email_edt);

            //owner_email_edt.setRawInputType(InputType.TYPE_CLASS_TEXT);
            owner_name_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    owner_name_edt.setCursorVisible(true);

                }
            });

            owner_email_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    owner_email_edt.setCursorVisible(true);

                }
            });

            owner_mobile_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    owner_mobile_edt.setCursorVisible(true);
                }
            });

        getOwnerDataUrl();

            btn_owner_info_update = findViewById(R.id.btn_owner_info_update);
            btn_owner_info_update.setOnClickListener(this);
        }
        else {
            connected = false;
            Toast.makeText(OwnerInfo.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOwnerDataUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

         String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageModel> reg_call = service.getAccountInfoUrl(phone_no);

        reg_call.enqueue(new Callback<GarageModel>() {
            @Override
            public void onResponse(Call<GarageModel> call, Response<GarageModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    ArrayList<AccountInfoModel> accountInfo = response.body().getAccountInfoModel();
                    setData(accountInfo);

                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    uploading.dismiss();
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
                owner_name_edt.setText(accountInfo.get(0).getName());
            }
        }
        if (accountInfo.get(0).getPhone() != null) {
            if (!accountInfo.get(0).getPhone().isEmpty()) {
                owner_mobile_edt.setText(accountInfo.get(0).getPhone());
            }

        }
        if (accountInfo.get(0).getEmail() != null) {
            if (!accountInfo.get(0).getEmail().isEmpty()) {
                owner_email_edt.setText(accountInfo.get(0).getEmail());
            }
        }
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_owner_info_update:
                if (owner_name_edt.getText().toString().isEmpty())
                {
                    owner_name_edt.setError("Enter Owner name");
                    owner_name_edt.requestFocus();
                }
                else if (owner_mobile_edt.getText().toString().isEmpty())
                {
                    owner_mobile_edt.setError("Enter mobile number");
                    owner_mobile_edt.requestFocus();
                }
                else if (owner_mobile_edt.getText().toString().length() !=10 )
                {
                    owner_mobile_edt.setError("Enter 10 digit mobile number");
                    owner_mobile_edt.requestFocus();
                }
                else if (owner_email_edt.getText().toString().isEmpty())
                {
                    owner_email_edt.setError("Enter Owner Email!");
                    owner_email_edt.requestFocus();

                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(owner_email_edt.getText().toString()).matches())
                {
                    owner_email_edt.setError("Enter Valid Email!");
                    owner_email_edt.requestFocus();

                }
                else {
                    String owner_name = owner_name_edt.getText().toString();
                    String owner_mobile = owner_mobile_edt.getText().toString();
                    String owner_email = owner_email_edt.getText().toString();

                    updateOwnerDataUrl(owner_name,owner_mobile,owner_email);
                    btn_owner_info_update.setEnabled(false);
                }
                break;
        }
    }

    private void updateOwnerDataUrl(String owner_name, String owner_mobile, final String owner_email) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

       String user_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageUpdateModel> owner_prof_call = service.postOwnerProfileUpdate(user_id,owner_mobile,owner_name,owner_email);

        owner_prof_call.enqueue(new Callback<GarageUpdateModel>() {
            @Override
            public void onResponse(Call<GarageUpdateModel> call, Response<GarageUpdateModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    btn_owner_info_update.setEnabled(true);
                    owner_email_edt.setCursorVisible(false);
                    owner_name_edt.setCursorVisible(false);
                    owner_mobile_edt.setCursorVisible(false);
                    String toastmsg = response.body().getMessage();
                    showUpdatemsg(toastmsg);
                    getOwnerDataUrl();
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    btn_owner_info_update.setEnabled(true);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GarageUpdateModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void showUpdatemsg(String toastmsg) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView msg =popupView.findViewById(R.id.s_msg);
        msg.setText(""+toastmsg);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(popupView);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();



        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        dialog.show();
        handler.postDelayed(runnable, 2000);
    }
}