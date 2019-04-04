package com.app.hulchul.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentUserdata {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("photo")
    @Expose
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
