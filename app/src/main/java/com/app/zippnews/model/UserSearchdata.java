package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSearchdata {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("bioData")
    @Expose
    private String bioData;
    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("follwerstatus")
    @Expose
    private String follwerstatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBioData() {
        return bioData;
    }

    public void setBioData(String bioData) {
        this.bioData = bioData;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFollwerstatus() {
        return follwerstatus;
    }

    public void setFollwerstatus(String follwerstatus) {
        this.follwerstatus = follwerstatus;
    }
}
