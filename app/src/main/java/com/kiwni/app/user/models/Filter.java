package com.kiwni.app.user.models;

public class Filter {

    int image;
    String name;

    public Filter(int image, String name) {
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
