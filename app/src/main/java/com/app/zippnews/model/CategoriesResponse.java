package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("categories")
    @Expose
    private List<CategoryModel> data = null;

    @SerializedName("usercategories")
    @Expose
    private List<UserAddedCategory> usercategories = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CategoryModel> getData() {
        return data;
    }

    public void setData(List<CategoryModel> data) {
        this.data = data;
    }

    public List<UserAddedCategory> getUsercategories() {
        return usercategories;
    }

    public void setUsercategories(List<UserAddedCategory> usercategories) {
        this.usercategories = usercategories;
    }
}
