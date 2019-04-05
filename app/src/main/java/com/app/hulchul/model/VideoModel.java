package com.app.hulchul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    @SerializedName("songfile")
    @Expose
    private String songfile;

    @SerializedName("songId")
    @Expose
    private String songId;
    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("likestatus")
    @Expose
    private String likestatus;

    @SerializedName("follwerstatus")
    @Expose
    private String follwerstatus;

    @SerializedName("followersCount")
    @Expose
    private String followersCount;

    @SerializedName("shareCount")
    @Expose
    private String shareCount;

    @SerializedName("commentCount")
    @Expose
    private String commentCount;

    @SerializedName("comments")
    @Expose
    private List<HomescreenCommentModel> comments = null;

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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getSongfile() {
        return songfile;
    }

    public void setSongfile(String songfile) {
        this.songfile = songfile;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLikestatus() {
        return likestatus;
    }

    public void setLikestatus(String likestatus) {
        this.likestatus = likestatus;
    }

    public String getFollwerstatus() {
        return follwerstatus;
    }

    public void setFollwerstatus(String follwerstatus) {
        this.follwerstatus = follwerstatus;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public List<HomescreenCommentModel> getComments() {
        return comments;
    }

    public void setComments(List<HomescreenCommentModel> comments) {
        this.comments = comments;
    }
}
