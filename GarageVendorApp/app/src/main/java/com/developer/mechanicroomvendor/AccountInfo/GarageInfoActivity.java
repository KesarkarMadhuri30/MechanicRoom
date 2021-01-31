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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class GarageInfoActivity extends MyActivity implements View.OnClickListener {
    Spinner pick_drop_spinner;
    ArrayAdapter pick_drop_adapter;
    String str_pick_drop_spinner;

    String[] pick_drop_option_list = {"Yes", "No"};
     AppCompatEditText garage_name_edt;
     AppCompatEditText garage_contact_edt ;
     AppCompatEditText garage_service_edt;

    Button btn_garage_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_info);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;

            garage_name_edt = findViewById(R.id.garage_name_edt);
            garage_contact_edt = findViewById(R.id.garage_contact_edt);
            garage_service_edt = findViewById(R.id.garage_service_edt);

            garage_name_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    garage_name_edt.setCursorVisible(true);

                }
            });

            garage_contact_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    garage_contact_edt.setCursorVisible(true);

                }
            });

            garage_service_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    garage_service_edt.setCursorVisible(true);
                }
            });
            pick_drop_spinner = findViewById(R.id.pick_drop_spinner);

            getGarageDataUrl();

             btn_garage_update = findViewById(R.id.btn_garage_update);
            btn_garage_update.setOnClickListener(this);
        }
            else {
                connected = false;
                Toast.makeText(GarageInfoActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
            }

    }

    private void getGarageDataUrl() {
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
        if (accountInfo.get(0).getGarage_name() != null) {
            if (!accountInfo.get(0).getGarage_name().isEmpty()) {
                garage_name_edt.setText(accountInfo.get(0).getGarage_name());
            }
        }
        if (accountInfo.get(0).getContact_no() != null) {
            if (!accountInfo.get(0).getContact_no().isEmpty()) {
                if (!accountInfo.get(0).getContact_no().equals("0")) {
                    garage_contact_edt.setText(accountInfo.get(0).getContact_no());
                }
            }

        }
        if (accountInfo.get(0).getServices() != null) {
            if (!accountInfo.get(0).getServices().isEmpty()) {
                garage_service_edt.setText(accountInfo.get(0).getServices());
            }
        }

        String pickdrop = null;

        if (accountInfo.size()!=0) {
            if (accountInfo.get(0).getPick_drop() != null) {
                if (!accountInfo.get(0).getPick_drop().isEmpty()) {
                    if (!accountInfo.get(0).getPick_drop().equals("0")) {
                        pickdrop = accountInfo.get(0).getPick_drop();
                    }
                }
            }
        }

         pick_drop_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, pick_drop_option_list);
        pick_drop_spinner.setAdapter(pick_drop_adapter);
        if (pickdrop != null) {
            int spinnerPosition = pick_drop_adapter.getPosition(pickdrop);
            pick_drop_spinner.setSelection(spinnerPosition);

        }
        pick_drop_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_pick_drop_spinner = pick_drop_spinner.getItemAtPosition(i).toString();
               // Toast.makeText(GarageInfoActivity.this, ""+str_pick_drop_spinner, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_garage_update:
                if (garage_name_edt.getText().toString().isEmpty())
                {
                    garage_name_edt.setError("Enter Garage name");
                    garage_name_edt.requestFocus();
                }
                else if (garage_contact_edt.getText().toString().isEmpty())
                {
                    garage_contact_edt.setError("Enter mobile number");
                    garage_contact_edt.requestFocus();
                }
                else if (garage_contact_edt.getText().toString().length() !=10 )
                {
                    garage_contact_edt.setError("Enter 10 digit mobile number");
                    garage_contact_edt.requestFocus();
                }
                else if (garage_service_edt.getText().toString().isEmpty())
                {
                    garage_service_edt.setError("Enter Garage Services");
                    garage_service_edt.requestFocus();

                }
                else if (str_pick_drop_spinner==null)
                {
                    if (str_pick_drop_spinner.isEmpty())
                    {
                        Toast.makeText(this, "Select Pickup and Drop Option", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    String garage_name = garage_name_edt.getText().toString();
                    String garage_mobile = garage_contact_edt.getText().toString();
                    String garage_service = garage_service_edt.getText().toString();

                    updateGarageDataUrl(garage_name,garage_mobile,garage_service,str_pick_drop_spinner);
                    btn_garage_update.setEnabled(false);
                }
                break;
        }
    }

    private void updateGarageDataUrl(String garage_name, String garage_mobile, String garage_service, String str_pick_drop_spinner) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String user_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageUpdateModel> owner_prof_call = service.postGarageProfileUpdate(user_id,garage_name,garage_mobile,garage_service,str_pick_drop_spinner);

        owner_prof_call.enqueue(new Callback<GarageUpdateModel>() {
            @Override
            public void onResponse(Call<GarageUpdateModel> call, Response<GarageUpdateModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    btn_garage_update.setEnabled(true);
                    garage_contact_edt.setCursorVisible(false);
                    garage_name_edt.setCursorVisible(false);
                    garage_service_edt.setCursorVisible(false);
                    String toastmsg = response.body().getMessage();
                    showUpdatemsg(toastmsg);
                    getGarageDataUrl();
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    btn_garage_update.setEnabled(true);
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