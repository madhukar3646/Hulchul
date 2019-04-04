package com.app.hulchul.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentCommentContainer {
    @SerializedName("commentId")
    @Expose
    private ParentCommentsData commentId;
    @SerializedName("userId")
    @Expose
    private ParentUseridofComments userId;

    public ParentCommentsData getCommentId() {
        return commentId;
    }

    public void setCommentId(ParentCommentsData commentId) {
        this.commentId = commentId;
    }

    public ParentUseridofComments getUserId() {
        return userId;
    }

    public void setUserId(ParentUseridofComments userId) {
        this.userId = userId;
    }
}
