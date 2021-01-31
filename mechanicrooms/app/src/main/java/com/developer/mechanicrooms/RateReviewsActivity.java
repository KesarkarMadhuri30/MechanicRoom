package com.developer.mechanicrooms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mechanicrooms.Model.GarageRatingModel;
import com.developer.mechanicrooms.Model.RatingModel;
import com.developer.mechanicrooms.Model.ReviewDataModel;
import com.developer.mechanicrooms.Model.ReviewModel;
import com.developer.mechanicrooms.Retrofit.RetrofitApiClient;
import com.developer.mechanicrooms.Retrofit.RetrofitClientInstance;
import com.developer.mechanicrooms.Utils.MyActivity;

import java.util.ArrayList;

public class RateReviewsActivity extends MyActivity implements View.OnClickListener {
    LinearLayout rating_lyt;
    AlertDialog dialog;
    RecyclerView reviews_recycler;
    TextView error_msg;
    String vendorID,customerID;
    String BASE_URL;
    AdapterReviews adapterReviews;
    RatingBar garage_rate;
    Button submitRating,review_postbtn;
    EditText comment_edttxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_reviews);

        garage_rate = findViewById(R.id.garage_rate);
        rating_lyt = findViewById(R.id.rating_lyt);
        rating_lyt.setOnClickListener(this);

        error_msg = findViewById(R.id.error_msg);

        reviews_recycler = findViewById(R.id.reviews_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reviews_recycler.setLayoutManager(layoutManager);
        reviews_recycler.setNestedScrollingEnabled(false);

        getReviewUrl();
        getRatingUrl();

        comment_edttxt = findViewById(R.id.comment_edttxt);
        review_postbtn = findViewById(R.id.review_postbtn);
        review_postbtn.setOnClickListener(this);
    }


    private void getRatingUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        vendorID = pref.getString("vendor_id","");
        Log.d("loc=","vendor_id"+vendorID);
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
                       // rating_lyt.setVisibility(View.GONE);
                    }
                    else
                    {
                        uploading.dismiss();
                        error_msg.setVisibility(View.VISIBLE);
                       // rating_lyt.setVisibility(View.GONE);
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
            garage_rate.setRating(Float.parseFloat(ratingList.get(0).getRating()));

        }
        else
        {
            garage_rate.setRating((float) 0.0);
        }
    }

    private void getReviewUrl() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        vendorID = pref.getString("vendor_id","");
       // Log.d("loc=","vendor_id"+vendorID);
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rating_lyt:
                RatingDialog();
                break;
            case R.id.review_postbtn:

                if (comment_edttxt.getText().toString().isEmpty())
                {
                    comment_edttxt.setError("Enter reviews");
                    comment_edttxt.requestFocus();
                }
                else
                {
                    String cust_reviews = comment_edttxt.getText().toString();

                    submitReviews(cust_reviews);
                    review_postbtn.setEnabled(false);
                }
                break;
        }
    }

    private void submitReviews( String cust_reviews) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        vendorID = pref.getString("vendor_id","");
        customerID = pref.getString("customer_id","");

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ReviewModel> call = service.postReviewsUser(customerID,vendorID,cust_reviews);

        call.enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
             if(response.body()!=null) {
                 if (response.body().isStatus()) {
                     review_postbtn.setEnabled(true);
                     uploading.dismiss();

                     comment_edttxt.setText("");
                     String toastmsg = response.body().getMessage();
                     Toast.makeText(getApplicationContext(), "" + toastmsg, Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
//                    startActivity(i);
                     getReviewUrl();
                 } else {
                     String toastmsg = response.body().getMessage();
                     showToastmsg(toastmsg);
                     uploading.dismiss();
                 }
             }
             else {
                 uploading.dismiss();
             }
            }

            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                review_postbtn.setEnabled(true);
                uploading.dismiss();
            }
        });

    }

    private void RatingDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_layout, null);

         submitRating = alertLayout.findViewById(R.id.submitRating);
        final RatingBar garage_rating = alertLayout.findViewById(R.id.garage_ratingbar);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setCancelable(false);
        alert.setView(alertLayout);

        dialog = alert.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.setCancelable(true);

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    dialog.dismiss();
              
                String rating = "" + garage_rating.getRating();
                submitRating(rating);
                submitRating.setEnabled(false);
            }
        });
        dialog.show();
    }

    private void submitRating(String rating) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        vendorID = pref.getString("vendor_id","");
        customerID = pref.getString("customer_id","");
        Log.d("loc=","customerID"+customerID);
        Log.d("loc=","vendorID"+vendorID);
        Log.d("loc=","rating"+rating);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<RatingModel> call = service.postRatingUser(customerID,rating,vendorID);

        call.enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                Log.d("###", "onsuccess"+response.body());
                if (response.body().getMessage().equals("Rating Successful."))
                {
                    submitRating.setEnabled(true);
                    dialog.dismiss();
                    uploading.dismiss();
                    getRatingUrl();
                    String toastmsg = response.body().getMessage();
                    Toast.makeText(getApplicationContext(), ""+toastmsg, Toast.LENGTH_SHORT).show();

                }
                else
                {
                    submitRating.setEnabled(true);
                    uploading.dismiss();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {
                submitRating.setEnabled(true);
                uploading.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
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