package com.developer.mechanicrooms;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.BikeModel;
import com.developer.mechanicrooms.Model.BikeModelInfo;
import com.developer.mechanicrooms.Model.CityInfoModel;
import com.developer.mechanicrooms.Model.CityModel;
import com.developer.mechanicrooms.Model.RegProfileModel;
import com.developer.mechanicrooms.Model.ServicingLocationResponse;
import com.developer.mechanicrooms.Model.UserInfoModel;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Addlocation extends MyActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Button mainpage, mainidbutton;
    ImageView back, search;
    Spinner city_spinner, brand_spinner, bikemodel_spinner;
    String str_city_spinner, str_brand_spinner, str_bikemodel_spinner, str_city_spinner_ID, str_brand_spinner_ID, str_bikemodel_spinner_ID;
    TextView toptext;
    AppCompatEditText edt_area, edt_areacode, edt_address;
    ArrayAdapter cityadapter;
    ArrayAdapter brand_adapter;
    ArrayAdapter modeladapter;
    ArrayList<UserInfoModel> accountInfo = new ArrayList<UserInfoModel>();

    LocationManager locationManager;
    private static final int MY_REQUEST_CODE = 0;
    private static final int REQUEST_CODE = 0;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    Geocoder geocoder;
    List<Address> addresses;

    String address, postalCode, area,city;
    Button current_loc_btn;
    ArrayList<String> cityNameList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlocation_latest);


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            mGoogleApiClient.connect();

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in mi

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
//            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                // Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
//                //jump_to_nextActivity();
//                //getAllPermissions();
//            } else {
//                showGPSDisabledAlertToUser();
//            }
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                checkLocationPermission();
//            }

            city_spinner = findViewById(R.id.city_spinner);
            brand_spinner = findViewById(R.id.brand_spinner);
            bikemodel_spinner = findViewById(R.id.bikemodel_spinner);

            getCityUrl();
            getBrandUrl();
            getRegisterDataUrl();

            edt_area = findViewById(R.id.edt_area);
            edt_areacode = findViewById(R.id.edt_areacode);
            edt_address = findViewById(R.id.edt_address);

            edt_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edt_area.setCursorVisible(true);
                }
            });

            edt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edt_address.setCursorVisible(true);
                }
            });

            edt_areacode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edt_areacode.setCursorVisible(true);
                }
            });

            mainidbutton = findViewById(R.id.mainidbutton);
            mainidbutton.setOnClickListener(this);

            current_loc_btn = findViewById(R.id.current_loc_btn);
            current_loc_btn.setOnClickListener(this);

        } else {
            connected = false;
            Toast.makeText(Addlocation.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRegisterDataUrl() {
        accountInfo.clear();
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        final String phone_no = pref.getString("phone", "");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegProfileModel> reg_call = service.getAccountInfoUrl(phone_no);

        reg_call.enqueue(new Callback<RegProfileModel>() {
            @Override
            public void onResponse(Call<RegProfileModel> call, Response<RegProfileModel> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        accountInfo = response.body().getCustomerInfo();
                        setData(accountInfo);

                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                    }
                } else {
                    uploading.dismiss();
                    Toast.makeText(Addlocation.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegProfileModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void setData(ArrayList<UserInfoModel> accountInfo) {
        if (accountInfo.get(0).getArea() != null) {
            if (!accountInfo.get(0).getArea().isEmpty()) {
                edt_area.setText(accountInfo.get(0).getArea());
            }
        }
        ///Toast.makeText(RegProfile.this, "" + accountInfo.get(0).getName(), Toast.LENGTH_SHORT).show();
        if (accountInfo.get(0).getPincode() != null) {
            if (!accountInfo.get(0).getPincode().isEmpty()) {
                if (!accountInfo.get(0).getPincode().equals("0")) {
                    edt_areacode.setText(accountInfo.get(0).getPincode());
                }
            }
        }
        //  Toast.makeText(RegProfile.this, "" +  accountInfo.get(0).getEmail(), Toast.LENGTH_SHORT).show();

//        if(accountInfo.get(0).getBrand_id()!=null)
//        {
//            if (!accountInfo.get(0).getBrand_id().isEmpty())
//            {
//                if (!accountInfo.get(0).getBrand_id().equals("0")) {
//                    String brandID = accountInfo.get(0).getBrand_id();
//                    String brandname = accountInfo.get(0).getBrand_name();
//                    Log.d("7",brandID+"  "+brandname);
//                    brand_spinner.setSelection(brand_adapter.setP(brandname));
//                    getBikeModelUrl(brandID);
//                }
//                else
//                {
//                    getBrandUrl();
//                }
//            }
//        }


        if (accountInfo.get(0).getAddress() != null) {
            if (!accountInfo.get(0).getAddress().isEmpty()) {
                if (!accountInfo.get(0).getPincode().equals("0")) {
                    edt_address.setText(accountInfo.get(0).getAddress());
                } else {
                    edt_address.setText("");
                }
            }
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
                if (response.body().isStatus() == true) {
                    uploading.dismiss();

                    ArrayList<CityInfoModel> cityList = response.body().getCityinfo();
                    setCityData(cityList);

                } else if (response.body().isStatus() == false) {
                    uploading.dismiss();

                } else {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {

            }
        });

    }

    private void setCityData(ArrayList<CityInfoModel> cityList) {
        cityNameList.clear();
        ArrayList<String> cityIDList = new ArrayList<>();

        for (int i = 0; i < cityList.size(); i++) {
            String cityname = cityList.get(i).getCity_name();
            cityNameList.add(cityname);
            String id = cityList.get(i).getId();
            cityIDList.add(id);
        }
//        String CityID = null;
//        String cityName = null;
//        if(accountInfo.get(0).getCity()!=null)
//        {
//            if (!accountInfo.get(0).getCity().isEmpty())
//            {
//                if (!accountInfo.get(0).getCity().equals("0")) {
//                    CityID = accountInfo.get(0).getCity();
//                   // cityName = accountInfo.get(0).getCity();
//                  //  Log.d("7",brandID+"  "+brandname);
//                }
//            }
//        }

        cityadapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, cityNameList);
        city_spinner.setAdapter(cityadapter);
//        if (cityName != null) {
//            int spinnerPosition = cityadapter.getPosition(cityName);
//            city_spinner.setSelection(spinnerPosition);
//        }

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i != -1) {
//
//                     //   Toast.makeText(getApplicationContext(), ""+ day_spinner.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
//                }
                str_city_spinner = city_spinner.getItemAtPosition(i).toString();
                str_city_spinner_ID = cityIDList.get(i);

                // Toast.makeText(Addlocation.this, ""+str_city_spinner_ID, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getBrandUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<BrandModel> brand_call = service.getBrandUrl();

        brand_call.enqueue(new Callback<BrandModel>() {
            @Override
            public void onResponse(Call<BrandModel> call, Response<BrandModel> response) {
                if (response.body().isStatus() == true) {
                    uploading.dismiss();

                    ArrayList<BrandInfoModel> brandList = response.body().getBrandinfo();

                    setBrandData(brandList);

                } else if (response.body().isStatus() == false) {
                    uploading.dismiss();

                } else {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BrandModel> call, Throwable t) {

            }
        });

    }

    private void setBrandData(ArrayList<BrandInfoModel> brandList) {
        ArrayList<String> brandIDList = new ArrayList<>();
        ArrayList<String> brandNameList = new ArrayList<>();
        for (int i = 0; i < brandList.size(); i++) {
            String brandname = brandList.get(i).getBrand_name();
            brandNameList.add(brandname);

            String id = brandList.get(i).getId();
            brandIDList.add(id);
        }
        String brandname = null;
        String brandID = null;
        if (accountInfo.size() != 0) {
            if (accountInfo.get(0).getBrand_id() != null) {
                if (!accountInfo.get(0).getBrand_id().isEmpty()) {
                    if (!accountInfo.get(0).getBrand_id().equals("0")) {
                        brandID = accountInfo.get(0).getBrand_id();
                        brandname = accountInfo.get(0).getBrand_name();
                        Log.d("7", brandID + "  " + brandname);
                    }
                }
            }
        }

        brand_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, brandNameList);
        brand_spinner.setAdapter(brand_adapter);
        if (brandname != null) {
            int spinnerPosition = brand_adapter.getPosition(brandname);
            brand_spinner.setSelection(spinnerPosition);
            getBikeModelUrl(brandID);
        }
        brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_brand_spinner = brand_spinner.getItemAtPosition(i).toString();
                // Toast.makeText(Addlocation.this, ""+str_brand_spinner, Toast.LENGTH_SHORT).show();
                str_brand_spinner_ID = brandIDList.get(i);
                // Toast.makeText(Addlocation.this, ""+str_brand_spinner_ID, Toast.LENGTH_SHORT).show();
                getBikeModelUrl(str_brand_spinner_ID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getBikeModelUrl(String model_ID) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<BikeModel> brandmodel_call = service.getBrandModelUrl(model_ID);

        brandmodel_call.enqueue(new Callback<BikeModel>() {
            @Override
            public void onResponse(Call<BikeModel> call, Response<BikeModel> response) {
                if (response.body().isStatus() == true) {
                    uploading.dismiss();

                    ArrayList<BikeModelInfo> bikemodelList = response.body().getBikemodelinfo();
                    setBikeModelData(bikemodelList);

                } else if (response.body().isStatus() == false) {
                    uploading.dismiss();

                } else {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BikeModel> call, Throwable t) {

            }
        });

    }

    private void setBikeModelData(ArrayList<BikeModelInfo> bikemodelList) {
        ArrayList<String> bikemodelIDList = new ArrayList<>();
        ArrayList<String> bikemodelNameList = new ArrayList<>();
        for (int i = 0; i < bikemodelList.size(); i++) {
            String brandname = bikemodelList.get(i).getModel_name();
            bikemodelNameList.add(brandname);

            String id = bikemodelList.get(i).getId();
            bikemodelIDList.add(id);
        }

        String model_name = null;
        if (accountInfo.size() != 0) {
            if (accountInfo.get(0).getModel_id() != null) {
                if (!accountInfo.get(0).getModel_id().isEmpty()) {
                    if (!accountInfo.get(0).getBrand_id().equals("0")) {
                        model_name = accountInfo.get(0).getModel_name();
                        Log.d("7", "  " + model_name);
                    }
                }
            }
        }

        modeladapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, bikemodelNameList);
        bikemodel_spinner.setAdapter(modeladapter);
        if (model_name != null) {
            int spinnerPosition = modeladapter.getPosition(model_name);
            bikemodel_spinner.setSelection(spinnerPosition);
        }

        bikemodel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_bikemodel_spinner = bikemodel_spinner.getItemAtPosition(i).toString();
                str_bikemodel_spinner_ID = bikemodelIDList.get(i);
                // Toast.makeText(Addlocation.this, ""+str_bikemodel_spinner_ID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current_loc_btn:
                //  getLocation();
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                    //jump_to_nextActivity();
                    //getAllPermissions();
                    edt_address.setCursorVisible(false);
                    edt_areacode.setCursorVisible(false);
                    edt_area.setCursorVisible(false);
                    if (address != null) {
                        if (!address.isEmpty()) {

                            edt_address.setText(address);
                            edt_address.clearFocus();
                        } else {
                            edt_address.setText("");
                        }
                    }

                    if (postalCode != null) {
                        if (!postalCode.isEmpty()) {
                            edt_areacode.setText(postalCode);
                            edt_areacode.clearFocus();
                        } else {
                            edt_areacode.setText("");
                        }
                    }

                    if (area != null) {
                        if (!area.isEmpty()) {
                            edt_area.setText(area);
                            edt_area.clearFocus();
                        } else {
                            edt_area.setText("");
                        }
                    }

//                    if (city != null) {
//                        if (!city.isEmpty()) {
//                            for(int i=0;i<=cityNameList.size();i++) {
//                                if (!city.equals(cityNameList.get(i))) {
//                                    Toast.makeText(this, "Service is not available for this City", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        } else {
//
//                        }
//                    }
                } else {
                    showGPSDisabledAlertToUser();
                }




                break;
            case R.id.mainidbutton:
                /**/

                if (edt_area.getText().toString().isEmpty()) {
                    edt_area.setError("Enter area");
                    edt_area.requestFocus();
                } else if (edt_areacode.getText().toString().isEmpty()) {
                    edt_areacode.setError("Enter zipcode");
                    edt_areacode.requestFocus();
                }else if (edt_areacode.getText().toString().length() != 6) {
                    edt_areacode.setError("Enter 6 digit zipcode");
                    edt_areacode.requestFocus();
                } else if (edt_address.getText().toString().isEmpty()) {
                    edt_address.setError("Enter Address");
                    edt_address.requestFocus();
                } else if (str_city_spinner.toString().isEmpty()) {
                    Toast.makeText(this, "select city", Toast.LENGTH_SHORT).show();

                } else if (str_brand_spinner.toString().isEmpty()) {
                    Toast.makeText(this, "select Brand", Toast.LENGTH_SHORT).show();

                } else if (str_bikemodel_spinner.toString().isEmpty()) {
                    Toast.makeText(this, "select Bike Model", Toast.LENGTH_SHORT).show();

                } else {
                    String address = edt_address.getText().toString();
                    String area = edt_area.getText().toString();
                    String pincode = edt_areacode.getText().toString();
                    String city_ID = str_city_spinner_ID;
                    String brand_ID = str_brand_spinner_ID;
                    String BikeModelId = str_bikemodel_spinner_ID;


                    updateAddressDataUrl(address, area, pincode, city_ID, brand_ID, BikeModelId);
                    mainidbutton.setEnabled(false);

                }
                break;
        }
    }

    private void updateAddressDataUrl(String address, String area, String pincode, String city_id, String brand_id, String bikeModelId) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String customer_id = pref.getString("customer_id", "");

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ServicingLocationResponse> location_call = service.putServicingLocation(customer_id, address, area, pincode,
                city_id, brand_id, bikeModelId);

        location_call.enqueue(new Callback<ServicingLocationResponse>() {
            @Override
            public void onResponse(Call<ServicingLocationResponse> call, Response<ServicingLocationResponse> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        mainidbutton.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                        pref.edit().putString("address", address).commit();
                        pref.edit().putString("area", area).commit();
                        pref.edit().putString("pincode", pincode).commit();
                        pref.edit().putString("city_id", city_id).commit();
                        pref.edit().putString("brand_id", brand_id).commit();
                        pref.edit().putString("bikeModelId", bikeModelId).commit();

                        pref.edit().putString("vehicle_type", str_bikemodel_spinner).commit();
                        Intent intent = new Intent(getApplicationContext(), Mainlastscreen.class);
                        startActivityForResult(intent,MY_REQUEST_CODE);

                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        mainidbutton.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                        mainidbutton.setEnabled(true);
                    }

                } else {
                    uploading.dismiss();
                    mainidbutton.setEnabled(true);
                    Toast.makeText(Addlocation.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServicingLocationResponse> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, "" + toastmsg, Toast.LENGTH_SHORT).show();
    }

//    void getLocation() {
//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//        } else {
//            showGPSDisabledAlertToUser();
//        }
//
//    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device, You have to enable it to start the App !!")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent, REQUEST_CODE);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                myUpdateOperation();
            } else {
                myUpdateOperation();
            }
        }

        if (requestCode== MY_REQUEST_CODE)
        {
            if (resultCode==RESULT_OK)
            {
                myUpdateOperation();
                // getUserFineInfo();
            }
        }
    }

    private void myUpdateOperation() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        mGoogleApiClient.connect();
     //   Toast.makeText(this, "hiiii", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "\t" + address+"\t" +area+"\t" +postalCode, Toast.LENGTH_SHORT).show();

    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown

                                ActivityCompat.requestPermissions(Addlocation.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                //myUpdateOperation();
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||  //&&
                ContextCompat.checkSelfPermission( this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown


                                ActivityCompat.requestPermissions(Addlocation.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            mGoogleApiClient.connect();
                        }
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        area = addresses.get(0).getSubLocality();
        postalCode = addresses.get(0).getPostalCode();
        city = addresses.get(0).getLocality();
       // Toast.makeText(this, "\t" + address+"\t" +area+"\t" +postalCode, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            //Toast.makeText(this, ""+currentLatitude + currentLongitude, Toast.LENGTH_SHORT).show();

            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            }

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            area = addresses.get(0).getSubLocality();
            postalCode = addresses.get(0).getPostalCode();

            String address1 = addresses.get(0).getAddressLine(1);
              city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String knownName = addresses.get(0).getFeatureName();
            String area1 = addresses.get(0).getAdminArea();
            String subarea = addresses.get(0).getSubAdminArea();
            String locality = addresses.get(0).getLocality();


          //  Toast.makeText(this, "\t" + address+"\t" +area+"\t" +postalCode, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
        // updateUI(false);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            locationManager.requestLocationUpdates(provider, 400, 1, (android.location.LocationListener) this);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            locationManager.removeUpdates((android.location.LocationListener) this);
//        }
//    }
}






