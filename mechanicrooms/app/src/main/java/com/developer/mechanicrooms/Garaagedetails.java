package com.developer.mechanicrooms;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.GalleryModel;
import com.developer.mechanicrooms.Model.GarageDetailsDataModel;
import com.developer.mechanicrooms.Model.GarageDetailsModel;
import com.developer.mechanicrooms.Model.GarageGalleryModel;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;
import com.developer.mechanicrooms.adapter.GalleryViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.developer.mechanicrooms.Uppercase.CovertFirstLetterInUppercase;

public class Garaagedetails extends MyActivity implements View.OnClickListener {

    private GalleryViewAdapter adapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<String> garageServiceList;
       Button burronnext;
    RecyclerView rvgallery,rvservice;
    LinearLayout rate_reviews_lyt;
    TextView error_msg;
    TextView txt_garage_name,txt_pick_drop_option,txt_address,txt_area,txt_pincode,txt_garage_contact,txt_weekoff,garage_time;//txt_garage_service;
    ArrayList<GarageDetailsDataModel> detailsDataList = new ArrayList<GarageDetailsDataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garaagedetails);


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

           // error_msg = findViewById(R.id.error_msg);
        rate_reviews_lyt = findViewById(R.id.rate_reviews_lyt);
        rate_reviews_lyt.setOnClickListener(this);

        burronnext = findViewById(R.id.burronnext);
        burronnext.setOnClickListener(this);

        // set up the RecyclerView
         rvgallery = findViewById(R.id.rvgallery);
            gridLayoutManager = new GridLayoutManager(Garaagedetails.this, 3);
            rvgallery.setLayoutManager(gridLayoutManager);
            rvgallery.setNestedScrollingEnabled(false);


            txt_garage_name = findViewById(R.id.txt_garage_name);
            txt_pick_drop_option = findViewById(R.id.txt_pick_drop_option);
            txt_address = findViewById(R.id.txt_address);
            txt_garage_contact = findViewById(R.id.txt_garage_contact);
            txt_area= findViewById(R.id.txt_area);
            txt_pincode= findViewById(R.id.txt_pincode);
            txt_weekoff = findViewById(R.id.txt_weekoff);
            garage_time = findViewById(R.id.garage_time);
           // txt_garage_service = findViewById(R.id.txt_garage_service);

            rvservice = findViewById(R.id.rvservice);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Garaagedetails.this);
            rvservice.setLayoutManager(linearLayoutManager);
            rvservice.setNestedScrollingEnabled(false);

            getGarageDetails();
            getGalleryImages();


        } else {
            connected = false;
            Toast.makeText(Garaagedetails.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void getGalleryImages() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String vendor_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GalleryModel> reg_call = service.getGalleryInfoUrl(vendor_id);

        reg_call.enqueue(new Callback<GalleryModel>() {
            @Override
            public void onResponse(Call<GalleryModel> call, Response<GalleryModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                   // error_msg.setVisibility(View.GONE);
                    rvgallery.setVisibility(View.VISIBLE);
                    ArrayList<GarageGalleryModel> garageGalleryList = response.body().getGarageGalleryModel();
                    String Base_url = response.body().getBase_url();

                    setGalleryData(garageGalleryList,Base_url);


                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                    //error_msg.setVisibility(View.VISIBLE);
                    rvgallery.setVisibility(View.GONE);
                }
                else
                {
                    uploading.dismiss();
                 //   error_msg.setVisibility(View.VISIBLE);
                    rvgallery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GalleryModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void setGalleryData(ArrayList<GarageGalleryModel> garageGalleryList, String base_url) {
        adapter = new GalleryViewAdapter(getApplicationContext(),garageGalleryList,base_url);
        rvgallery.setAdapter(adapter);
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    private void getGarageDetails() {
        detailsDataList.clear();
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String vendor_phone = pref.getString("vendor_phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageDetailsModel> garageDtl_call = service.getGarageInfoUrl(vendor_phone);

        garageDtl_call.enqueue(new Callback<GarageDetailsModel>() {
            @Override
            public void onResponse(Call<GarageDetailsModel> call, Response<GarageDetailsModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                detailsDataList = response.body().getgarageInfoModel();
                    setData(detailsDataList);

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
            public void onFailure(Call<GarageDetailsModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void setData(ArrayList<GarageDetailsDataModel> detailsDataList) {

        if (detailsDataList.get(0).getGarage_name() != null) {
            if (!detailsDataList.get(0).getGarage_name().isEmpty()) {
                String str_garagename = CovertFirstLetterInUppercase(detailsDataList.get(0).getGarage_name());
                txt_garage_name.setText(str_garagename);
            }
        }

        if (detailsDataList.get(0).getPick_drop() != null) {
            if (!detailsDataList.get(0).getPick_drop().isEmpty()) {
                String str_pick_drop_option = CovertFirstLetterInUppercase(detailsDataList.get(0).getPick_drop());
                txt_pick_drop_option.setText(str_pick_drop_option);
            }
        }


        if (detailsDataList.get(0).getOpen_at() != null && detailsDataList.get(0).getClose_at() != null) {
            if (!detailsDataList.get(0).getOpen_at().isEmpty() && !detailsDataList.get(0).getClose_at().isEmpty()) {

                garage_time.setText(detailsDataList.get(0).getOpen_at()+" - "+detailsDataList.get(0).getClose_at());
            }
        }

        if (detailsDataList.get(0).getAddress() != null) {
            if (!detailsDataList.get(0).getAddress().isEmpty()) {
                String str_address = CovertFirstLetterInUppercase(detailsDataList.get(0).getAddress());
                txt_address.setText(str_address);
            }
        }
        if (detailsDataList.get(0).getArea() != null) {
            if (!detailsDataList.get(0).getArea().isEmpty()) {
                String str_area = CovertFirstLetterInUppercase(detailsDataList.get(0).getArea());

                txt_area.setText("Area: "+str_area);
            }
        }
        if (detailsDataList.get(0).getPincode() != null) {
            if (!detailsDataList.get(0).getPincode().isEmpty()) {
                txt_pincode.setText("Pincode: "+detailsDataList.get(0).getPincode());
            }
        }

        if (detailsDataList.get(0).getContact_no() != null) {
            if (!detailsDataList.get(0).getContact_no().isEmpty()) {
                txt_garage_contact.setText(detailsDataList.get(0).getContact_no());
            }
        }


        if (detailsDataList.get(0).getWeek_off() != null) {
            if (!detailsDataList.get(0).getWeek_off().isEmpty()) {
                String str_weekoff = CovertFirstLetterInUppercase(detailsDataList.get(0).getWeek_off());
                txt_weekoff.setText(str_weekoff);
            }
        }
        if (detailsDataList.get(0).getServices() != null) {
            if (!detailsDataList.get(0).getServices().isEmpty()) {
              //  txt_garage_service.setText(detailsDataList.get(0).getServices());
                String garage_service = detailsDataList.get(0).getServices();
                garageServiceList = new ArrayList<String>(Arrays.asList(garage_service.split(",")));
                setGarageService(garageServiceList);

//                Log.d("loc= ",garage_service);
//                Log.d("loc= ", garageServiceList.get(1));
            }
        }
    }

    private void setGarageService(ArrayList<String> garageServiceList) {
        ServiceAdapter serviceAdapter = new ServiceAdapter(Garaagedetails.this,garageServiceList);
        rvservice.setAdapter(serviceAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rate_reviews_lyt:
                Intent intent = new Intent(getApplicationContext(), RateReviewsActivity.class);
                startActivity(intent);
                break;
            case R.id.burronnext:
                if (detailsDataList.get(0).getVendor_id()!=null) {
                    pref.edit().putString("garage_id",detailsDataList.get(0).getVendor_id()).commit();
                    pref.edit().putString("garage_name",detailsDataList.get(0).getGarage_name()).commit();
                    pref.edit().putString("garage_phone",detailsDataList.get(0).getContact_no()).commit();
                    pref.edit().putString("garage_service",detailsDataList.get(0).getServices()).commit();
                    pref.edit().putString("pick_drop",detailsDataList.get(0).getPick_drop()).commit();
                    pref.edit().putString("open_time",detailsDataList.get(0).getOpen_at()).commit();
                    pref.edit().putString("close_time",detailsDataList.get(0).getClose_at()).commit();

                    Intent i = new Intent(getApplicationContext(), Bookservice.class);
                    startActivity(i);
                    finish();
                }else
                {
                    Toast.makeText(this, "Data not loaded proper", Toast.LENGTH_SHORT).show();
                }
                    break;
        }
    }

    private class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
        Context context;
        ArrayList<String> mServiceList=new ArrayList<>();

        public ServiceAdapter(Context context, ArrayList<String> mServiceList) {
            this.context = context;
            this.mServiceList = mServiceList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View rootview = LayoutInflater.from(context).inflate(R.layout.servicelist_item, parent, false);
            return new ViewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String str_service = CovertFirstLetterInUppercase(mServiceList.get(position));

            holder.garage_service_name.setText(""+str_service);
            //Toast.makeText(context, ""+mServiceList.get(position), Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getItemCount() {
            return mServiceList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView garage_service_name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                garage_service_name = itemView.findViewById(R.id.garage_service_name);
            }
        }
    }
}
