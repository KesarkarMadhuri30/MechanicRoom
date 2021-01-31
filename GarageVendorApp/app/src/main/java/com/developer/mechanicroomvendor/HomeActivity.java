package com.developer.mechanicroomvendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicroomvendor.AccountInfo.MyAccountActivity;
import com.developer.mechanicroomvendor.Adapter.AdapterDrawer;
import com.developer.mechanicroomvendor.Adapter.Adver_banner_PagerAdapter;
import com.developer.mechanicroomvendor.Drawer.AboutUsActivity;
import com.developer.mechanicroomvendor.Drawer.ContactUsActivity;
import com.developer.mechanicroomvendor.Drawer.PrivacyPolicy;
import com.developer.mechanicroomvendor.Drawer.RateReviewsActivity;
import com.developer.mechanicroomvendor.Model.AppointDataModel;
import com.developer.mechanicroomvendor.Model.BannerModel;
import com.developer.mechanicroomvendor.Model.BookingModel;
import com.developer.mechanicroomvendor.Model.CompleteModel;
import com.developer.mechanicroomvendor.Model.PendingModel;
import com.developer.mechanicroomvendor.Model.RejectModel;
import com.developer.mechanicroomvendor.Model.ScheduleModel;
import com.developer.mechanicroomvendor.Model.VendorModel;
import com.developer.mechanicroomvendor.Model.WorkingModel;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.StartPages.RegisterActivity;
import com.developer.mechanicroomvendor.Utils.MyActivity;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends MyActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

