package com.app.hulchul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewProfileResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ProfileViewdata data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileViewdata getData() {
        return data;
    }

    public void setData(ProfileViewdata data) {
        this.data = data;
    }
}
