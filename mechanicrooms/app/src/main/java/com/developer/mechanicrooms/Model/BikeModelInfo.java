package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class BikeModelInfo {

    @SerializedName("id")
    private String id;

    @SerializedName("model_name")
    private String model_name;

    public String getId() {
        return id;
    }

    public String getModel_name() {
        return model_name;
    }
}
