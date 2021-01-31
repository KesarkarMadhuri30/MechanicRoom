package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RatingModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("garageRating")
     ArrayList<GarageRatingModel> ratingList;

    @SerializedName("base_url")
    private String base_url;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<GarageRatingModel> getRatingList() {
        return ratingList;
    }

    public String getBase_url() {
        return base_url;
    }
}
