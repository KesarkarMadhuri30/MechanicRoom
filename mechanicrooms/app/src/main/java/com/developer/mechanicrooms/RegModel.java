package com.developer.mechanicrooms;

import com.google.gson.annotations.SerializedName;

public class RegModel {
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
