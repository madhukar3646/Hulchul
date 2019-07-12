package com.app.zippnews.servicerequestmodels;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentPostRequest {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("parentId")
    @Expose
    private String parentId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
