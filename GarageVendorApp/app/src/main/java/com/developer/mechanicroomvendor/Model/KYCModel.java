package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class KYCModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("garageMerchantKYC")
    ArrayList<GarageKYCModel> garageMerchantKYC;

    @SerializedName("garageReviews")
    ArrayList<GarageBusinessKYCModel> garageBusinessKYC;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getBase_url() {
        return base_url;
    }

    public ArrayList<GarageKYCModel> getGarageMerchantKYC() {
        return garageMerchantKYC;
    }

    public ArrayList<GarageBusinessKYCModel> getGarageBusinessKYC() {
        return garageBusinessKYC;
    }
}
