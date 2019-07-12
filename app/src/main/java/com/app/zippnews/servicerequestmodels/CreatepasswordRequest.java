package com.app.zippnews.servicerequestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatepasswordRequest {
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
