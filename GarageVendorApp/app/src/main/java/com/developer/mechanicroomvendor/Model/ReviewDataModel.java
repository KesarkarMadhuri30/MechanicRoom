package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class ReviewDataModel {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("reviews")
    private String reviews;


    @SerializedName("customer_id")
    private String customer_id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReviews() {
        return reviews;
    }

    public String getCustomer_id() {
        return customer_id;
    }
}
