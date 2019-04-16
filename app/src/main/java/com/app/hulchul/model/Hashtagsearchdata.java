package com.app.hulchul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hashtagsearchdata {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("hashTag")
    @Expose
    private String hashTag;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("popular")
    @Expose
    private String popular;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }
}