//    private BubbleNavigationConstraintView bubbleNavigationLinearView;
//    private FragmentManager fragmentManager;

    DrawerLayout drawer_layout;
    ImageView imgv_drawer;
    AdapterDrawer adapterDrawer;
    ListView left_drawer;

    RecyclerView home_recyclerview;
    ViewPager viewPager;

    ArrayList<BannerModel> bannerData = new ArrayList<BannerModel>();
    int currentPage, NUM_PAGES;
    Adver_banner_PagerAdapter adverbannerPagerAdapter;

    TabLayout mTabLayout;
    ArrayList<AppointDataModel> appointDataList = new ArrayList<>();
    ArrayList<PendingModel> mPendingList=new ArrayList<>();
    ArrayList<WorkingModel> mWorkingList=new ArrayList<>();
    ArrayList<ScheduleModel> mScheduleList=new ArrayList<>();
    ArrayList<RejectModel> mRejectList=new ArrayList<>();
    ArrayList<CompleteModel> mCompleteList=new ArrayList<>();
    ArrayList<VendorModel> mVendorList= new ArrayList<VendorModel>();
    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        imgv_drawer = findViewById(R.id.imgv_drawer);
        imgv_drawer.setOnClickListener(this);

        adapterDrawer = new AdapterDrawer(this, getResources().getStringArray(R.array.menus));
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        left_drawer.setOnItemClickListener(this);
        left_drawer.setAdapter(adapterDrawer);

        viewPager = findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabDots);
        mTabLayout.setupWithViewPager(viewPager, true);
        GetImageforbaners();

        home_recyclerview = findViewById(R.id.home_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      //  GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        home_recyclerview.setLayoutManager(layoutManager);
        home_recyclerview.setNestedScrollingEnabled(false);
       // getVendorUrl();
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
        getApointmentCountUrl();
        }
        else {
            connected = false;
            Toast.makeText(HomeActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
//        fragmentManager = getSupportFragmentManager();
//        bubbleNavigationLinearView = findViewById(R.id.top_navigation_constraint);
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bubbleNavigationLinearView.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
//
//        if (bubbleNavigationLinearView != null) {
//            selectFragmentStyle(0);
//
//            bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
//                @Override
//                public void onNavigationChanged(View view, int position) {
////                viewPager.setCurrentItem(position, true);
//                    selectFragmentStyle(position);
//                }
//            });
//        }
    }
    private void getApointmentCountUrl()
    {
        appointDataList.clear();
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

                    appointDataList=response.body().getBookingDataList();

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
            public void onFailure(Call<BookingModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

        if (appointDataList !=null) {
            getAppointDataUrl(appointDataList);
        }
    }

    private void getAppointDataUrl(ArrayList<AppointDataModel> appointDataList) {
        mPendingList.clear();
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
                if (strDate.before(todayDate) || todayDate.equals(strDate) ) {
                    if (appointDataList.get(i).getBooking_status().equals("0")) {

                        PendingModel appointData = new PendingModel(appointDataList.get(i).getCustomer_id());
                        mPendingList.add(appointData);

                    } else {
                        //Toast.makeText(this, "not before date ", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mWorkingList.clear();
        for (int i=0;i<appointDataList.size();i++) {
            if (appointDataList.get(i).getBooking_status().equals("1")) {
                WorkingModel appointData = new WorkingModel(appointDataList.get(i).getCustomer_id());
                mWorkingList.add(appointData);

            }
            else
            {

            }

        }

        mScheduleList.clear();
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

                        ScheduleModel appointData = new ScheduleModel(appointDataList.get(i).getCustomer_id());
                        mScheduleList.add(appointData);

                    } else {

                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        mRejectList.clear();
        for (int i=0;i<appointDataList.size();i++) {

            if (appointDataList.get(i).getBooking_status().equals("2")) {

                RejectModel appointData = new RejectModel(appointDataList.get(i).getCustomer_id());
                mRejectList.add(appointData);

            }
            else
            {

            }
        }

        mCompleteList.clear();
        for (int i=0;i<appointDataList.size();i++) {
            if (appointDataList.get(i).getBooking_status().equals("4")) {
                CompleteModel appointData = new CompleteModel(appointDataList.get(i).getCustomer_id());
                mCompleteList.add(appointData);

            }
            else
            {
            }
        }

//        Toast.makeText(this, "Pending "+mPendingList.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Working "+mWorkingList.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Scheduled "+mScheduleList.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Rejected "+mRejectList.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Completed "+mCompleteList.size(), Toast.LENGTH_SHORT).show();
//
        getVendorUrl(mPendingList.size(),mWorkingList.size(),mScheduleList.size(),mRejectList.size(),mCompleteList.size());
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    private void getVendorUrl(int pending_size, int working_size, int schedule_size, int reject_size, int complete_size) {
        mVendorList.clear();

        mVendorList.add(new VendorModel("Pending Appointments",pending_size));
        mVendorList.add(new VendorModel("Working Appointments",working_size));
        mVendorList.add(new VendorModel("Scheduled Appointments",schedule_size));
        mVendorList.add(new VendorModel("Rejected Appointments",reject_size));
        mVendorList.add(new VendorModel("Completed Appointments",complete_size));
        homeAdapter = new HomeAdapter(getApplicationContext(),mVendorList);
        home_recyclerview.setAdapter(homeAdapter);
    }

    private void GetImageforbaners() {

        bannerData.add(new BannerModel("1",R.drawable.garage_shop));
        bannerData.add(new BannerModel("2",R.drawable.shop2));
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
    public void onBackPressed() {
//
        String alertmsg = "Do you really want to exit ?";
        Alertdialog(alertmsg);
        // super.onBackPressed();
    }

    private void Alertdialog(String alertmsg) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.main_alert_dialog, null);
        final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
        final Button btn_yes = (Button) alertLayout.findViewById(R.id.btn_yes);
        final Button btn_no = (Button) alertLayout.findViewById(R.id.btn_no);

        txt_dialog.setText(""+alertmsg);

        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);


        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_drawer:
                if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                    drawer_layout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer_layout.openDrawer(Gravity.RIGHT);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.left_drawer) {
            if (i == 0) {
                Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(intent);
            }
            else if (i == 1) {

                Intent intent = new Intent(getApplicationContext(), RateReviewsActivity.class);
                startActivity(intent);

            }else if (i == 2) {
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);

            }
            else if (i == 3) {
                Intent intent1 = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(intent1);

            }else if (i == 4) {
                Intent intent1 = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent1);

            } else if (i == 5) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Thank you for sharing App with your Friends and Family! Click on below link to download app\n" + "\n";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
                // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Your friend shared: Playstore APP Link ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + getResources().getString(R.string.share_link));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
            else if (i == 6) {
                String alertmsg = "Do you really want to Logout?";
                LogoutAlertdialog(alertmsg);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "App is under construction...Work in progress.", Toast.LENGTH_SHORT).show();
            }
        }
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
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();

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

    private class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

        Context context;
        ArrayList<VendorModel> allHomeList;

        public HomeAdapter(Context context, ArrayList<VendorModel> allHomeList) {
            this.context = context;
            this.allHomeList = allHomeList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.home_listitem, parent, false);
            return new ViewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.txtvname.setText(""+allHomeList.get(position).getName());
            holder.count.setText(""+allHomeList.get(position).getListSize());

            holder.lin_apoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id= position;
                    if (id == 0) {
                        Intent intent = new Intent(view.getContext(), TodaysAppointListActivity.class);
                        startActivity(intent);
                    }else if (id == 1) {
                        Intent intent = new Intent(view.getContext(), WorkingAppointActivity.class);
                        startActivity(intent);
                    }else if (id == 2) {
                        Intent intent = new Intent(view.getContext(), ScheduleAppointActivity.class);
                        startActivity(intent);
                    }else if (id == 3) {
                        Intent intent = new Intent(view.getContext(),RejectAppointActivity.class);
                        startActivity(intent);
                    }else if (id ==4 ) {
                        Intent intent = new Intent(view.getContext(),CompletedAppointActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return allHomeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtvname;
            TextView count;
            LinearLayout lin_apoint;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txtvname = itemView.findViewById(R.id.txtv_name);
                count = itemView.findViewById(R.id.apoint_count);
                lin_apoint = itemView.findViewById(R.id.lin_apoint);
            }
        }
    }

    //    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
//        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//            fm.popBackStack();
//        }
//        FragmentTransaction ft = fm.beginTransaction();
//        //  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.replace(R.id.Container, f1, name);
//        ft.commit();
//        //setToolbarTitle(name);
//    }
//
//    protected void selectFragmentStyle(int item) {
//
//        bubbleNavigationLinearView.setCurrentActiveItem(item);
//
//        switch (item) {
//            case 0:
//                visible_bottomNavigation();
//                HomeFragment homeFragment = new HomeFragment();
//                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
//
//                break;
//            case 1:
//
//                HelpFragment helpFragment = new HelpFragment();
//                loadFrag(helpFragment, getString(R.string.menu_help), fragmentManager);
//                break;
//            case 2:
//                HistoryFragment historyFragment = new HistoryFragment();
//                loadFrag(historyFragment, getString(R.string.menu_history), fragmentManager);
//                break;
//
//            case 3:
//                AccountFragment accountFragment = new AccountFragment();
//                loadFrag(accountFragment, getString(R.string.menu_profile), fragmentManager);
//
//                break;
//
//        }
//    }
//
//    public void visible_bottomNavigation() {
//        bubbleNavigationLinearView.setVisibility(View.VISIBLE);
//    }
}