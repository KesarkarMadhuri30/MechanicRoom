package com.developer.mechanicroomvendor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.AppointDataModel;
import com.developer.mechanicroomvendor.Model.BookingModel;
import com.developer.mechanicroomvendor.Model.ServerResponse;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleAppointActivity extends MyActivity {
    RecyclerView schedule_recyclerview;
   // ArrayList<ApointModel> mApointList= new ArrayList<ApointModel>();
    ScheduleApointAdapter apointAdapter;
    TextView txt_message;
    ArrayList<AppointDataModel> mAppointDataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appoint);

        schedule_recyclerview = findViewById(R.id.schedule_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        schedule_recyclerview.setLayoutManager(layoutManager);
        schedule_recyclerview.setNestedScrollingEnabled(false);

        txt_message = findViewById(R.id.txt_message);

        getApointUrl(); 
    }
    private void getApointUrl() {
//        mApointList.clear();
//
//        mApointList.add(new ApointModel("Satish Dalavi","Car","Repairing","20/12/2020","2.00 PM"));
//        mApointList.add(new ApointModel("Vishal Mishra","Car","Servicing","10/12/2020","1.00 PM"));
//        mApointList.add(new ApointModel("Sushant k","Bike","Servicing & Repairing","8/1/2020","10.00 AM"));
//        mApointList.add(new ApointModel("Ramiz","Bike","Repairing","2/1/2020","4.00 PM"));
//
//        apointAdapter = new ScheduleApointAdapter(getApplicationContext(),mApointList);
//        schedule_recyclerview.setAdapter(apointAdapter);

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
                    schedule_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    ArrayList<AppointDataModel> appointDataList=response.body().getBookingDataList();
                    getAppointDataUrl(appointDataList);
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    schedule_recyclerview.setVisibility(View.GONE);
                    txt_message.setVisibility(View.VISIBLE);
                    String toastmsg = response.body().getMessage();
                  //  showToastmsg(toastmsg);
                    txt_message.setText(""+toastmsg);
                }
                else
                {
                    schedule_recyclerview.setVisibility(View.VISIBLE);
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
//
        mAppointDataList.clear();
        for (int i=0;i<appointDataList.size();i++) {
            String compareDate = appointDataList.get(i).getPick_date();
            //Log.d("&&&", compareDate);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modifiedDate= sdf.format(date);

            Date strDate = null;
            Date todayDate=null;
            // Log.d("&&&", modifiedDate);
            try {
                strDate = sdf.parse(compareDate);
                todayDate = sdf.parse(modifiedDate);
                //  Log.d("&&&", String.valueOf(strDate));
                //  Log.d("&&&", String.valueOf(todayDate));

                // System.currentTimeMillis()
                if (strDate.after(todayDate)&& !todayDate.equals(strDate) ) {
                    if (appointDataList.get(i).getBooking_status().equals("0")) {
                        // Toast.makeText(this, "equal or before date", Toast.LENGTH_SHORT).show();
                    schedule_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                        AppointDataModel appointData = new AppointDataModel(appointDataList.get(i).getCustomer_id(), appointDataList.get(i).getCustomer_name(),
                                appointDataList.get(i).getCustomer_phone(), appointDataList.get(i).getBooking_no(), appointDataList.get(i).getVehical_type(),
                                appointDataList.get(i).getService_requirement(), appointDataList.get(i).getPick_date(), appointDataList.get(i).getPick_time(),
                                appointDataList.get(i).getPick_up_drop(), appointDataList.get(i).getAdditional_requirements(), appointDataList.get(i).getBooking_status(),
                                appointDataList.get(i).getRejection_reason(), appointDataList.get(i).getCancellation_reason(),
                                appointDataList.get(i).getService_payment(),appointDataList.get(i).getPayment_status());
                        mAppointDataList.add(appointData);
                        apointAdapter = new ScheduleApointAdapter(getApplicationContext(), mAppointDataList);
                        schedule_recyclerview.setAdapter(apointAdapter);
                    } else {
                        // Toast.makeText(this, "not before date ", Toast.LENGTH_SHORT).show();
                    txt_message.setText("No Appointment were found");
                    schedule_recyclerview.setVisibility(View.GONE);
                    txt_message.setVisibility(View.VISIBLE);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    private class ScheduleApointAdapter extends RecyclerView.Adapter<ScheduleApointAdapter.Viewholder> {
        Context context;
        ArrayList<AppointDataModel> allApointList;

        public ScheduleApointAdapter(Context context, ArrayList<AppointDataModel> allApointList) {
            this.context = context;
            this.allApointList = allApointList;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.schedule_listitem, parent, false);
            return new Viewholder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
            holder.sch_apoint_Date.setText(""+allApointList.get(position).getPick_date()+" "+allApointList.get(position).getPick_time());
            holder.sch_txtv_name.setText(""+allApointList.get(position).getCustomer_name());
            holder.sch_apoint_vtype.setText("Vehicle Type : "+allApointList.get(position).getVehical_type());
            holder.sch_apoint_servtype.setText("Service Type : "+allApointList.get(position).getService_requirement());

            holder.sch_apoint_book_no.setText(""+allApointList.get(position).getBooking_no());
            holder.sch_txtv_contact.setText(""+allApointList.get(position).getCustomer_phone());
            holder.sch_apoint_req.setText("Additional Requirement : "+allApointList.get(position).getAdditional_requirements());

            holder.sch_btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String booking_no = allApointList.get(position).getBooking_no();
                    String booking_status = "1";
                    BookingAcceptRejectUrl(booking_no,booking_status);
                }
            });
            holder.sch_btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String booking_no = allApointList.get(position).getBooking_no();
                    String booking_status = "2";
                    BookingAcceptRejectUrl(booking_no,booking_status);
                }
            });
        }

        private void BookingAcceptRejectUrl(String booking_no, String booking_status) {
            final ProgressDialog uploading;
            uploading = ProgressDialog.show(ScheduleAppointActivity.this, "Loading", "Please wait...", false, false);

            String vendor_id = pref.getString("vendor_id","");
            RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
            Call<ServerResponse> booking_status_call = service.putBookingStatus(booking_no,booking_status);

            booking_status_call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.body()!=null) {
                        if (response.body().isStatus() == true) {
                            uploading.dismiss();
                            getApointUrl();
                            String toastmsg = response.body().getMessage();
                            showToastmsg(toastmsg);

                        } else if (response.body().isStatus() == false) {
                            uploading.dismiss();
                            String toastmsg = response.body().getMessage();
                            showToastmsg(toastmsg);

                        } else {

                            uploading.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    uploading.dismiss();
                }
            });

        }


        @Override
        public int getItemCount() {
            return allApointList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            TextView sch_apoint_Date,sch_txtv_name,sch_apoint_vtype,sch_apoint_servtype,sch_apoint_book_no,
                    sch_txtv_contact,sch_apoint_req,sch_btn_reject,sch_btn_accept;
            public Viewholder(@NonNull View itemView) {
                super(itemView);

                sch_apoint_Date = itemView.findViewById(R.id.sch_apoint_Date);
                sch_txtv_name = itemView.findViewById(R.id.sch_txtv_name);
                sch_apoint_vtype = itemView.findViewById(R.id.sch_apoint_vtype);
                sch_apoint_servtype = itemView.findViewById(R.id.sch_apoint_servtype);

                sch_apoint_book_no = itemView.findViewById(R.id.sch_apoint_book_no);
                sch_txtv_contact =itemView.findViewById(R.id.sch_txtv_contact);
                sch_apoint_req = itemView.findViewById(R.id.sch_apoint_req);
                sch_btn_accept = itemView.findViewById(R.id.sch_btn_accept);
                sch_btn_reject = itemView.findViewById(R.id.sch_btn_reject);
            }
        }
    }
  
}