package com.kiwni.app.user.models;

public class KeyValue {
    public String key;
    public String value;

    public KeyValue(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
