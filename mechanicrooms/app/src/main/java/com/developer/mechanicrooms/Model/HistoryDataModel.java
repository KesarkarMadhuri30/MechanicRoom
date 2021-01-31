package com.developer.mechanicrooms.Model;

import com.google.gson.annotations.SerializedName;

public class HistoryDataModel {

    @SerializedName("booking_no")
    private String booking_no;

    @SerializedName("garage_name")
    private String garage_name;

    @SerializedName("garage_phone")
    private String garage_phone;

    @SerializedName("vehical_type")
    private String vehical_type;

    @SerializedName("service_requirement")
    private String service_requirement;

    @SerializedName("service_payment")
    private String service_payment;

    @SerializedName("payment_status")
    private String payment_status;

    @SerializedName("pick_date")
    private String pick_date;

    @SerializedName("pick_time")
    private String pick_time;

    @SerializedName("pick_up_drop")
    private String pick_up_drop;

    @SerializedName("additional_requirements")
    private String additional_requirements;

    @SerializedName("booking_status")
    private String booking_status;

    @SerializedName("rejection_reason")
    private String rejection_reason;

    @SerializedName("cancellation_reason")
    private String cancellation_reason;


    public HistoryDataModel(String booking_no, String garage_name, String garage_phone, String vehical_type,
                            String service_requirement, String service_payment, String payment_status, String pick_date,
                            String pick_time, String pick_up_drop, String additional_requirements, String booking_status,
                            String rejection_reason, String cancellation_reason) {
        this.booking_no = booking_no;
        this.garage_name = garage_name;
        this.garage_phone = garage_phone;
        this.vehical_type = vehical_type;
        this.service_requirement = service_requirement;
        this.service_payment = service_payment;
        this.payment_status = payment_status;
        this.pick_date = pick_date;
        this.pick_time = pick_time;
        this.pick_up_drop = pick_up_drop;
        this.additional_requirements = additional_requirements;
        this.booking_status = booking_status;
        this.rejection_reason = rejection_reason;
        this.cancellation_reason = cancellation_reason;
    }

    public String getBooking_no() {
        return booking_no;
    }

    public String getGarage_name() {
        return garage_name;
    }

    public String getGarage_phone() {
        return garage_phone;
    }

    public String getVehical_type() {
        return vehical_type;
    }

    public String getService_requirement() {
        return service_requirement;
    }

    public String getService_payment() {
        return service_payment;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getPick_date() {
        return pick_date;
    }

    public String getPick_time() {
        return pick_time;
    }

    public String getPick_up_drop() {
        return pick_up_drop;
    }

    public String getAdditional_requirements() {
        return additional_requirements;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public String getRejection_reason() {
        return rejection_reason;
    }

    public String getCancellation_reason() {
        return cancellation_reason;
    }
}
