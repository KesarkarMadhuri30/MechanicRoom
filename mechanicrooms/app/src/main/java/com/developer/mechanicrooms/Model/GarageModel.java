package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GarageModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("garages")
    ArrayList<GarageListModel> garageList;

    @SerializedName("base_url")
    private String base_url;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<GarageListModel> getGarageList() {
        return garageList;
    }

    public String getBase_url() {
        return base_url;
    }
}
