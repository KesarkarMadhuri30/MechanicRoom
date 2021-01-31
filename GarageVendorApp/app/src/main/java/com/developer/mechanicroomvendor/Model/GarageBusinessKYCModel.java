package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class GarageBusinessKYCModel {
    @SerializedName("id")
    private String id;

    @SerializedName("shop_act")
    private String shop_act;

    @SerializedName("shop_photo")
    private String shop_photo;

    @SerializedName("gst_no")
    private String gst_no;

    @SerializedName("shop_act_document")
    private String shop_act_document;

    public String getShop_act_document() {
        return shop_act_document;
    }

    public String getId() {
        return id;
    }

    public String getShop_act() {
        return shop_act;
    }

    public String getShop_photo() {
        return shop_photo;
    }

    public String getGst_no() {
        return gst_no;
    }
}
