package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class BookingModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
