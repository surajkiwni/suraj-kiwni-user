package com.kiwni.app.user.models;

public class HourPackage {

    String name , kilometer;

    public HourPackage(String name, String kilometer) {
        this.name = name;
        this.kilometer = kilometer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }
}
