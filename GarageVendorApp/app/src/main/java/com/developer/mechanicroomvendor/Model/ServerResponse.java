package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("base_url")
    private String base_url;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBase_url() {
        return base_url;
    }
}
