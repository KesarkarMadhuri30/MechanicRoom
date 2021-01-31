package com.developer.mechanicrooms;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.BannerModel;
import com.developer.mechanicrooms.Model.GarageListModel;
import com.developer.mechanicrooms.Model.GarageModel;
import com.developer.mechanicrooms.Model.ServicingLocationResponse;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;
import com.developer.mechanicrooms.adapter.Adver_banner_PagerAdapter;
import com.developer.mechanicrooms.adapter.MyListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Mainlastscreen extends MyActivity implements View.OnClickListener {
    TextView textviewtop,txt_user;
    ImageView imageviewtop,locationtop;
    RecyclerView home_recyclerview;
    Button hiss;
    TextView user_loc_pincode,txt_error;
    ViewPager viewPager;
    ArrayList<BannerModel> bannerData = new ArrayList<BannerModel>();
    int currentPage, NUM_PAGES;
    Adver_banner_PagerAdapter adverbannerPagerAdapter;
     AlertDialog pincode_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlastscreen);

//        imageviewtop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent inten=new Intent(getApplicationContext(), History.class);
//                startActivity(inten);
//            }
//        });


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;


            txt_error = findViewById(R.id.txt_error);

//            hiss = findViewById(R.id.hiss);
//        hiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), History.class);
//                startActivity(intent);
//            }
//        });
            home_recyclerview = (RecyclerView) findViewById(R.id.recyclermain);
           // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
              GridLayoutManager layoutManager = new GridLayoutManager(this,2);
            home_recyclerview.setLayoutManager(layoutManager);
            home_recyclerview.setNestedScrollingEnabled(false);

            viewPager = findViewById(R.id.viewPager);
            GetImageforbaners();

        imageviewtop = findViewById(R.id.imageviewtop);
        imageviewtop.setOnClickListener(this);

            locationtop = findViewById(R.id.locationtop);
            locationtop.setOnClickListener(this);

        textviewtop = findViewById(R.id.textviewtop);
            txt_user = findViewById(R.id.txt_user);
            String username = pref.getString("name","");
            if (username !=null)
            {
                txt_user.setText("Hello "+username);
            }

            user_loc_pincode = findViewById(R.id.user_loc_pincode);
            getNearGarage();

        } else {
            connected = false;
            Toast.makeText(Mainlastscreen.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void GetImageforbaners() {

        bannerData.add(new BannerModel("1", R.drawable.banner1));
        bannerData.add(new BannerModel("2", R.drawable.banner2));
        bannerData.add(new BannerModel("3", R.drawable.banner4));
        if (!bannerData.isEmpty()) {
            SetImages(bannerData);
        }
    }

    private void SetImages(ArrayList<BannerModel> bannerData) {
        NUM_PAGES = this.bannerData.size();
        adverbannerPagerAdapter = new Adver_banner_PagerAdapter(this, bannerData);
        viewPager.setAdapter(adverbannerPagerAdapter);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.locationtop:
//                Intent intentnew=new Intent(getApplicationContext(), Addlocation.class);
//                startActivity(intentnew);
                if (!user_loc_pincode.getText().toString().isEmpty()) {
                    PincodeSearchdialog(user_loc_pincode.getText().toString());
                }
                break;
            case R.id.imageviewtop:
                Intent i=new Intent(getApplicationContext(), Userinfo.class);
                startActivity(i);

                break;
        }
    }

    private void PincodeSearchdialog(String pincode) {
             LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pincode_search_dialog, null);
        final EditText edt_areacode = alertLayout.findViewById(R.id.edt_areacode);
        final Button btn_search = (Button) alertLayout.findViewById(R.id.btn_search);
        final ImageView pincode_close_btn = (ImageView) alertLayout.findViewById(R.id.pincode_close_btn);


        edt_areacode.setText(""+pincode);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        alert.setView(alertLayout);
        alert.setCancelable(false);
         pincode_dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        pincode_dialog.setCancelable(false);

        pincode_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pincode_dialog.dismiss();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_areacode.getText().toString().isEmpty()) {
                    edt_areacode.setError("Enter zipcode");
                    edt_areacode.requestFocus();
                }else if (edt_areacode.getText().toString().length() != 6) {
                    edt_areacode.setError("Enter 6 digit zipcode");
                    edt_areacode.requestFocus();
                }
                else
                {
                    String pincode = edt_areacode.getText().toString();
                    updateAddressDataUrl(pincode);

                }
            }
        });

        pincode_dialog.show();
    }


    private void updateAddressDataUrl(String s_pincode) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String customer_id = pref.getString("customer_id", "");
        String address = pref.getString("address", "");
        String area = pref.getString("area", "");
        String pincode = s_pincode;
        String city_id = pref.getString("city_id", "");
        String brand_id = pref.getString("brand_id", "");
        String bikeModelId = pref.getString("bikeModelId", "");


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
                        pincode_dialog.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                        pref.edit().putString("pincode", s_pincode).commit();
                        getNearGarage();

                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();

                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();

                    }

                } else {
                    uploading.dismiss();

                    Toast.makeText(Mainlastscreen.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServicingLocationResponse> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }


    private void getHomeData(ArrayList<GarageListModel> garageList,String BaseUrl) {

        MyListAdapter adapter = new MyListAdapter(Mainlastscreen.this,garageList,BaseUrl);
        home_recyclerview.setAdapter(adapter);

    }

    private void getNearGarage() {
        String pincode = pref.getString("pincode","");
        if (pincode !=null)
        {
            user_loc_pincode.setText(""+pincode);
        }

        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String city_id = pref.getString("city_id","");
       // String pincode = pref.getString("pincode","");
        String area = pref.getString("area","");

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageModel> garage_call = service.getGaragesUrl(city_id,pincode,area);

        garage_call.enqueue(new Callback<GarageModel>() {
            @Override
            public void onResponse(Call<GarageModel> call, Response<GarageModel> response) {
                Log.d("###", "onsuccess");
                if (response.body()!=null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        txt_error.setVisibility(View.GONE);
                        home_recyclerview.setVisibility(View.VISIBLE);
                        ArrayList<GarageListModel> garageList = response.body().getGarageList();
                        String BaseUrl = response.body().getBase_url();
                        getHomeData(garageList,BaseUrl);

                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        txt_error.setVisibility(View.VISIBLE);
                        home_recyclerview.setVisibility(View.GONE);
                        String toastmsg = response.body().getMessage();
                        txt_error.setText(""+toastmsg);
//                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                        home_recyclerview.setVisibility(View.GONE);
                        txt_error.setVisibility(View.GONE);
                    }
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


}
