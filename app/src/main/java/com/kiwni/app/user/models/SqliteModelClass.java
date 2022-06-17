package com.kiwni.app.user.models;

public class SqliteModelClass {

    private String addressType, address, favoriteLat, favoriteLng;

    public SqliteModelClass(String addressType, String address, String favoriteLat, String favoriteLng) {
        this.addressType = addressType;
        this.address = address;
        this.favoriteLat = favoriteLat;
        this.favoriteLng = favoriteLng;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFavoriteLat() {
        return favoriteLat;
    }

    public void setFavoriteLat(String favoriteLat) {
        this.favoriteLat = favoriteLat;
    }

    public String getFavoriteLng() {
        return favoriteLng;
    }

    public void setFavoriteLng(String favoriteLng) {
        this.favoriteLng = favoriteLng;
    }
}

