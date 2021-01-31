package com.developer.mechanicrooms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.mechanicrooms.Model.GarageGalleryModel;
import com.developer.mechanicrooms.R;

import java.util.ArrayList;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.ViewHolder> {
    Context context;
    ArrayList<GarageGalleryModel> garageGalleryList;
    String base_url;

    public GalleryViewAdapter(Context context, ArrayList<GarageGalleryModel> garageGalleryList, String base_url) {
        this.context = context;
        this.garageGalleryList = garageGalleryList;
        this.base_url = base_url;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image_url = base_url + garageGalleryList.get(position).getGarage_pic();
        Glide.with(context)
                .load(image_url)
                .error(R.drawable.mcroomlogo)
                .placeholder(R.drawable.mcroomlogo)
                .into(holder.highlight_img);
        holder.txt_highlight_name.setSelected(true);
        holder.txt_highlight_name.setText(garageGalleryList.get(position).getHighlight_name());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return garageGalleryList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView highlight_img;
        TextView txt_highlight_name;

        ViewHolder(View itemView) {
            super(itemView);
            highlight_img = itemView.findViewById(R.id.highlight_img);
            txt_highlight_name = itemView.findViewById(R.id.txt_highlight_name);
        }


    }


}