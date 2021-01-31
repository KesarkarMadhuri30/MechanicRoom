package com.developer.mechanicroomvendor.Model;

public class VendorModel {

    String name;
    int listSize;

    public VendorModel(String name, int listSize) {
        this.name = name;
        this.listSize = listSize;
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
