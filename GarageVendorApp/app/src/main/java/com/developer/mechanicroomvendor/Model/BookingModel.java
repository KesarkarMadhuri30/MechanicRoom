package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BookingModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("booking")
    private ArrayList<AppointDataModel> bookingDataList;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public ArrayList<AppointDataModel> getBookingDataList() {
        return bookingDataList;
    }

    public String getBase_url() {
        return base_url;
    }

    public String getMessage() {
        return message;
    }
}
