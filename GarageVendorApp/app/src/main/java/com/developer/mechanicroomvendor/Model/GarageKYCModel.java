package com.developer.mechanicroomvendor.Model;

import com.google.gson.annotations.SerializedName;

public class GarageKYCModel {
    @SerializedName("id")
    private String id;

    @SerializedName("aadhaar_card")
    private String aadhaar_card;

    @SerializedName("pan_card")
    private String pan_card;

    @SerializedName("vendor_profile")
    private String vendor_profile;

    @SerializedName("aadhaar_document")
    private String aadhaar_document;

    @SerializedName("pan_document")
    private String pan_document;

    public String getId() {
        return id;
    }

    public String getAadhaar_card() {
        return aadhaar_card;
    }

    public String getPan_card() {
        return pan_card;
    }

    public String getVendor_profile() {
        return vendor_profile;
    }

    public String getAadhaar_document() {
        return aadhaar_document;
    }

    public String getPan_document() {
        return pan_document;
    }
}
