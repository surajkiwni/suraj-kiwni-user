package com.kiwni.app.user.models.vehicle_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleDates
{
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("startLocation")
    @Expose
    private String startLocation;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("matchExactTime")
    @Expose
    private Boolean matchExactTime;

    public ScheduleDates(String startTime, String endTime, String startLocation,
                         String direction, String serviceType, String classType, String vehicleType,
                         Double distance, boolean matchExactTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.direction = direction;
        this.serviceType = serviceType;
        this.classType = classType;
        this.vehicleType = vehicleType;
        this.distance = distance;
        this.matchExactTime = matchExactTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getMatchExactTime() {
        return matchExactTime;
    }

    public void setMatchExactTime(Boolean matchExactTime) {
        this.matchExactTime = matchExactTime;
    }
}
