package com.kiwni.app.user.models;

public class Offers {

    int image;

    public Offers(int image) {
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
