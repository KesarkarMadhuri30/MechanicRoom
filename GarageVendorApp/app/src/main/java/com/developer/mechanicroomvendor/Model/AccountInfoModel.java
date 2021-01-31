package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class AccountInfoModel {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("vendor_id")
    private String vendor_id;


    @SerializedName("garage_name")
    private String garage_name;

    @SerializedName("contact_no")
    private String contact_no;

    @SerializedName("open_at")
    private String open_at;

    @SerializedName("close_at")
    private String close_at;

    @SerializedName("week_off")
    private String week_off;

    @SerializedName("address")
    private String address;

    @SerializedName("area")
    private String area;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("city")
    private String city;

    @SerializedName("pick_drop")
    private String pick_drop;

    @SerializedName("services")
    private String services;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public String getGarage_name() {
        return garage_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getOpen_at() {
        return open_at;
    }

    public String getClose_at() {
        return close_at;
    }

    public String getWeek_off() {
        return week_off;
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

    public String getPick_drop() {
        return pick_drop;
    }

    public String getServices() {
        return services;
    }
}
