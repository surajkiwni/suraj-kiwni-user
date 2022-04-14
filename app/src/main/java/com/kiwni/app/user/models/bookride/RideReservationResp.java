package com.kiwni.app.user.models.bookride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideReservationResp {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("scheduleId")
    @Expose
    private Long scheduleId;
    @SerializedName("reservationTime")
    @Expose
    private String reservationTime;
    @SerializedName("providerId")
    @Expose
    private Integer providerId;
    @SerializedName("providerName")
    @Expose
    private String providerName;
    @SerializedName("driverId")
    @Expose
    private Integer driverId;
    @SerializedName("driverLicense")
    @Expose
    private String driverLicense;
    @SerializedName("driverPhone")
    @Expose
    private String driverPhone;
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("createdUser")
    @Expose
    private String createdUser;
    @SerializedName("updatedUser")
    @Expose
    private String updatedUser;
    @SerializedName("createdTime")
    @Expose
    private String createdTime;
    @SerializedName("updatedTime")
    @Expose
    private String updatedTime;
    @SerializedName("invoiceId")
    @Expose
    private Object invoiceId;
    @SerializedName("paymentId")
    @Expose
    private Object paymentId;
    @SerializedName("vehicleId")
    @Expose
    private Integer vehicleId;
    @SerializedName("ride")
    @Expose
    private RideResp ride;
    @SerializedName("serviceType")
    @Expose
    private ServiceTypeResp serviceType;
    @SerializedName("status")
    @Expose
    private StatusResp status;
    @SerializedName("channel")
    @Expose
    private ChannelResp channel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
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

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Object getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Object invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Object getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Object paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public RideResp getRide() {
        return ride;
    }

    public void setRide(RideResp ride) {
        this.ride = ride;
    }

    public ServiceTypeResp getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeResp serviceType) {
        this.serviceType = serviceType;
    }

    public StatusResp getStatus() {
        return status;
    }

    public void setStatus(StatusResp status) {
        this.status = status;
    }

    public ChannelResp getChannel() {
        return channel;
    }

    public void setChannel(ChannelResp channel) {
        this.channel = channel;
    }
}
