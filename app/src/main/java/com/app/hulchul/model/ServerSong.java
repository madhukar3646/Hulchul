package com.app.hulchul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerSong {

    private boolean isPlaying = false;
    private boolean isHeader=false;
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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createAt")
    @Expose
    private String createAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("duration")
    @Expose
    private String duration;

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
