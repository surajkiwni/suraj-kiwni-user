package com.kiwni.app.user.models.vehicle_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleMapResp
{
    public static final int LayoutOne = 0;
    public static final int LayoutTwo = 1;
    private int viewType;
    int cnt;
    boolean isExpandable = false;
    List<ScheduleMapResp> nestedList;

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("availableLocation")
    @Expose
    private String availableLocation;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("driverId")
    @Expose
    private Integer driverId;
    @SerializedName("driverName")
    @Expose
    private Object driverName;
    @SerializedName("driverMobileNo")
    @Expose
    private Object driverMobileNo;
    @SerializedName("vehicleId")
    @Expose
    private Integer vehicleId;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    private int totalAvailable;
    @SerializedName("vehicle")
    @Expose
    private ScheduleDatesVehicle vehicle;
    @SerializedName("price")
    @Expose
    private Double price;

    boolean displayHeader = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getAvailableLocation() {
        return availableLocation;
    }

    public void setAvailableLocation(String availableLocation) {
        this.availableLocation = availableLocation;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Object getDriverName() {
        return driverName;
    }

    public void setDriverName(Object driverName) {
        this.driverName = driverName;
    }

    public Object getDriverMobileNo() {
        return driverMobileNo;
    }

    public void setDriverMobileNo(Object driverMobileNo) {
        this.driverMobileNo = driverMobileNo;
    }


    public ScheduleDatesVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ScheduleDatesVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getViewType() { return viewType; }

    public void setViewType(int viewType)
    {
        this.viewType = viewType;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public boolean getDisplayHeader() {
        return displayHeader;
    }

    public void setDisplayHeader(boolean displayHeader) {
        this.displayHeader = displayHeader;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<ScheduleMapResp> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<ScheduleMapResp> nestedList) {
        this.nestedList = nestedList;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "{" +
                "cnt=" + cnt +
                ", id=" + id +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", availableLocation='" + availableLocation + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", status='" + status + '\'' +
                ", driverId=" + driverId +
                ", driverName=" + driverName +
                ", driverMobileNo=" + driverMobileNo +
                ", vehicleId=" + vehicleId +
                ", model='" + model + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", classType='" + classType + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", totalAvailable=" + totalAvailable +
                ", vehicle=" + vehicle +
                ", price=" + price +
                '}';
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

   /* public static ScheduleDatesResp fromJson(JsonElement jsonElement)
    {
        return new Gson().fromJson(jsonElement, ScheduleDatesResp.class);
    }*/
}
