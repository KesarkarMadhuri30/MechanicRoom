package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class GarageListModel {

    @SerializedName("vendor_id")
    private String vendor_id;

    @SerializedName("garage_name")
    private String garage_name;


    @SerializedName("shop_photo")
    private String shop_photo;

    @SerializedName("services")
    private String services;

    @SerializedName("rating")
    private String rating;

    @SerializedName("phone")
    private String phone;

    public String getVendor_id() {
        return vendor_id;
    }

    public String getGarage_name() {
        return garage_name;
    }

    public String getServices() {
        return services;
    }

    public String getRating() {
        return rating;
    }

    public String getPhone() {
        return phone;
    }


    public String getShop_photo() {
        return shop_photo;
    }
}
