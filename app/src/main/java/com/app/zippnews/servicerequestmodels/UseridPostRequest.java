package com.app.zippnews.servicerequestmodels;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UseridPostRequest {

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
