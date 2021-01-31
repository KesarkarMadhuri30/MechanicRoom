package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class GarageUpdateModel {
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
