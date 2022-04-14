package com.kiwni.app.user.models.bookride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusReq
{
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
