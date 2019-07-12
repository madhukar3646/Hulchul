package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosListingResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("videos")
    @Expose
    private List<VideoModel> videos = null;
    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("songurl")
    @Expose
    private String songurl;

    @SerializedName("FavouriteStatus")
    @Expose
    String favouriteStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<VideoModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoModel> videos) {
        this.videos = videos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongurl() {
        return songurl;
    }

    public void setSongurl(String songurl) {
        this.songurl = songurl;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }
}
