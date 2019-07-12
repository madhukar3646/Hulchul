package com.app.zippnews.servicerequestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialloginRequest {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("info")
    @Expose
    private SocialInfoRequest info;

    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;

    @SerializedName("deviceType")
    @Expose
    private String deviceType;

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

    public SocialInfoRequest getInfo() {
        return info;
    }

    public void setInfo(SocialInfoRequest info) {
        this.info = info;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
