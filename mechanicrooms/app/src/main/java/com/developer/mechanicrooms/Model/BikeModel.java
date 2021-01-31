package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BikeModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("brand_model")
    ArrayList<BikeModelInfo> bikemodelinfo;

    public boolean isStatus() {
        return status;
    }

    public String getBase_url() {
        return base_url;
    }

    public ArrayList<BikeModelInfo> getBikemodelinfo() {
        return bikemodelinfo;
    }
}
