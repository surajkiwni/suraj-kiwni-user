package com.kiwni.app.user.datamodels;

public class BookingModel {

    int image;
    String name;
    int offer1;


    public BookingModel( String name) {

        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

}

