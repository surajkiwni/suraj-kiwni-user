package com.kiwni.app.user.datamodels;

public class HourPackageModel {

    String name , kilometer;

    public HourPackageModel(String name, String kilometer) {
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
