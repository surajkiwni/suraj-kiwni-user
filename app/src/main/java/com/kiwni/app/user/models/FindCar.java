package com.kiwni.app.user.models;

public class FindCar {

    int image;
    String carName;

    public FindCar(int image, String carName) {
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
