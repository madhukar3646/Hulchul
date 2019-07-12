package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileViewdata {

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
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("videos")
    @Expose
    private String videos;
    @SerializedName("following")
    @Expose
    private String following;

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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollwerstatus() {
        return follwerstatus;
    }

    public void setFollwerstatus(String follwerstatus) {
        this.follwerstatus = follwerstatus;
    }
}
