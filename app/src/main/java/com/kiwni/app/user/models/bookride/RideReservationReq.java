package com.kiwni.app.user.models.bookride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideReservationReq
{

    @SerializedName("channel")
    @Expose
    private ChannelReq channel;
    @SerializedName("createdTime")
    @Expose
    private String createdTime;
    @SerializedName("createdUser")
    @Expose
    private String createdUser;
    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("driverId")
    @Expose
    private Integer driverId;
    @SerializedName("driverLicense")
    @Expose
    private String driverLicense;
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("driverPhone")
    @Expose
    private String driverPhone;
    @SerializedName("providerId")
    @Expose
    private Integer providerId;
    @SerializedName("providerName")
    @Expose
    private String providerName;
    @SerializedName("reservationTime")
    @Expose
    private String reservationTime;
    @SerializedName("ride")
    @Expose
    private RideReq ride;
    @SerializedName("scheduleId")
    @Expose
    private Long scheduleId;
    @SerializedName("serviceType")
    @Expose
    private ServiceTypeReq serviceType;
    @SerializedName("status")
    @Expose
    private StatusReq status;
    @SerializedName("updatedTime")
    @Expose
    private String updatedTime;
    @SerializedName("updatedUser")
    @Expose
    private String updatedUser;
    @SerializedName("vehicleId")
    @Expose
    private Integer vehicleId;
    @SerializedName("vehicleNo")
    @Expose
    private String vehicleNo;
    @SerializedName("estimatedPrice")
    @Expose
    private Double estimatedPrice;
    @SerializedName("notificationType")
    @Expose
    private String notificationType;
    @SerializedName("tripType")
    @Expose
    private String tripType;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("companyEmail")
    @Expose
    private String companyEmail;
    @SerializedName("companyPhone")
    @Expose
    private String companyPhone;

    public RideReservationReq(ChannelReq channel, String createdTime, String createdUser,
                              String customerEmail, Integer customerId, String customerName,
                              String customerPhone, Integer driverId, String driverLicense,
                              String driverName, String driverPhone, Integer providerId,
                              String providerName, String reservationTime, RideReq ride,
                              Long scheduleId, ServiceTypeReq serviceType, StatusReq status,
                              String updatedTime, String updatedUser, Integer vehicleId,
                              String vehicleNo, Double estimatedPrice, String notificationType,
                              String tripType, String companyName, String companyEmail,
                              String companyPhone)
    {
        this.channel = channel;
        this.createdTime = createdTime;
        this.createdUser = createdUser;
        this.customerEmail = customerEmail;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.driverId = driverId;
        this.driverLicense = driverLicense;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.providerId = providerId;
        this.providerName = providerName;
        this.reservationTime = reservationTime;
        this.ride = ride;
        this.scheduleId = scheduleId;
        this.serviceType = serviceType;
        this.status = status;
        this.updatedTime = updatedTime;
        this.updatedUser = updatedUser;
        this.vehicleId = vehicleId;
        this.vehicleNo = vehicleNo;
        this.estimatedPrice = estimatedPrice;
        this.notificationType = notificationType;
        this.tripType = tripType;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
    }

    public ChannelReq getChannel() {
        return channel;
    }

    public void setChannel(ChannelReq channel) {
        this.channel = channel;
    }

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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public RideReq getRide() {
        return ride;
    }

    public void setRide(RideReq ride) {
        this.ride = ride;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ServiceTypeReq getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeReq serviceType) {
        this.serviceType = serviceType;
    }

    public StatusReq getStatus() {
        return status;
    }

    public void setStatus(StatusReq status) {
        this.status = status;
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

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    @Override
    public String toString() {
        return "{" +
                "channel=" + channel +
                ", createdTime='" + createdTime + '\'' +
                ", createdUser='" + createdUser + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", driverId=" + driverId +
                ", driverLicense='" + driverLicense + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", providerId=" + providerId +
                ", providerName='" + providerName + '\'' +
                ", reservationTime='" + reservationTime + '\'' +
                ", ride=" + ride +
                ", scheduleId=" + scheduleId +
                ", serviceType=" + serviceType +
                ", status=" + status +
                ", updatedTime='" + updatedTime + '\'' +
                ", updatedUser='" + updatedUser + '\'' +
                ", vehicleId=" + vehicleId +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", estimatedPrice=" + estimatedPrice +
                ", notificationType='" + notificationType + '\'' +
                ", tripType='" + tripType + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                '}';
    }
}