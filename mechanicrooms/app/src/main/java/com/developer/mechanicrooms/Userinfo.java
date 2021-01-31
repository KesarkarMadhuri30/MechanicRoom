package com.developer.mechanicrooms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
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
import android.os.Vibrator;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.His.History;
import com.developer.mechanicrooms.Model.RegProfileModel;
import com.developer.mechanicrooms.Model.UserInfoModel;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;

import java.util.ArrayList;

public class Userinfo extends MyActivity implements View.OnClickListener {
    Button history, logoutt,aboutus,privacy,update;
    ImageView share_img,contact_img;
AppCompatEditText edt_name,edt_email;
TextView txt_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            edt_name = findViewById(R.id.edt_name);
            edt_email = findViewById(R.id.edt_email);
            txt_mobile = findViewById(R.id.txt_mobile);
            getRegisterDataUrl();

            txt_mobile.setOnClickListener(this);
            update = findViewById(R.id.update);
            update.setOnClickListener(this);

        }
            else {
                connected = false;
                Toast.makeText(Userinfo.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
            }
        share_img =findViewById(R.id.share_img);
        share_img.setOnClickListener(this);

        contact_img = findViewById(R.id.contact_img);
        contact_img.setOnClickListener(this);

        aboutus = findViewById(R.id.aboutus);
        history = (Button) findViewById(R.id.history);
        logoutt = (Button) findViewById(R.id.logoutt);

        aboutus.setOnClickListener(this);

        privacy =findViewById(R.id.privacy);
        privacy.setOnClickListener(this);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
            }
        });
        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentlog = new Intent(getApplicationContext(), RegPageActivity.class);
//                startActivity(intentlog);
                String alertmsg = "Do you really want to Logout?";
                LogoutAlertdialog(alertmsg);
            }
        });
    }

    private void getRegisterDataUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        final String phone_no = pref.getString("phone","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegProfileModel> reg_call = service.getAccountInfoUrl(phone_no);

        reg_call.enqueue(new Callback<RegProfileModel>() {
            @Override
            public void onResponse(Call<RegProfileModel> call, Response<RegProfileModel> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        ArrayList<UserInfoModel> accountInfo = response.body().getCustomerInfo();
                        setData(accountInfo);

                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                    }
                }
                else
                {
                    uploading.dismiss();
                    Toast.makeText(Userinfo.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegProfileModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void setData(ArrayList<UserInfoModel> accountInfo) {
        if (accountInfo.get(0).getName() != null) {
            if (!accountInfo.get(0).getName().isEmpty()) {
                edt_name.setText(accountInfo.get(0).getName());
            }
        }
        ///Toast.makeText(RegProfile.this, "" + accountInfo.get(0).getName(), Toast.LENGTH_SHORT).show();
        if (accountInfo.get(0).getEmail() != null) {
            if (!accountInfo.get(0).getEmail().isEmpty()) {
                edt_email.setText(accountInfo.get(0).getEmail());
            }
        }
        if (accountInfo.get(0).getPhone() != null) {
            if (!accountInfo.get(0).getPhone().isEmpty()) {
                txt_mobile.setText(accountInfo.get(0).getPhone());
            }
        }
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    private void LogoutAlertdialog(String alertmsg) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.main_alert_dialog, null);
        final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
        final Button btn_yes = (Button) alertLayout.findViewById(R.id.btn_yes);
        final Button btn_no = (Button) alertLayout.findViewById(R.id.btn_no);

        txt_dialog.setText(""+alertmsg);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putBoolean("logged_in", false).commit();
                Intent intent = new Intent(getApplicationContext(), RegPageActivity.class);
                startActivity(intent);
               // finish();
                finishAffinity();

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.update:
                if (edt_name.getText().toString().isEmpty())
                {
                    edt_name.setError("Enter name");
                    edt_name.requestFocus();
            }
                else if (edt_email.getText().toString().isEmpty())
            {
                edt_email.setError("Enter Email!");
                edt_email.requestFocus();

            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches())
            {
                edt_email.setError("Enter Valid Email!");
                edt_email.requestFocus();

            }
            else {
                String name = edt_name.getText().toString();
                String email = edt_email.getText().toString();
                String mobile = txt_mobile.getText().toString();
                userDataUrl(name,email,mobile);
                update.setEnabled(false);
            }
                break;
            case R.id.txt_mobile:
                Intent mob_intent = new Intent(getApplicationContext(), UpdateMobile.class);
                startActivity(mob_intent);
                break;
            case R.id.aboutus:
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.privacy:
                Intent i = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(i);
                break;
            case R.id.contact_img:
                Intent ii = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(ii);
                break;
            case R.id.share_img:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Thank you for sharing App with your Friends and Family! Click on below link to download app\n" + "\n";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
                // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Your friend shared: Playstore APP Link ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + getResources().getString(R.string.share_link));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
    }

    private void userDataUrl(String name, String email, String mobile) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String customerId = pref.getString("customer_id","");
        String user_type ="user";
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RegProfileModel> user_prof_call = service.postUserProfile(mobile,name,email,user_type);

        user_prof_call.enqueue(new Callback<RegProfileModel>() {
            @Override
            public void onResponse(Call<RegProfileModel> call, Response<RegProfileModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true)
                {
                    uploading.dismiss();
                    update.setEnabled(true);

                    String toastmsg = response.body().getMessage();
                    showUpdatemsg(toastmsg);
                    //pref.edit().putString("phone",mobile).commit();
                    // pref.edit().putString("id",response.body().getCustomerInfo().getId()).commit();
                    pref.edit().putString("customer_id", customerId).commit();
                    pref.edit().putBoolean("logged_in", true).commit();
                    pref.edit().putString("name",name).commit();
                    pref.edit().putString("email",email).commit();
                    pref.edit().putString("phone",mobile).commit();


                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    update.setEnabled(true);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    uploading.dismiss();
                    update.setEnabled(true);
                    Toast.makeText(Userinfo.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RegProfileModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void showUpdatemsg(String toastmsg) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.toast_window, null);
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
                    Intent ii = new Intent(Userinfo.this, Mainlastscreen.class);
                    startActivity(ii);
                    finish();
                }
            }
        };
        dialog.show();
        handler.postDelayed(runnable, 2000);
    }
}
