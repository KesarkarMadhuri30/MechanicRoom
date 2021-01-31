package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class CityInfoModel {
    @SerializedName("id")
    private String id;

    @SerializedName("city_name")
    private String city_name;

    @SerializedName("state_id")
    private String state_id;

    public String getId() {
        return id;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getState_id() {
        return state_id;
    }
}
