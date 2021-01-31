package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class GarageRatingModel {

    @SerializedName("rating")
    private String rating;

    @SerializedName("vendor_id")
    private String vendor_id;

    public String getRating() {
        return rating;
    }

    public String getVendor_id() {
        return vendor_id;
    }
}
