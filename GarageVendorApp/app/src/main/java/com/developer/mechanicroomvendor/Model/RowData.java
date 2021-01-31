package com.developer.mechanicroomvendor.Model;

public class RowData {

    String service_name,service_price;
    Boolean b;

    public RowData(String service_name, String service_price, Boolean b) {
        this.service_name = service_name;
        this.service_price = service_price;
        this.b = b;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public Boolean getB() {
        return b;
    }

    public void setB(Boolean b) {
        this.b = b;
    }
}
