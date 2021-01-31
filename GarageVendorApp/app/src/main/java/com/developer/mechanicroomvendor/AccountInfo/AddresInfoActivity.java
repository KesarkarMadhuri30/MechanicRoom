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
import com.developer.mechanicroomvendor.Model.CityInfoModel;
import com.developer.mechanicroomvendor.Model.CityModel;
import com.developer.mechanicroomvendor.Model.GarageModel;
import com.developer.mechanicroomvendor.Model.GarageUpdateModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;

public class AddresInfoActivity extends MyActivity implements View.OnClickListener {
    AppCompatEditText edt_address,edt_area,edt_zipcode;
    Button loc_submit_btn;
    Spinner city_spinner;
    String str_city_spinner,str_city_spinner_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addres_info);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;

         edt_address = findViewById(R.id.edt_address);
           edt_area = findViewById(R.id.edt_area);
        edt_zipcode = findViewById(R.id.edt_zipcode);

            edt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    edt_address.setCursorVisible(true);

                }
            });

            edt_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edt_area.setCursorVisible(true);

                }
            });

            edt_zipcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    edt_zipcode.setCursorVisible(true);
                }
            });

            city_spinner = findViewById(R.id.city_spinner);
        // edt_state = addressview.findViewById(R.id.edt_state);
            getCityUrl();
            getAddressDataUrl();


        loc_submit_btn = findViewById(R.id.loc_submit_btn);
        loc_submit_btn.setOnClickListener(this);
        }
        else {
            connected = false;
            Toast.makeText(AddresInfoActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCityUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<CityModel> city_call = service.getCityUrl();

        city_call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.body().isStatus()== true) {
                    uploading.dismiss();

                    ArrayList<CityInfoModel> cityList = response.body().getCityinfo();
                    setCityData(cityList);

                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();

                }
                else
                {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {

            }
        });

    }

    private void setCityData(ArrayList<CityInfoModel> cityList) {
        final ArrayList<String> cityIDList = new ArrayList<>();
        ArrayList<String> cityNameList = new ArrayList<>();
        for (int i=0;i<cityList.size();i++)
        {
            String cityname = cityList.get(i).getCity_name();
            cityNameList.add(cityname);

            String id = cityList.get(i).getId();
            cityIDList.add(id);

        }

        ArrayAdapter minadapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, cityNameList);
        city_spinner.setAdapter(minadapter);

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i != -1) {
//
//                     //   Toast.makeText(getApplicationContext(), ""+ day_spinner.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
//                }
                str_city_spinner = city_spinner.getItemAtPosition(i).toString();
                str_city_spinner_ID = cityIDList.get(i);
                //Toast.makeText(Addlocation.this, ""+str_city_spinner, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAddressDataUrl() {
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

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    private void setData(ArrayList<AccountInfoModel> accountInfo) {
        if (accountInfo.get(0).getAddress() != null) {
            if (!accountInfo.get(0).getAddress().isEmpty()) {
                edt_address.setText(accountInfo.get(0).getAddress());
            }
        }
        if (accountInfo.get(0).getArea() != null) {
            if (!accountInfo.get(0).getArea().isEmpty()) {
                    edt_area.setText(accountInfo.get(0).getArea());
            }

        }
        if (accountInfo.get(0).getPincode() != null) {
            if (!accountInfo.get(0).getPincode().isEmpty()) {
                if (!accountInfo.get(0).getPincode().equals("0")) {
                    edt_zipcode.setText(accountInfo.get(0).getPincode());
                }
            }
        }
//        if (accountInfo.get(0).getCity() != null) {
//            if (!accountInfo.get(0).getCity().isEmpty()) {
//                if (!accountInfo.get(0).getCity().equals("0")) {
//                    edt_city.setText(accountInfo.get(0).getCity());
//                }
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.loc_submit_btn:
                if (edt_address.getText().toString().isEmpty())
                {
                    edt_address.setError("Enter Address");
                    edt_address.requestFocus();
                }
                else if (edt_area.getText().toString().isEmpty())
                {
                    edt_area.setError("Enter area");
                    edt_area.requestFocus();
                }
                else if (edt_zipcode.getText().toString().length() !=6 )
                {
                    edt_zipcode.setError("Enter 6 digit zipcode");
                    edt_zipcode.requestFocus();
                }
                else if (str_city_spinner.toString().isEmpty())
                {
                    Toast.makeText(this, "select city", Toast.LENGTH_SHORT).show();

                }
                else {
                    String address = edt_address.getText().toString();
                    String area = edt_area.getText().toString();
                    String pincode = edt_zipcode.getText().toString();
                    String city = str_city_spinner_ID;

                    updateAddressDataUrl(address,area,pincode,city);
                    loc_submit_btn.setEnabled(false);

                }
                break;
        }
    }

    private void updateAddressDataUrl(String address, String area, String pincode, String city) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String user_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageUpdateModel> address_call = service.postGarageLocationUpdate(user_id,address,area,pincode,city);

        address_call.enqueue(new Callback<GarageUpdateModel>() {
            @Override
            public void onResponse(Call<GarageUpdateModel> call, Response<GarageUpdateModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    loc_submit_btn.setEnabled(true);
                    edt_address.setCursorVisible(false);
                    edt_area.setCursorVisible(false);
                    edt_zipcode.setCursorVisible(false);
                    String toastmsg = response.body().getMessage();
                    showUpdatemsg(toastmsg);
                    getAddressDataUrl();
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    loc_submit_btn.setEnabled(true);
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