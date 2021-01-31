package com.developer.mechanicroomvendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.AppointDataModel;
import com.developer.mechanicroomvendor.Model.BookingModel;
import com.developer.mechanicroomvendor.Model.ServerResponse;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;

public class WorkingAppointActivity extends MyActivity {
    RecyclerView working_recyclerview;
    //ArrayList<ApointModel> mWorkngApointList= new ArrayList<ApointModel>();
    WorkingApointAdapter apointAdapter;
    TextView txt_message;

    AlertDialog dialog;
    ArrayList<AppointDataModel> mAppointDataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_appoint);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
        working_recyclerview = findViewById(R.id.working_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        working_recyclerview.setLayoutManager(layoutManager);
        working_recyclerview.setNestedScrollingEnabled(false);
            txt_message = findViewById(R.id.txt_message);
        getApointUrl();

        }
        else {
            connected = false;
            Toast.makeText(WorkingAppointActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getApointUrl() {
      /*  mWorkngApointList.clear();

        mWorkngApointList.add(new ApointModel("Satish Dalavi","Car","Repairing","20/12/2020","2.00 PM"));
        mWorkngApointList.add(new ApointModel("Vishal Mishra","Car","Servicing","10/12/2020","1.00 PM"));
        mWorkngApointList.add(new ApointModel("Sushant k","Bike","Servicing & Repairing","8/1/2020","10.00 AM"));
        mWorkngApointList.add(new ApointModel("Ramiz","Bike","Repairing","2/1/2020","4.00 PM"));

        apointAdapter = new WorkingApointAdapter(getApplicationContext(),mWorkngApointList);
        working_recyclerview.setAdapter(apointAdapter);*/

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
                    working_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    ArrayList<AppointDataModel> appointDataList=response.body().getBookingDataList();
                    getAppointDataUrl(appointDataList);
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    working_recyclerview.setVisibility(View.GONE);
                    txt_message.setVisibility(View.VISIBLE);
                    String toastmsg = response.body().getMessage();
                   // showToastmsg(toastmsg);
                    txt_message.setText(""+toastmsg);
                }
                else
                {
                    working_recyclerview.setVisibility(View.VISIBLE);
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
if (appointDataList.get(i).getBooking_status().equals("1")) {
//                if ("0".equals("0")) {
                 //   Toast.makeText(this, "equal or before date", Toast.LENGTH_SHORT).show();
//                    working_recyclerview.setVisibility(View.VISIBLE);
//                    txt_message.setVisibility(View.GONE);
                            AppointDataModel appointData = new AppointDataModel(appointDataList.get(i).getCustomer_id(),appointDataList.get(i).getCustomer_name(),
                            appointDataList.get(i).getCustomer_phone(),appointDataList.get(i).getBooking_no(),appointDataList.get(i).getVehical_type(),
                            appointDataList.get(i).getService_requirement(),appointDataList.get(i).getPick_date(),appointDataList.get(i).getPick_time(),
                            appointDataList.get(i).getPick_up_drop(),appointDataList.get(i).getAdditional_requirements(),appointDataList.get(i).getBooking_status(),
                            appointDataList.get(i).getRejection_reason(),appointDataList.get(i).getCancellation_reason(),
                            appointDataList.get(i).getService_payment(),appointDataList.get(i).getPayment_status());
                    mAppointDataList.add(appointData);
                    apointAdapter = new WorkingApointAdapter(getApplicationContext(),mAppointDataList);
                    working_recyclerview.setAdapter(apointAdapter);

                }
                else
                {
                   // Toast.makeText(this, "not before date ", Toast.LENGTH_SHORT).show();
//                    txt_message.setText("No Appointment were found");
//                    working_recyclerview.setVisibility(View.GONE);
//                    txt_message.setVisibility(View.VISIBLE);
                }

        }
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }


    private class WorkingApointAdapter extends RecyclerView.Adapter<WorkingApointAdapter.Viewholder> {
        Context context;
        ArrayList<AppointDataModel> allApointList;

        public WorkingApointAdapter(Context context, ArrayList<AppointDataModel> allApointList) {
            this.context = context;
            this.allApointList = allApointList;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.working_listitem, parent, false);
            return new Viewholder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
            holder.w_apoint_Date.setText(""+allApointList.get(position).getPick_date()+" "+allApointList.get(position).getPick_time());
            holder.w_txtv_name.setText(""+allApointList.get(position).getCustomer_name());
            holder.w_apoint_vtype.setText("Vehicle Type : "+allApointList.get(position).getVehical_type());
            holder.w_apoint_servtype.setText("Service Type : "+allApointList.get(position).getService_requirement());

            holder.w_apoint_book_no.setText(""+allApointList.get(position).getBooking_no());
            holder.w_txtv_contact.setText(""+allApointList.get(position).getCustomer_phone());
            holder.w_apoint_req.setText("Additional Requirement : "+allApointList.get(position).getAdditional_requirements());

            if(allApointList.get(position).getService_payment() !=null)
            {
                holder.txt_total.setText("Payment "+allApointList.get(position).getService_payment());
            }
            else
            {
                holder.txt_total.setText("Payment 0");
            }

            if (allApointList.get(position).getService_payment()!=null)
            {
                int service_payment = Integer.parseInt(allApointList.get(position).getService_payment());
                if (service_payment > 0)
                {
                    holder.btn_done.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.btn_done.setVisibility(View.GONE);
                }
            }

            holder.btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String booking_no = allApointList.get(position).getBooking_no();
                    String booking_status = "4";
                    BookingCompletedUrl(booking_no,booking_status);
                }
            });


            holder.total_lyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String booking_no =allApointList.get(position).getBooking_no();
                    PriceDialog(booking_no);
                }
            });

