package com.kiwni.app.user.models.bookride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RideReq
{
    @SerializedName("createdTime")
    @Expose
    private String createdTime;
    @SerializedName("createdUser")
    @Expose
    private String createdUser;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("fromLocation")
    @Expose
    private String fromLocation;
    @SerializedName("journeyEndTime")
    @Expose
    private String journeyEndTime;
    @SerializedName("journeyTime")
    @Expose
    private String journeyTime;
    @SerializedName("rates")
    @Expose
    private List<RateReq> rates = null;
    @SerializedName("status")
    @Expose
    private RideStatusReq status;
    @SerializedName("toLocation")
    @Expose
    private String toLocation;
    @SerializedName("updatedTime")
    @Expose
    private String updatedTime;
    @SerializedName("updatedUser")
    @Expose
    private String updatedUser;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getJourneyEndTime() {
        return journeyEndTime;
    }

    public void setJourneyEndTime(String journeyEndTime) {
        this.journeyEndTime = journeyEndTime;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public List<RateReq> getRates() {
        return rates;
    }

    public void setRates(List<RateReq> rates) {
        this.rates = rates;
    }

    public RideStatusReq getStatus() {
        return status;
    }

    public void setStatus(RideStatusReq status) {
        this.status = status;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }
}
