package com.app.zippnews.servicerequestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendOtpRequest{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("username")
    @Expose
    private String username;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
