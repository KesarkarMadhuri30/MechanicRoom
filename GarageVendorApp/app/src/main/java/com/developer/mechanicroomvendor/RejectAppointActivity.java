package com.developer.mechanicroomvendor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.AppointDataModel;
import com.developer.mechanicroomvendor.Model.BookingModel;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;

public class RejectAppointActivity extends MyActivity {

    RecyclerView reject_recyclerview;
  //  ArrayList<ApointModel> mApointList= new ArrayList<ApointModel>();
    RejectAppointAdapter apointAdapter;
    TextView txt_message;
    ArrayList<AppointDataModel> mAppointDataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_appoint);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
        reject_recyclerview = findViewById(R.id.reject_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reject_recyclerview.setLayoutManager(layoutManager);
        reject_recyclerview.setNestedScrollingEnabled(false);
            txt_message = findViewById(R.id.txt_message);
        getApointUrl();

    }
        else {
        connected = false;
        Toast.makeText(RejectAppointActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
    }
    }
    private void getApointUrl() {
//        mApointList.clear();
//
//        mApointList.add(new ApointModel("Satish Dalavi","Car","Repairing","20/12/2020","2.00 PM"));
//        mApointList.add(new ApointModel("Vishal Mishra","Car","Servicing","10/12/2020","1.00 PM"));
//        mApointList.add(new ApointModel("Sushant k","Bike","Servicing & Repairing","8/1/2020","10.00 AM"));
//        mApointList.add(new ApointModel("Ramiz","Bike","Repairing","2/1/2020","4.00 PM"));
//
//        apointAdapter = new RejectAppointAdapter(getApplicationContext(),mApointList);
//        reject_recyclerview.setAdapter(apointAdapter);

        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String vendor_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<BookingModel> booking_call = service.getGarageAppointment(vendor_id);

        booking_call.enqueue(new Callback<BookingModel>() {
            @Override
            public void onResponse(Call<BookingModel> call, Response<BookingModel> response) {
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    reject_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    ArrayList<AppointDataModel> appointDataList=response.body().getBookingDataList();
                    getAppointDataUrl(appointDataList);
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    reject_recyclerview.setVisibility(View.GONE);
                    txt_message.setVisibility(View.VISIBLE);
                    String toastmsg = response.body().getMessage();
                    //showToastmsg(toastmsg);
                    txt_message.setText(""+toastmsg);
                }
                else
                {
                    reject_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BookingModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void getAppointDataUrl(ArrayList<AppointDataModel> appointDataList) {
        mAppointDataList.clear();
        for (int i=0;i<appointDataList.size();i++) {

                if (appointDataList.get(i).getBooking_status().equals("2")) {
                  //  Toast.makeText(this, "equal or before date", Toast.LENGTH_SHORT).show();
//                    reject_recyclerview.setVisibility(View.VISIBLE);
//                    txt_message.setVisibility(View.GONE);
                    AppointDataModel appointData = new AppointDataModel(appointDataList.get(i).getCustomer_id(),appointDataList.get(i).getCustomer_name(),
                            appointDataList.get(i).getCustomer_phone(),appointDataList.get(i).getBooking_no(),appointDataList.get(i).getVehical_type(),
                            appointDataList.get(i).getService_requirement(),appointDataList.get(i).getPick_date(),appointDataList.get(i).getPick_time(),
                            appointDataList.get(i).getPick_up_drop(),appointDataList.get(i).getAdditional_requirements(),appointDataList.get(i).getBooking_status(),
                            appointDataList.get(i).getRejection_reason(),appointDataList.get(i).getCancellation_reason(),
                            appointDataList.get(i).getService_payment(),appointDataList.get(i).getPayment_status());
                    mAppointDataList.add(appointData);
                    apointAdapter = new RejectAppointAdapter(getApplicationContext(),mAppointDataList);
                    reject_recyclerview.setAdapter(apointAdapter);
                }
                else
                {
                   // Toast.makeText(this, "not before date ", Toast.LENGTH_SHORT).show();
//                    txt_message.setText("No Appointment were found");
//                    reject_recyclerview.setVisibility(View.GONE);
//                    txt_message.setVisibility(View.VISIBLE);
                }
        }
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }


    private class RejectAppointAdapter extends RecyclerView.Adapter<RejectAppointAdapter.Viewholder> {
        Context context;
        ArrayList<AppointDataModel> allApointList;

        public RejectAppointAdapter(Context context, ArrayList<AppointDataModel> allApointList) {
            this.context = context;
            this.allApointList = allApointList;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.reject_listitem, parent, false);
            return new Viewholder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            holder.rj_apoint_Date.setText(""+allApointList.get(position).getPick_date()+" "+allApointList.get(position).getPick_time());
            holder.rj_txtv_name.setText(""+allApointList.get(position).getCustomer_name());
            holder.rj_apoint_vtype.setText("Vehicle Type : "+allApointList.get(position).getVehical_type());
            holder.rj_apoint_servtype.setText("Service Type : "+allApointList.get(position).getService_requirement());

            holder.rj_apoint_book_no.setText(""+allApointList.get(position).getBooking_no());
            holder.rj_txtv_contact.setText(""+allApointList.get(position).getCustomer_phone());
            holder.rj_apoint_req.setText("Additional Requirement : "+allApointList.get(position).getAdditional_requirements());
        }

        @Override
        public int getItemCount() {
            return allApointList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            TextView rj_apoint_Date,rj_txtv_name,rj_apoint_vtype,rj_apoint_servtype,rj_apoint_book_no,rj_txtv_contact,rj_apoint_req;

            public Viewholder(@NonNull View itemView) {
                super(itemView);

                rj_apoint_Date = itemView.findViewById(R.id.rj_apoint_Date);
                rj_txtv_name = itemView.findViewById(R.id.rj_txtv_name);
                rj_apoint_vtype = itemView.findViewById(R.id.rj_apoint_vtype);
                rj_apoint_servtype = itemView.findViewById(R.id.rj_apoint_servtype);
                rj_apoint_book_no = itemView.findViewById(R.id.rj_apoint_book_no);
                rj_txtv_contact =itemView.findViewById(R.id.rj_txtv_contact);
                rj_apoint_req = itemView.findViewById(R.id.rj_apoint_req);
            }
        }
    }

}