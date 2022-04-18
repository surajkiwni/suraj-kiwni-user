package com.kiwni.app.user.models;

public class ReviewResponse {

    String name;
    String Decription;

    public ReviewResponse(String name, String decription) {
        this.name = name;
        Decription = decription;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return Decription;
    }

    public void setDecription(String decription) {
        Decription = decription;
    }

    @Override
    public String toString() {
        return "ReviewResponse{" +
                "name='" + name + '\'' +
                ", Decription='" + Decription + '\'' +
                '}';
    }
}
