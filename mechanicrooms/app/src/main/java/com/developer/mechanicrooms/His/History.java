package com.developer.mechanicrooms.His;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.HistoryDataModel;
import com.developer.mechanicrooms.Model.HistoryModel;
import com.developer.mechanicrooms.R;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;

import java.util.ArrayList;

public class History extends MyActivity {
    RecyclerView history_recyclerview;
    TextView txt_message;
    ArrayList<HistoryDataModel> mHistoryDataList=new ArrayList<>();
    AdapterHistory adapterHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
            history_recyclerview = findViewById(R.id.history_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            history_recyclerview.setLayoutManager(layoutManager);
            history_recyclerview.setNestedScrollingEnabled(false);
            txt_message = findViewById(R.id.txt_message);
            getApointUrl();

        }
        else {
            connected = false;
            Toast.makeText(History.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getApointUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String customer_id = pref.getString("customer_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<HistoryModel> history_call = service.getCustomerHistory(customer_id);

        history_call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    history_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    ArrayList<HistoryDataModel> historyDataList=response.body().getHistoryDataList();
                    getAppointDataUrl(historyDataList);
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    history_recyclerview.setVisibility(View.GONE);
                    txt_message.setVisibility(View.VISIBLE);
                    String toastmsg = response.body().getMessage();
                    //showToastmsg(toastmsg);
                    txt_message.setText(""+toastmsg);
                }
                else
                {
                    history_recyclerview.setVisibility(View.VISIBLE);
                    txt_message.setVisibility(View.GONE);
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void getAppointDataUrl(ArrayList<HistoryDataModel> historyDataList) {
        adapterHistory = new AdapterHistory(getApplicationContext(),historyDataList);
        history_recyclerview.setAdapter(adapterHistory);

//        mHistoryDataList.clear();
//        for (int i=0;i<historyDataList.size();i++) {
//            int service_payment = 0;
//            if (historyDataList.get(i).getService_payment() !=null) {
//                 service_payment = Integer.parseInt(historyDataList.get(i).getService_payment());
//            }
//            if (service_payment>0) {
//
//                HistoryDataModel historyData = new HistoryDataModel(historyDataList.get(i).getBooking_no(),historyDataList.get(i).getGarage_name(),
//                        historyDataList.get(i).getGarage_phone(),historyDataList.get(i).getVehical_type(),
//                        historyDataList.get(i).getService_requirement(),historyDataList.get(i).getService_payment(),
//                        historyDataList.get(i).getPayment_status(),
//                        historyDataList.get(i).getPick_date(),historyDataList.get(i).getPick_time(),
//                        historyDataList.get(i).getPick_up_drop(),historyDataList.get(i).getAdditional_requirements(),
//                        historyDataList.get(i).getBooking_status(),
//                        historyDataList.get(i).getRejection_reason(),historyDataList.get(i).getCancellation_reason());
//                mHistoryDataList.add(historyData);
//
//                adapterHistory = new AdapterHistory(getApplicationContext(),mHistoryDataList);
//              history_recyclerview.setAdapter(adapterHistory);
//            }
//            else
//            {
//                // Toast.makeText(this, "not before date ", Toast.LENGTH_SHORT).show();
////                    txt_message.setText("No Appointment were found");
////                    reject_recyclerview.setVisibility(View.GONE);
////                    txt_message.setVisibility(View.VISIBLE);
//            }
    //    }
    }

    private class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.viewHolder> {
        Context context;
        ArrayList<HistoryDataModel> mHistoryDataList;

        public AdapterHistory(Context context, ArrayList<HistoryDataModel> mHistoryDataList) {
            this.context = context;
            this.mHistoryDataList = mHistoryDataList;
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.history_listitem, parent, false);
            return new viewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            holder.txt_date.setText(""+mHistoryDataList.get(position).getPick_date()+" "+mHistoryDataList.get(position).getPick_time());
            holder.txt_grg_name.setText(""+mHistoryDataList.get(position).getGarage_name());
            holder.txt_bike_dtl.setText("Vehicle Type : "+mHistoryDataList.get(position).getVehical_type());
            holder.txt_servtype.setText("Service Type : "+mHistoryDataList.get(position).getService_requirement());

            holder.txt_book_no.setText(""+mHistoryDataList.get(position).getBooking_no());
            holder.txt_req.setText("Additional Requirement : "+mHistoryDataList.get(position).getAdditional_requirements());


            if(mHistoryDataList.get(position).getService_payment() !=null)
            {
                holder.txt_Payment_amt.setText("Payment : "+mHistoryDataList.get(position).getService_payment());
            }
            else
            {
                holder.txt_Payment_amt.setText("Payment 0");
            }

            if (mHistoryDataList.get(position).getBooking_status().equals("0")) {
                holder.iv_timeline_indicator.setImageResource(R.drawable.ic_baseline_stars_yellow);
                holder.tv_timeline_indicator_line.setBackgroundResource(R.color.yellow_500);
                holder.txt_payment_status.setBackgroundResource(R.drawable.btn_cardbg);
                holder.txt_payment_status.setText("Pending");
            }
            else  if (mHistoryDataList.get(position).getBooking_status().equals("1")) {
                holder.iv_timeline_indicator.setImageResource(R.drawable.ic_baseline_stars_teal);
                holder.tv_timeline_indicator_line.setBackgroundResource(R.color.teal_500);
                holder.txt_payment_status.setBackgroundResource(R.drawable.btn_cardbg1);
                holder.txt_payment_status.setText("Accepted");
            }else  if (mHistoryDataList.get(position).getBooking_status().equals("2")) {
                 holder.iv_timeline_indicator.setImageResource(R.drawable.ic_baseline_stars_orange);
                holder.tv_timeline_indicator_line.setBackgroundResource(R.color.orange_600);
                holder.txt_payment_status.setBackgroundResource(R.drawable.btn_cardbg2);
                holder.txt_payment_status.setText("Rejected");
            }
            else  if (mHistoryDataList.get(position).getBooking_status().equals("3")) {
                 holder.iv_timeline_indicator.setImageResource(R.drawable.ic_baseline_stars_red);
                holder.tv_timeline_indicator_line.setBackgroundResource(R.color.red_500);
                holder.txt_payment_status.setBackgroundResource(R.drawable.btn_cardbg3);
                holder.txt_payment_status.setText("Canceled");
            }
            else   if (mHistoryDataList.get(position).getBooking_status().equals("4")) {
                holder.iv_timeline_indicator.setImageResource(R.drawable.ic_baseline_stars_24);
                holder.tv_timeline_indicator_line.setBackgroundResource(R.color.light_green_500);
                holder.txt_payment_status.setBackgroundResource(R.drawable.btn_cardbg4);
                holder.txt_payment_status.setText("Completed");
            }else
            {

            }

            if (mHistoryDataList.get(position).getPayment_status()!=null)
            {
                if (mHistoryDataList.get(position).getPayment_status().equals("0"))
                {
                   // holder.txt_payment_status.setText("Pending");

                    if (mHistoryDataList.get(position).getService_payment()!=null)
                    {
                        int payment = Integer.parseInt(mHistoryDataList.get(position).getService_payment());
                        if (payment >0)
                        {
                            holder.btn_pay.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            holder.btn_pay.setVisibility(View.GONE);
                        }
                    }

                }
                else if (mHistoryDataList.get(position).getPayment_status().equals("1"))
                {
                   // holder.txt_payment_status.setText("Completed");
                    holder.btn_pay.setVisibility(View.GONE);
                }
            }


        }

        @Override
        public int getItemCount() {
            return mHistoryDataList.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            TextView txt_date,txt_grg_name,txt_bike_dtl,txt_servtype,txt_Payment_amt,
                    txt_book_no,txt_req,txt_payment_status;
            Button btn_pay;
            ImageView iv_timeline_indicator;
            TextView tv_timeline_indicator_line;
            public viewHolder(@NonNull View itemView) {
                super(itemView);
                txt_book_no = itemView.findViewById(R.id.txt_book_no);
                txt_date = itemView.findViewById(R.id.txt_date);
                txt_grg_name = itemView.findViewById(R.id.txt_grg_name);
                txt_bike_dtl = itemView.findViewById(R.id.txt_bike_dtl);
                txt_Payment_amt = itemView.findViewById(R.id.txt_Payment_amt);
                txt_payment_status = itemView.findViewById(R.id.txt_payment_status);
                txt_servtype = itemView.findViewById(R.id.txt_servtype);
                txt_req = itemView.findViewById(R.id.txt_req);

                btn_pay = itemView.findViewById(R.id.btn_pay);
                iv_timeline_indicator = itemView.findViewById(R.id.iv_timeline_indicator);
                tv_timeline_indicator_line = itemView.findViewById(R.id.tv_timeline_indicator_line);
            }
        }
    }
}
