package com.app.hulchul.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentslistModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("userId")
    @Expose
    private CommentUserdata userId;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("parentId")
    @Expose
    private ParentCommentContainer parentId;
    @SerializedName("likes")
    @Expose
    private List<Object> likes = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public CommentUserdata getUserId() {
        return userId;
    }

    public void setUserId(CommentUserdata userId) {
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

    public ParentCommentContainer getParentId() {
        return parentId;
    }

    public void setParentId(ParentCommentContainer parentId) {
        this.parentId = parentId;
    }

    public List<Object> getLikes() {
        return likes;
    }

    public void setLikes(List<Object> likes) {
        this.likes = likes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
