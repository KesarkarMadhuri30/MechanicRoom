package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

//    @SerializedName("customerInfo")
//    UserInfoModel customerInfo;

//    public UserInfoModel getCustomerInfo() {
//        return customerInfo;
//    }
//
//    public void setCustomerInfo(UserInfoModel customerInfo) {
//        this.customerInfo = customerInfo;
//    }

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
}
