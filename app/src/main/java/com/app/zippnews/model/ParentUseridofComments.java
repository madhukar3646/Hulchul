package com.app.zippnews.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentUseridofComments {
    @SerializedName("_id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
