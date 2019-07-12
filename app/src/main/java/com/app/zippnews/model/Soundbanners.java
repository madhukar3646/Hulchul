package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Soundbanners {

    @SerializedName("songId")
    @Expose
    private String songId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("createAt")
    @Expose
    private Object createAt;

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Object createAt) {
        this.createAt = createAt;
    }
}
