package com.kiwni.app.user.models.socket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocketReservationResp {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("startLocation")
    @Expose
    private Object startLocation;
    @SerializedName("endLocation")
    @Expose
    private Object endLocation;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("createdByparty")
    @Expose
    private SocketCreatedByparty createdByparty;
    @SerializedName("provider")
    @Expose
    private SocketProvider provider;
    @SerializedName("driver")
    @Expose
    private SocketDriver driver;
    @SerializedName("passenger")
    @Expose
    private SocketPassenger passenger;
    @SerializedName("reservationStatus")
    @Expose
    private String reservationStatus;
    @SerializedName("bookingChannel")
    @Expose
    private String bookingChannel;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("acceptedByDriverAt")
    @Expose
    private Object acceptedByDriverAt;
    @SerializedName("reachedLocationAt")
    @Expose
    private Object reachedLocationAt;
    @SerializedName("vehicleId")
    @Expose
    private Integer vehicleId;
    @SerializedName("vehcileNo")
    @Expose
    private Object vehcileNo;
    @SerializedName("estimatedPrice")
    @Expose
    private Object estimatedPrice;
    @SerializedName("driverImageUrl")
    @Expose
    private Object driverImageUrl;
    @SerializedName("reservationId")
    @Expose
    private Integer reservationId;
    @SerializedName("invoiceId")
    @Expose
    private Object invoiceId;
    @SerializedName("paymentId")
    @Expose
    private Object paymentId;
    @SerializedName("startLocationCity")
    @Expose
    private String startLocationCity;
    @SerializedName("endlocationCity")
    @Expose
    private String endlocationCity;
    @SerializedName("scheduleId")
    @Expose
    private Long scheduleId;
    @SerializedName("otp")
    @Expose
    private String otp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Object startLocation) {
        this.startLocation = startLocation;
    }

    public Object getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Object endLocation) {
        this.endLocation = endLocation;
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

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public SocketCreatedByparty getCreatedByparty() {
        return createdByparty;
    }

    public void setCreatedByparty(SocketCreatedByparty createdByparty) {
        this.createdByparty = createdByparty;
    }

    public SocketProvider getProvider() {
        return provider;
    }

    public void setProvider(SocketProvider provider) {
        this.provider = provider;
    }

    public SocketDriver getDriver() {
        return driver;
    }

    public void setDriver(SocketDriver driver) {
        this.driver = driver;
    }

    public SocketPassenger getPassenger() {
        return passenger;
    }

    public void setPassenger(SocketPassenger passenger) {
        this.passenger = passenger;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getBookingChannel() {
        return bookingChannel;
    }

    public void setBookingChannel(String bookingChannel) {
        this.bookingChannel = bookingChannel;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Object getAcceptedByDriverAt() {
        return acceptedByDriverAt;
    }

    public void setAcceptedByDriverAt(Object acceptedByDriverAt) {
        this.acceptedByDriverAt = acceptedByDriverAt;
    }

    public Object getReachedLocationAt() {
        return reachedLocationAt;
    }

    public void setReachedLocationAt(Object reachedLocationAt) {
        this.reachedLocationAt = reachedLocationAt;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
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

    public String getStartLocationCity() {
        return startLocationCity;
    }

    public void setStartLocationCity(String startLocationCity) {
        this.startLocationCity = startLocationCity;
    }

    public String getEndlocationCity() {
        return endlocationCity;
    }

    public void setEndlocationCity(String endlocationCity) {
        this.endlocationCity = endlocationCity;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public void setData(Long id)
    {
        this.id = id;
    }

    public Object getVehcileNo() {
        return vehcileNo;
    }

    public void setVehcileNo(Object vehcileNo) {
        this.vehcileNo = vehcileNo;
    }

    public Object getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Object estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public Object getDriverImageUrl() {
        return driverImageUrl;
    }

    public void setDriverImageUrl(Object driverImageUrl) {
        this.driverImageUrl = driverImageUrl;
    }
}
