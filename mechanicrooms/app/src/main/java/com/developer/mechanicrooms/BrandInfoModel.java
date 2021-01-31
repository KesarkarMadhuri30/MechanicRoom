package com.developer.mechanicrooms;

import com.google.gson.annotations.SerializedName;

public class BrandInfoModel {
    @SerializedName("id")
    private String id;

    @SerializedName("brand_name")
    private String brand_name;

    public String getId() {
        return id;
    }

    public String getBrand_name() {
        return brand_name;
    }
}
