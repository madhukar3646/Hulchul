package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Discoverhashtags {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("hashTag")
    @Expose
    private String hashTag;
    @SerializedName("createdAt")
    @Expose
    private Object createdAt;
    @SerializedName("popular")
    @Expose
    private Object popular;
    @SerializedName("videos")
    @Expose
    private List<VideoModel> videos = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getPopular() {
        return popular;
    }

    public void setPopular(Object popular) {
        this.popular = popular;
    }

    public List<VideoModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoModel> videos) {
        this.videos = videos;
    }
}
