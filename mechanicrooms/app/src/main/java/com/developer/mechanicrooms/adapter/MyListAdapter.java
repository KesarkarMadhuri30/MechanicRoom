package com.developer.mechanicrooms.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.mechanicrooms.Garaagedetails;
import com.developer.mechanicrooms.Model.GarageListModel;
import com.developer.mechanicrooms.R;

import java.util.ArrayList;

import static com.developer.mechanicrooms.Uppercase.CovertFirstLetterInUppercase;
import static com.developer.mechanicrooms.Utils.MyActivity.pref;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    Context context;
    ArrayList<GarageListModel> mGarageList;
    String baseUrl;

    public MyListAdapter(Context context, ArrayList<GarageListModel> mGarageList,String BaseUrl) {
        this.context = context;
        this.mGarageList = mGarageList;
        this.baseUrl = BaseUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str_garage_name = CovertFirstLetterInUppercase(mGarageList.get(position).getGarage_name());
        holder.garage_name.setText(str_garage_name);

        String image_url = baseUrl + mGarageList.get(position).getShop_photo();
        Glide.with(context)
                .load(image_url)
                .error(R.drawable.mcroomlogo)
                .placeholder(R.drawable.mcroomlogo)
                .into(holder.shop_img);
        //String str_service = CovertFirstLetterInUppercase(mGarageList.get(position).getServices());
        //holder.garage_service.setText(str_service);

        if (mGarageList.get(position).getRating() != null) {
            holder.rating.setRating(Float.parseFloat(mGarageList.get(position).getRating()));

        }
        else
        {
            holder.rating.setRating((float) 0.0);
        }

//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(context,Garaagedetails.class);
//                v.getContext().startActivity(intent);
//
//            }
//        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(), "click on item: " + mGarageList.getDescription(), Toast.LENGTH_LONG).show();
                pref.edit().putString("vendor_id", mGarageList.get(position).getVendor_id()).commit();
                pref.edit().putString("vendor_phone", mGarageList.get(position).getPhone()).commit();
                Intent intent = new Intent(context,Garaagedetails.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGarageList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView shop_img;
        RatingBar rating;
        RelativeLayout relativeLayout;
        public TextView garage_name; //garage_service;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

            rating = (RatingBar) itemView.findViewById(R.id.rating);
           // garage_service =  itemView.findViewById(R.id.garage_service);
            garage_name = itemView.findViewById(R.id.garage_name);
            shop_img = itemView.findViewById(R.id.shop_img);
        }
    }
}
