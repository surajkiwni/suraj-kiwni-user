package com.kiwni.app.user.datamodels;

public class FilterModel {

    int image;
    String name;


    public FilterModel(int image, String name) {
        this.image = image;
        this.name = name;
    }



    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
