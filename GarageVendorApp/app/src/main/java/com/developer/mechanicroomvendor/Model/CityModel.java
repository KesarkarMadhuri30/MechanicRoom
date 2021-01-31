package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CityModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("city")
    ArrayList<CityInfoModel> cityinfo;

    public boolean isStatus() {
        return status;
    }

    public String getBase_url() {
        return base_url;
    }

    public ArrayList<CityInfoModel> getCityinfo() {
        return cityinfo;
    }
}
