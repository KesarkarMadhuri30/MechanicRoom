package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class GarageGalleryModel {
    @SerializedName("id")
    private String id;

    @SerializedName("highlight_name")
    private String highlight_name;

    @SerializedName("garage_pic")
    private String garage_pic;

    public String getId() {
        return id;
    }

    public String getHighlight_name() {
        return highlight_name;
    }

    public String getGarage_pic() {
        return garage_pic;
    }
}
