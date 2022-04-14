package com.kiwni.app.user.models.bookride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideResp
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("journeyTime")
    @Expose
    private String journeyTime;
    @SerializedName("journeyEndTime")
    @Expose
    private String journeyEndTime;
    @SerializedName("fromLocation")
    @Expose
    private String fromLocation;
    @SerializedName("toLocation")
    @Expose
    private String toLocation;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("createdTime")
    @Expose
    private String createdTime;
    @SerializedName("updatedTime")
    @Expose
    private String updatedTime;
    @SerializedName("createdUser")
    @Expose
    private String createdUser;
    @SerializedName("updatedUser")
    @Expose
    private String updatedUser;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("rates")
    @Expose
    private Object rates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public String getJourneyEndTime() {
        return journeyEndTime;
    }

    public void setJourneyEndTime(String journeyEndTime) {
        this.journeyEndTime = journeyEndTime;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getRates() {
        return rates;
    }

    public void setRates(Object rates) {
        this.rates = rates;
    }

}
