package com.kiwni.app.user.datamodels;

public class OffersModel {

    int image;

    public OffersModel(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "OffersModel{" +
                "image=" + image +
                '}';
    }
}
