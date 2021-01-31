package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class UserInfoModel {
    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("area")
    private String area;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("city")
    private String city;

    @SerializedName("model_id")
    private String model_id;

    @SerializedName("brand_id")
    private String brand_id;


    @SerializedName("brand_name")
    private String brand_name;


    @SerializedName("model_name")
    private String model_name;

    public String getBrand_name() {
        return brand_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }

    public String getModel_id() {
        return model_id;
    }

    public String getBrand_id() {
        return brand_id;
    }
}
