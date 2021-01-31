package com.developer.mechanicrooms;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BrandModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("brand")
    ArrayList<BrandInfoModel> brandinfo;

    public boolean isStatus() {
        return status;
    }

    public String getBase_url() {
        return base_url;
    }

    public ArrayList<BrandInfoModel> getBrandinfo() {
        return brandinfo;
    }
}
