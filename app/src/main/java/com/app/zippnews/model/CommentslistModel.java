package com.app.zippnews.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentslistModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parentId")
    @Expose
    private String parentId;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("likeCount")
    @Expose
    private String likeCount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("parentcoment")
    @Expose
    private String parentcoment;
    @SerializedName("parentuserId")
    @Expose
    private String parentuserId;
    @SerializedName("parentfullName")
    @Expose
    private String parentfullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getParentcoment() {
        return parentcoment;
    }

    public void setParentcoment(String parentcoment) {
        this.parentcoment = parentcoment;
    }

    public String getParentuserId() {
        return parentuserId;
    }

    public void setParentuserId(String parentuserId) {
        this.parentuserId = parentuserId;
    }

    public String getParentfullName() {
        return parentfullName;
    }

    public void setParentfullName(String parentfullName) {
        this.parentfullName = parentfullName;
    }
}
