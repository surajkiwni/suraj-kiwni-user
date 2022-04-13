package com.kiwni.app.user.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleDatesRates
{
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("startRange")
    @Expose
    private Object startRange;
    @SerializedName("endRange")
    @Expose
    private Object endRange;
    @SerializedName("effectFrom")
    @Expose
    private String effectFrom;
    @SerializedName("effectTo")
    @Expose
    private String effectTo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Object getStartRange() {
        return startRange;
    }

    public void setStartRange(Object startRange) {
        this.startRange = startRange;
    }

    public Object getEndRange() {
        return endRange;
    }

    public void setEndRange(Object endRange) {
        this.endRange = endRange;
    }

    public String getEffectFrom() {
        return effectFrom;
    }

    public void setEffectFrom(String effectFrom) {
        this.effectFrom = effectFrom;
    }

    public String getEffectTo() {
        return effectTo;
    }

    public void setEffectTo(String effectTo) {
        this.effectTo = effectTo;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", serviceType='" + serviceType + '\'' +
                ", category='" + category + '\'' +
                ", rate=" + rate +
                ", startRange=" + startRange +
                ", endRange=" + endRange +
                ", effectFrom='" + effectFrom + '\'' +
                ", effectTo='" + effectTo + '\'' +
                '}' + "\n";
    }
}
