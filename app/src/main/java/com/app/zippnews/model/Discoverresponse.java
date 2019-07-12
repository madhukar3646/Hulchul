package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Discoverresponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Discoverhashtags> data = null;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("songurl")
    @Expose
    private String songurl;

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

    public List<Discoverhashtags> getData() {
        return data;
    }

    public void setData(List<Discoverhashtags> data) {
        this.data = data;
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
}
