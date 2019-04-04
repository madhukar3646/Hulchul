package com.app.hulchul.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentCommentsData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
