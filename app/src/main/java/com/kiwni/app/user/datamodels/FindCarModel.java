package com.kiwni.app.user.datamodels;

public class FindCarModel {

    int image;
    String carName;

    public FindCarModel(int image, String carName) {
        this.image = image;
        this.carName = carName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}
