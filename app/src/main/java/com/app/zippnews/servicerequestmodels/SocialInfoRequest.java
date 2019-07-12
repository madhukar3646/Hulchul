package com.app.zippnews.servicerequestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialInfoRequest{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
