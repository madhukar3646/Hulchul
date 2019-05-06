package com.app.hulchul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoundSearchdata {

    @SerializedName("songId")
    @Expose
    private String songId;
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
    private String status;
    @SerializedName("createAt")
    @Expose
    private String createAt;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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
}
