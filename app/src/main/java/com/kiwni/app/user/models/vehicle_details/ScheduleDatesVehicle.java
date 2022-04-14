package com.kiwni.app.user.models.vehicle_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleDatesVehicle {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("kiwniVechicleId")
    @Expose
    private String kiwniVechicleId;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("capacity")
    @Expose
    private Integer capacity;
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("vehicleType")
    @Expose
    private Object vehicleType;
    @SerializedName("regNo")
    @Expose
    private String regNo;
    @SerializedName("baseLocation")
    @Expose
    private String baseLocation;
    @SerializedName("regYear")
    @Expose
    private String regYear;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("driver")
    @Expose
    private Object driver;
    @SerializedName("provider")
    @Expose
    private ScheduleDatesProvider provider;
    @SerializedName("rates")
    @Expose
    private List<ScheduleDatesRates> rates = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKiwniVechicleId() {
        return kiwniVechicleId;
    }

    public void setKiwniVechicleId(String kiwniVechicleId) {
        this.kiwniVechicleId = kiwniVechicleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Object getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Object vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    public String getRegYear() {
        return regYear;
    }

    public void setRegYear(String regYear) {
        this.regYear = regYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getDriver() {
        return driver;
    }

    public void setDriver(Object driver) {
        this.driver = driver;
    }

    public ScheduleDatesProvider getProvider() {
        return provider;
    }

    public void setProvider(ScheduleDatesProvider provider) {
        this.provider = provider;
    }

    public List<ScheduleDatesRates> getRates() {
        return rates;
    }

    public void setRates(List<ScheduleDatesRates> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", kiwniVechicleId='" + kiwniVechicleId + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", capacity=" + capacity +
                ", classType='" + classType + '\'' +
                ", vehicleType=" + vehicleType +
                ", regNo='" + regNo + '\'' +
                ", baseLocation='" + baseLocation + '\'' +
                ", regYear='" + regYear + '\'' +
                ", status='" + status + '\'' +
                ", driver=" + driver +
                ", provider=" + provider +
                ", rates=" + rates +
                '}' + "\n";
    }
}
