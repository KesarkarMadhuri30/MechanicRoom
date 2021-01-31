package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("garageReviews")
    private ArrayList<ReviewDataModel> reviewDataList;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getBase_url() {
        return base_url;
    }

    public ArrayList<ReviewDataModel> getReviewDataList() {
        return reviewDataList;
    }
}
