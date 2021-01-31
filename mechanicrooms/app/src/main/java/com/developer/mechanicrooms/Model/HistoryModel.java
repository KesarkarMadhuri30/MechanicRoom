package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HistoryModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("history")
    private ArrayList<HistoryDataModel> historyDataList;

    @SerializedName("base_url")
    private String base_url;

    @SerializedName("message")
    private String message;

    public ArrayList<HistoryDataModel> getHistoryDataList() {
        return historyDataList;
    }

    public void setHistoryDataList(ArrayList<HistoryDataModel> historyDataList) {
        this.historyDataList = historyDataList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
