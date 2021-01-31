package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GarageModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("garageInfo")
    ArrayList<AccountInfoModel> accountInfoModel;

    @SerializedName("base_url")
    private String base_url;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getBase_url() {
        return base_url;
    }

    public ArrayList<AccountInfoModel> getAccountInfoModel() {
        return accountInfoModel;
    }
}
