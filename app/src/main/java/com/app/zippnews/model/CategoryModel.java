package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("image")
    @Expose
    private String image;

    private boolean isCategory=true;
    private boolean isHashtag=false;
    private boolean isLocation=false;
    private String lattitude;
    private String longnitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setCategory(boolean category) {
        isCategory = category;
    }

    public boolean isHashtag() {
        return isHashtag;
    }

    public void setHashtag(boolean hashtag) {
        isHashtag = hashtag;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void setLocation(boolean location) {
        isLocation = location;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongnitude() {
        return longnitude;
    }

    public void setLongnitude(String longnitude) {
        this.longnitude = longnitude;
    }
}
