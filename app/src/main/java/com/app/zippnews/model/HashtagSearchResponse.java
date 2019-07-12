package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HashtagSearchResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<Hashtagsearchdata> data = null;

    @SerializedName("videos")
    @Expose
    private List<Hashtagsearchdata> hashtagsdata = null;

    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;

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

    public List<Hashtagsearchdata> getData() {
            return data;
    }
    public void setData(List<Hashtagsearchdata> data) {
        this.data = data;
    }

    public List<Hashtagsearchdata> getHashtagsdata() {
        return hashtagsdata;
    }

    public void setHashtagsdata(List<Hashtagsearchdata> hashtagsdata) {
        this.hashtagsdata = hashtagsdata;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
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
