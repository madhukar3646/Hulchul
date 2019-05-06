package com.app.hulchul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("video")
    @Expose
    private String video;

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("comment")
    @Expose
    private String comment;





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getComment() {
        if(comment==null)
            comment="";
        else if(comment.equalsIgnoreCase("null"))
            comment="";
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
