package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GalleryModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("garageGallery")
    ArrayList<GarageGalleryModel> garageGalleryModel;

    @SerializedName("base_url")
    private String base_url;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<GarageGalleryModel> getGarageGalleryModel() {
        return garageGalleryModel;
    }

    public String getBase_url() {
        return base_url;
    }
}
