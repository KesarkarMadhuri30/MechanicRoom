package com.developer.mechanicroomvendor.Drawer;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicroomvendor.Model.GarageRatingModel;
import com.developer.mechanicroomvendor.Model.RatingModel;
import com.developer.mechanicroomvendor.Model.ReviewDataModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Model.ReviewModel;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.util.ArrayList;

public class RateReviewsActivity extends MyActivity {
    RecyclerView reviews_recycler;
     ArrayList<ReviewModel> al_ReviewList=new ArrayList<ReviewModel>();
    //String bookmark_review ;
    AdapterReviews adapterReviews;

    LinearLayout rating_lyt;
    public static String BASE_URL="";
    RatingBar ratingbar;
    TextView error_msg;
    LinearLayout rating_review_lyt;
    String vendorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_reviews);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;


        rating_review_lyt = findViewById(R.id.rating_review_lyt);
        error_msg = findViewById(R.id.error_msg);

        reviews_recycler = findViewById(R.id.reviews_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reviews_recycler.setLayoutManager(layoutManager);
        reviews_recycler.setNestedScrollingEnabled(false);

        getReviewUrl();

        rating_lyt = findViewById(R.id.rating_lyt);
        ratingbar = findViewById(R.id.ratingbar);

        getRatingUrl();

       // ratingbar.setRating(Float.parseFloat("3.5"));
        }
        else {
            connected = false;
            Toast.makeText(RateReviewsActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getRatingUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        vendorID = pref.getString("vendor_id","");
        RetrofitApiClient retrofitApiClient = RetrofitClientInstance.getInstance().getApi();
        Call<RatingModel> ratingcall = retrofitApiClient.getGarageRating(vendorID);

        ratingcall.enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()== true) {
                        uploading.dismiss();
                        error_msg.setVisibility(View.GONE);
                        rating_lyt.setVisibility(View.VISIBLE);

                        Log.d("###", "onsuccess" + response.body());
                        ArrayList<GarageRatingModel> ratingList = response.body().getRatingList();
                        BASE_URL = response.body().getBase_url();
                        setRating(ratingList);
                    }
                    else  if (response.body().isStatus() == false)
                    {
                        uploading.dismiss();
                        error_msg.setVisibility(View.VISIBLE);
                        rating_lyt.setVisibility(View.GONE);
                    }
                    else
                    {
                        uploading.dismiss();
                        error_msg.setVisibility(View.VISIBLE);
                        rating_lyt.setVisibility(View.GONE);
                    }

                } else {
                    uploading.dismiss();
                    rating_lyt.setVisibility(View.GONE);
                    error_msg.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }

    private void setRating(ArrayList<GarageRatingModel> ratingList) {
        if (ratingList.get(0).getRating() != null) {
            ratingbar.setRating(Float.parseFloat(ratingList.get(0).getRating()));

        }
        else
        {
            ratingbar.setRating((float) 0.0);
        }
    }

    private void getReviewUrl() {
      /*  al_ReviewList.clear();
        al_ReviewList.add(new ReviewModel("Madhuri kesarkar","Good service"));
        al_ReviewList.add(new ReviewModel("Satish"," Nice Service"));
        al_ReviewList.add(new ReviewModel("Sushant","This is one of the best options available for car servicing"));
        al_ReviewList.add(new ReviewModel("Supriya","Amazing service. "));
        al_ReviewList.add(new ReviewModel("Sanket"," Good Service.Quality of work done is top notch."));
        al_ReviewList.add(new ReviewModel("Pranita","Good facilities available here. The staff is friendly and helpful."));
        al_ReviewList.add(new ReviewModel("Shalini","Excellent service, qualified technicians and professional Customer Care."));

         */

        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        vendorID = pref.getString("vendor_id","");
        RetrofitApiClient retrofitApiClient = RetrofitClientInstance.getInstance().getApi();
        Call<ReviewModel> revicewcall = retrofitApiClient.getGarageReviews(vendorID);

        revicewcall.enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                    if (response.body() != null) {
                        if (response.body().isStatus()== true) {
                            reviews_recycler.setVisibility(View.VISIBLE);
                            uploading.dismiss();
                            error_msg.setVisibility(View.GONE);

                            Log.d("###", "onsuccess" + response.body());
                            ArrayList<ReviewDataModel> allReviewList = response.body().getReviewDataList();
                            BASE_URL = response.body().getBase_url();
                            generateReviewList(allReviewList);
                        }
                        else  if (response.body().isStatus() == false)
                        {
                            uploading.dismiss();
                            reviews_recycler.setVisibility(View.GONE);
                            error_msg.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            uploading.dismiss();
                            reviews_recycler.setVisibility(View.GONE);
                            error_msg.setVisibility(View.VISIBLE);
                        }

                } else {
                    uploading.dismiss();
                    reviews_recycler.setVisibility(View.GONE);
                    error_msg.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
                reviews_recycler.setVisibility(View.GONE);
                uploading.dismiss();
            }
    });
    }

    private void generateReviewList(ArrayList<ReviewDataModel> allReviewList) {
        adapterReviews = new AdapterReviews(getApplicationContext(),allReviewList);
        reviews_recycler.setAdapter(adapterReviews);
    }

    private class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ViewHolder> {

        Context context;
        ArrayList<ReviewDataModel> mReviewList;

        public AdapterReviews(Context context, ArrayList<ReviewDataModel> mReviewList) {
            this.context = context;
            this.mReviewList = mReviewList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.review_listitem, parent, false);
            return new ViewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            holder.txt_username.setText(""+mReviewList.get(i).getName());
            holder.txt_reviews.setText(""+mReviewList.get(i).getReviews());
        }

        @Override
        public int getItemCount() {
            return mReviewList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_username,txt_reviews;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_username = itemView.findViewById(R.id.txt_username);
                txt_reviews = itemView.findViewById(R.id.txt_reviews);
            }
        }
    }
}