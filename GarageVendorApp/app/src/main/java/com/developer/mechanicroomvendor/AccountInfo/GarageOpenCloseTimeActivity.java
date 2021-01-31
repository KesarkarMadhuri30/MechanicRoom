package com.developer.mechanicroomvendor.AccountInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.AccountInfoModel;
import com.developer.mechanicroomvendor.Model.GarageModel;
import com.developer.mechanicroomvendor.Model.GarageUpdateModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GarageOpenCloseTimeActivity extends MyActivity implements View.OnClickListener {
    Button open_time_btn,close_time_btn;
    TextView txt_open_time,txt_close_time;
    private int  mHour, mMinute;
    AppCompatEditText weekoff_edt;
    Button time_submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_open_close_time);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
        txt_open_time = findViewById(R.id.txt_open_time);

        open_time_btn = findViewById(R.id.open_time_btn);
        open_time_btn.setOnClickListener(this);

        txt_close_time = findViewById(R.id.txt_close_time);

        close_time_btn= findViewById(R.id.close_time_btn);
        close_time_btn.setOnClickListener(this);

        weekoff_edt = findViewById(R.id.weekoff_edt);
            weekoff_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    weekoff_edt.setCursorVisible(true);

                }
            });
            getOpenCloseDataUrl();

        time_submit_btn = findViewById(R.id.time_submit_btn);
        time_submit_btn.setOnClickListener(this);
        }
        else {
            connected = false;
            Toast.makeText(GarageOpenCloseTimeActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getOpenCloseDataUrl() {
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
        if (accountInfo.get(0).getOpen_at() != null) {
            if (!accountInfo.get(0).getOpen_at().isEmpty()) {
                if (!accountInfo.get(0).getOpen_at().equals("00:00:00")) {
                    txt_open_time.setText(accountInfo.get(0).getOpen_at());
                }
            }
        }
        if (accountInfo.get(0).getClose_at() != null) {
            if (!accountInfo.get(0).getClose_at().isEmpty()) {
                if (!accountInfo.get(0).getClose_at().equals("00:00:00")) {
                    txt_close_time.setText(accountInfo.get(0).getClose_at());
                }

            }

        }
        if (accountInfo.get(0).getWeek_off() != null) {
            if (!accountInfo.get(0).getWeek_off().isEmpty()) {
                weekoff_edt.setText(accountInfo.get(0).getWeek_off());
            }
        }
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.time_submit_btn:
                if (txt_open_time.getText().toString().equals(""))
                {
                    Toast.makeText(this, "Select Open time", Toast.LENGTH_SHORT).show();
                }else if (txt_close_time.getText().toString().equals("")) {
                    //lunch_time_dialog.dismiss();
                    Toast.makeText(this, "Select Open time", Toast.LENGTH_SHORT).show();


                }else if (weekoff_edt.getText().toString().isEmpty()) {
                    weekoff_edt.setError("Enter Week Off Day");
                    weekoff_edt.requestFocus();

                }
                else
                {
                   String open_at = txt_open_time.getText().toString();
                    String  close_at = txt_close_time.getText().toString();
                    String weekoff = weekoff_edt.getText().toString();

                    updateTimeDataUrl(open_at,close_at,weekoff);
                    time_submit_btn.setEnabled(false);
                }
                break;
            case R.id.open_time_btn:
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(GarageOpenCloseTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int hour = hourOfDay % 12;
                                if (hour == 0) hour = 12;
                                String _AM_PM = (hourOfDay > 12) ? "PM" : "AM";
                                // System.out.println(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM));
                                //et_time.setText(hourOfDay + ":" + minute);
                                txt_open_time.setText(""+(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM)));
                                // Log.d("***", String.valueOf(txt_dinner_from_time));


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.close_time_btn:
                // Get Current Time
                final Calendar cc = Calendar.getInstance();
                mHour = cc.get(Calendar.HOUR_OF_DAY);
                mMinute = cc.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int hour = hourOfDay % 12;
                                if (hour == 0) hour = 12;
                                String _AM_PM = (hourOfDay > 12) ? "PM" : "AM";
                                System.out.println(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM));

                                //et_time.setText(hourOfDay + ":" + minute);
                                txt_close_time.setText(""+(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM)));
                                Log.d("***", String.valueOf(txt_close_time));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog1.show();
                break;
        }
    }

    private void updateTimeDataUrl(String open_at, String close_at, String weekoff) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String user_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GarageUpdateModel> owner_prof_call = service.postGarageTimeUpdate(user_id,open_at,close_at,weekoff);

        owner_prof_call.enqueue(new Callback<GarageUpdateModel>() {
            @Override
            public void onResponse(Call<GarageUpdateModel> call, Response<GarageUpdateModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    time_submit_btn.setEnabled(true);
                    weekoff_edt.setCursorVisible(false);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                    getOpenCloseDataUrl();
                }
                else   if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    time_submit_btn.setEnabled(true);
                    String toastmsg = response.body().getMessage();
                    showUpdatemsg(toastmsg);
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