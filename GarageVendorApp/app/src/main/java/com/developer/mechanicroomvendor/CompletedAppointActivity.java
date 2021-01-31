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

public class CompletedAppointActivity extends MyActivity {
    RecyclerView complete_recyclerview;
    CompleteAppointAdapter apointAdapter;
    TextView txt_message;
    ArrayList<AppointDataModel> mAppointDataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

     
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
            complete_recyclerview = findViewById(R.id.complete_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            complete_recyclerview.setLayoutManager(layoutManager);
            complete_recyclerview.setNestedScrollingEnabled(false);
            txt_message = findViewById(R.id.txt_message);
            getApointUrl();

        }
        else {
            connected = false;
            Toast.makeText(CompletedAppointActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getApointUrl() {
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
                    complete_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    ArrayList<AppointDataModel> appointDataList=response.body().getBookingDataList();
                    getAppointDataUrl(appointDataList);
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    complete_recyclerview.setVisibility(View.GONE);
                    txt_message.setVisibility(View.VISIBLE);
                    String toastmsg = response.body().getMessage();
                    //showToastmsg(toastmsg);
                    txt_message.setText(""+toastmsg);
                }
                else
                {
                    complete_recyclerview.setVisibility(View.VISIBLE);
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

            if (appointDataList.get(i).getBooking_status().equals("4")) {
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
                apointAdapter = new CompleteAppointAdapter(getApplicationContext(),mAppointDataList);
                complete_recyclerview.setAdapter(apointAdapter);
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

    private class CompleteAppointAdapter extends RecyclerView.Adapter<CompleteAppointAdapter.ViewHolder> {
        Context context;
        ArrayList<AppointDataModel> allApointList;

        public CompleteAppointAdapter(Context context, ArrayList<AppointDataModel> allApointList) {
            this.context = context;
            this.allApointList = allApointList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.complete_listitem, parent, false);
            return new ViewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.cp_apoint_Date.setText(""+allApointList.get(position).getPick_date()+" "+allApointList.get(position).getPick_time());
            holder.cp_txtv_name.setText(""+allApointList.get(position).getCustomer_name());
            holder.cp_apoint_vtype.setText("Vehicle Type : "+allApointList.get(position).getVehical_type());
            holder.cp_apoint_servtype.setText("Service Type : "+allApointList.get(position).getService_requirement());

            holder.cp_apoint_book_no.setText(""+allApointList.get(position).getBooking_no());
            holder.cp_txtv_contact.setText(""+allApointList.get(position).getCustomer_phone());
            holder.cp_apoint_req.setText("Additional Requirement : "+allApointList.get(position).getAdditional_requirements());

            if (allApointList.get(position).getPayment_status()!=null)
            {
                if (allApointList.get(position).getPayment_status().equals("0"))
                {
                    holder.cp_payment_status.setText("Payment Status Pending");
                }
                else if (allApointList.get(position).getPayment_status().equals("1"))
                {
                    holder.cp_payment_status.setText("Payment Status Completed");
                }
            }

            holder.cp_service_payment.setText("Payment : "+allApointList.get(position).getService_payment());
        }

        @Override
        public int getItemCount() {
            return allApointList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cp_apoint_Date,cp_txtv_name,cp_apoint_vtype,cp_apoint_servtype,
                    cp_apoint_book_no,cp_txtv_contact,cp_apoint_req,cp_payment_status,cp_service_payment;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cp_apoint_Date = itemView.findViewById(R.id.cp_apoint_Date);
                cp_txtv_name = itemView.findViewById(R.id.cp_txtv_name);
                cp_apoint_vtype = itemView.findViewById(R.id.cp_apoint_vtype);
                cp_apoint_servtype = itemView.findViewById(R.id.cp_apoint_servtype);
                cp_apoint_book_no = itemView.findViewById(R.id.cp_apoint_book_no);
                cp_txtv_contact =itemView.findViewById(R.id.cp_txtv_contact);
                cp_apoint_req = itemView.findViewById(R.id.cp_apoint_req);
                cp_payment_status = itemView.findViewById(R.id.cp_payment_status);

                cp_service_payment = itemView.findViewById(R.id.cp_service_payment);
            }
        }
    }
}