//            holder.btn_job_cart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(view.getContext(),JobCartActivity.class);
//                    startActivity(i);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return allApointList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            TextView w_apoint_Date,w_txtv_name,w_apoint_vtype,w_apoint_servtype,w_apoint_book_no,w_txtv_contact,w_apoint_req;
            RelativeLayout total_lyt;
           // Button btn_job_cart;
            Button btn_done;
            TextView txt_total;
            public Viewholder(@NonNull View itemView) {
                super(itemView);

                w_apoint_Date = itemView.findViewById(R.id.w_apoint_Date);
                w_txtv_name = itemView.findViewById(R.id.w_txtv_name);
                w_apoint_vtype = itemView.findViewById(R.id.w_apoint_vtype);
                w_apoint_servtype = itemView.findViewById(R.id.w_apoint_servtype);
                w_apoint_book_no = itemView.findViewById(R.id.w_apoint_book_no);
                w_txtv_contact =itemView.findViewById(R.id.w_txtv_contact);
                w_apoint_req = itemView.findViewById(R.id.w_apoint_req);
                btn_done =itemView.findViewById(R.id.btn_done);
                txt_total = itemView.findViewById(R.id.txt_total);
                total_lyt = itemView.findViewById(R.id.total_lyt);
               // btn_job_cart = itemView.findViewById(R.id.btn_job_cart);
            }
        }
    }

    private void BookingCompletedUrl(String booking_no, String booking_status) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(WorkingAppointActivity.this, "Loading", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ServerResponse> booking_status_call = service.putBookingStatus(booking_no,booking_status);

        booking_status_call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body()!=null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        getApointUrl();
                        apointAdapter.notifyDataSetChanged();
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

    private void PriceDialog(final String booking_no) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.payment_dialog, null);
        final EditText edt_payment = alertLayout.findViewById(R.id.edt_payment);
        final Button btn_submit = (Button) alertLayout.findViewById(R.id.btn_submit);

        final AlertDialog.Builder alert = new AlertDialog.Builder(WorkingAppointActivity.this);

        alert.setView(alertLayout);
        alert.setCancelable(false);
          dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(true);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_payment.getText().toString().isEmpty())
                {
                    edt_payment.setError("Enter Amount");
                    edt_payment.requestFocus();
                }
                else
                {
//                    txt_total.setText("Total Price "+ edt_payment.getText().toString());
                    String service_pay = edt_payment.getText().toString();
                    submitPayment(booking_no,service_pay);
                    dialog.dismiss();

                }

            }
        });


        dialog.show();
    }

    private void submitPayment(String booking_no,String service_pay) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ServerResponse> payment_call = service.putServicePayment(booking_no,service_pay);

        payment_call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    dialog.dismiss();
                    getApointUrl();
                    apointAdapter.notifyDataSetChanged();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);

                }
                else   if (response.body().isStatus() == false)
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
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }
}