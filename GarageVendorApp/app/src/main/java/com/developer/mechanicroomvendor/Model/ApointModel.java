package com.developer.mechanicroomvendor.Model;

public class ApointModel {

    String username,vehicle_type,service_type,date,time;

    public ApointModel(String username, String vehicle_type, String service_type, String date, String time) {
        this.username = username;
        this.vehicle_type = vehicle_type;
        this.service_type = service_type;
        this.date = date;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getService_type() {
        return service_type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
