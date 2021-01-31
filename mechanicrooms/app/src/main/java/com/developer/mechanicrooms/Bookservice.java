package com.developer.mechanicrooms;

import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.BookingModel;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Bookservice extends MyActivity implements View.OnClickListener {
    public static final String inputFormat = "hh:mm";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    Button btn_bookservice;
    ImageView back_img;
   // Spinner service_spinner;
   // String str_service_spinner;

    CheckBox ch_serv,ch_rep,ch_serv_rep,ch_other;
    ArrayList<String> vehi_ServiceList = new ArrayList<>();

    Spinner pick_drop_spinner;
    String str_pick_drop_spinner;

    String[] pick_drop_option_list = {"Yes", "No"};

    TextView booking_time,booking_date;
    private int  mHour, mMinute;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    EditText edt_additonal_req;
    TextView txt_vehicle_type;
    ArrayList<String> garage_serviceList;
    LinearLayout pick_drop_spinner_lyt;
    String  garage_pick_drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookservice);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;


//           String  garage_service = pref.getString("garage_service","");
//            garage_serviceList = new ArrayList<String>(Arrays.asList(garage_service.split(",")));
//
//            service_spinner = findViewById(R.id.service_spinner);
//            ArrayAdapter serviceadapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, garage_serviceList);
//            service_spinner.setAdapter(serviceadapter);
//
//            service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    str_service_spinner = service_spinner.getItemAtPosition(i).toString();
//                   // Toast.makeText(Bookservice.this, ""+str_service_spinner, Toast.LENGTH_SHORT).show();
//
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });

            back_img = findViewById(R.id.back_img);
            back_img.setOnClickListener(this);
            txt_vehicle_type = findViewById(R.id.txt_vehicle_type);
            txt_vehicle_type.setText(""+pref.getString("vehicle_type",""));

            edt_additonal_req = findViewById(R.id.edt_additonal_req);

            booking_date = findViewById(R.id.booking_date);
            booking_date.setOnClickListener(this);

            booking_time = findViewById(R.id.booking_time);
            booking_time.setOnClickListener(this);

            pick_drop_spinner_lyt = findViewById(R.id.pick_drop_spinner_lyt);
            pick_drop_spinner = findViewById(R.id.pick_drop_spinner);
              garage_pick_drop = pref.getString("pick_drop","");
            if (garage_pick_drop !=null)
            {
                if (!garage_pick_drop.isEmpty())
                {
                    if (garage_pick_drop.equals("Yes"))
                    {
                        pick_drop_spinner_lyt.setVisibility(View.VISIBLE);
                    }
                    else if (garage_pick_drop.equals("No"))
                    {
                        pick_drop_spinner_lyt.setVisibility(View.GONE);
                    }
                }
            }

            ArrayAdapter minadapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, pick_drop_option_list);
            pick_drop_spinner.setAdapter(minadapter);

            pick_drop_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    str_pick_drop_spinner = pick_drop_spinner.getItemAtPosition(i).toString();
                   // Toast.makeText(Bookservice.this, ""+str_pick_drop_spinner, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ch_serv = findViewById(R.id.ch_serv);
            ch_rep = findViewById(R.id.ch_rep);
            ch_serv_rep = findViewById(R.id.ch_serv_rep);
            ch_other = findViewById(R.id.ch_other);

            ch_serv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (!isChecked) {
                        //  cat_type_house.setChecked(true);
                        // cat_type_plot.setChecked(true);
                        // Log.i("checker", "true");
                        vehi_ServiceList.remove(ch_serv.getText().toString());
                    } else {
                        Log.i("checker", "false");
                        vehi_ServiceList.add(ch_serv.getText().toString());
//                        ch_rep.setChecked(false);
//                        ch_serv_rep.setChecked(false);
//                        ch_other.setChecked(false);
                        // subcatList.add(categoryType);
                    }
                }
            });



            ch_rep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (!isChecked) {
                        //  cat_type_house.setChecked(true);
                        // cat_type_plot.setChecked(true);
                        // Log.i("checker", "true");
                        vehi_ServiceList.remove(ch_rep.getText().toString());
                    } else {
                        Log.i("checker", "false");
                        vehi_ServiceList.add(ch_rep.getText().toString());
//                        ch_serv.setChecked(false);
//                        ch_serv_rep.setChecked(false);
//                        ch_other.setChecked(false);
                        // subcatList.add(categoryType);
                    }
                }
            });

            ch_serv_rep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (!isChecked) {
                        //  cat_type_house.setChecked(true);
                        // cat_type_plot.setChecked(true);
                        // Log.i("checker", "true");
                        vehi_ServiceList.remove(ch_serv_rep.getText().toString());
                    } else {
                        Log.i("checker", "false");
                        vehi_ServiceList.add(ch_serv_rep.getText().toString());
//                        ch_rep.setChecked(false);
//                        ch_serv.setChecked(false);
//                        ch_other.setChecked(false);
                        // subcatList.add(categoryType);
                    }
                }
            });

            ch_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (!isChecked) {
                        //  cat_type_house.setChecked(true);
                        // cat_type_plot.setChecked(true);
                        // Log.i("checker", "true");
                        vehi_ServiceList.remove(ch_other.getText().toString());
                    } else {
                        Log.i("checker", "false");
                        vehi_ServiceList.add(ch_other.getText().toString());
//                        ch_rep.setChecked(false);
//                        ch_serv.setChecked(false);
//                        ch_serv_rep.setChecked(false);
                        // subcatList.add(categoryType);
                    }
                }
            });



            btn_bookservice = findViewById(R.id.btn_bookservice);
            btn_bookservice.setOnClickListener(this);
        }
        else {
            connected = false;
            Toast.makeText(Bookservice.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Garaagedetails.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_img:
                Intent i = new Intent(getApplicationContext(), Garaagedetails.class);
                startActivity(i);
                finish();
                break;
            case R.id.booking_time:
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Bookservice.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int hour = hourOfDay % 12;
                                if (hour == 0) hour = 12;
                                String _AM_PM = (hourOfDay > 12) ? "PM" : "AM";
                                //  System.out.println(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM));
                                //et_time.setText(hourOfDay + ":" + minute);
                                booking_time.setText(""+(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM)));

                                /*
                                Time condition

                                String open_time = pref.getString("open_time","");
                                String close_time = pref.getString("close_time","");

                                 Date date = parseDate(hour + ":" + minute);
                                Date  dateCompareOne = parseDate(open_time);
                                Date  dateCompareTwo = parseDate(close_time);

                                if ( date.before( dateCompareOne ) && date.after(dateCompareTwo)) {
                                    Toast.makeText(Bookservice.this, "Select time between "+open_time+ " to "+close_time, Toast.LENGTH_SHORT).show();
                                    booking_time.setText("");
                                }
                                else
                                {
                                    booking_time.setText(""+(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM)));

                                }
*/
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.booking_date:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(Bookservice.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                booking_date.setText(year  + "/" + (month + 1) + "/" +day );
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

            case R.id.btn_bookservice:
                if (txt_vehicle_type.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "select vehicle type", Toast.LENGTH_SHORT).show();

                }
                else if (vehi_ServiceList.isEmpty())
                {
                    Toast.makeText(this, "select service", Toast.LENGTH_SHORT).show();

                }
                else if (booking_date.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "select Date", Toast.LENGTH_SHORT).show();

                }
                else  if (booking_time.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "select time", Toast.LENGTH_SHORT).show();

                }
             
                else {
                    if (garage_pick_drop !=null)
                    {
                        if (garage_pick_drop.equals("Yes"))
                        {
                            if (str_pick_drop_spinner.isEmpty())
                            {
                                Toast.makeText(this, "Select Pick-drop option", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String v_type = txt_vehicle_type.getText().toString();
                                String v_service = vehi_ServiceList.toString();
                                String b_date = booking_date.getText().toString();
                                String b_time = booking_time.getText().toString();
                                String pick = str_pick_drop_spinner;
                                String addi_req;
                                if (edt_additonal_req.getText().toString().isEmpty()) {
                                    addi_req = "no";
                                } else
                                {
                                     addi_req = edt_additonal_req.getText().toString();
                                   }
                                Log.d("@@@",v_service);
                                submitBookingDataUrl(v_type,v_service,b_date,b_time,pick,addi_req);
                                btn_bookservice.setEnabled(false);
                            }
                        }
                        else if (garage_pick_drop.equals("No"))
                        {

                            String v_type = txt_vehicle_type.getText().toString();
                            String v_service =  vehi_ServiceList.toString();
                            String b_date = booking_date.getText().toString();
                            String b_time = booking_time.getText().toString();
                            String pick = "No";
                            String addi_req;
                            if (edt_additonal_req.getText().toString().isEmpty()) {
                                addi_req = "no";
                            } else
                            {
                                addi_req = edt_additonal_req.getText().toString();
                            }
                            Log.d("@@@",v_service);
                          submitBookingDataUrl(v_type,v_service,b_date,b_time,pick,addi_req);
                            btn_bookservice.setEnabled(false);
                        }
                    }

                }
//                Intent intentnew = new Intent(getApplicationContext(), Slotbook.class);
//                startActivity( intentnew);
                break;

        }
    }

    private Date parseDate(String date) {
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    private void submitBookingDataUrl(String vehicle_type, String v_service, String b_date, String b_time, String pick,String addi_req) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String customer_id = pref.getString("customer_id","");
        String customer_name = pref.getString("name","");
        String customer_phone = pref.getString("phone","");
        String garage_id = pref.getString("garage_id","");
        String garage_name = pref.getString("garage_name","");
        String garage_phone = pref.getString("garage_phone","");

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<BookingModel> booking_call = service.postServiceBooking(customer_id,customer_name,customer_phone,garage_id,
                garage_name,garage_phone,vehicle_type,v_service,b_date,b_time,pick,addi_req);

        booking_call.enqueue(new Callback<BookingModel>() {
            @Override
            public void onResponse(Call<BookingModel> call, Response<BookingModel> response) {
                Log.d("###", "onsuccess");
                if (response.body()!=null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        btn_bookservice.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showDialogmsg(toastmsg);



                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        btn_bookservice.setEnabled(true);
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                        btn_bookservice.setEnabled(true);
                    }

                }
                else
                {
                    uploading.dismiss();
                    btn_bookservice.setEnabled(true);
                    Toast.makeText(Bookservice.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void showDialogmsg(String toastmsg) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView msg =popupView.findViewById(R.id.msg);
        msg.setText(""+toastmsg);
        Button btn_yes = popupView.findViewById(R.id.btn_yes);

//        // create the popup window
//        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window tolken
//        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupWindow.dismiss();
//                Intent intent = new Intent(getApplicationContext(), Mainlastscreen.class);
//                startActivity(intent);
//                return true;
//            }
//        });
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(popupView);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Mainlastscreen.class);
                startActivity(intent);
                finish();
            }
        });

//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_LONG).show();
    }
}


//        Birthday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(Bookservice.this, dateone, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//            }
//        });
//        DatePickerDialog.OnDateSetListener dateone = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };
//        btn_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
////                TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
////                        new TimePickerDialog.OnTimeSetListener() {
////
////                            @Override
////                            public void onTimeSet(TimePicker view, int pHour,
////                                                  int pMinute) {
////
////                                mHour = pHour;
////                                mMinute = pMinute;
////                            }
////                        }, mHour, mMinute, true);
////
////                timePickerDialog.show();
//            }
//        });
//        btnPick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                show_Datepicker();
//            }
//        });
//
//    }
//
//    private void updateLabel() {
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        Birthday.setText(sdf.format(myCalendar.getTime()));
//
//    }
//
//    private void show_Datepicker() {
//
//        c = Calendar.getInstance();
//        int mYearParam = mYear;
//        int mMonthParam = mMonth-1;
//        int mDayParam = mDay;
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        mMonth = monthOfYear + 1;
//                        mYear=year;
//                        mDay=dayOfMonth;
//                    }
//                }, mYearParam, mMonthParam, mDayParam);
//
//        datePickerDialog.show();
//
//    }
//}
//
