package com.app.zippnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SoundsContainer {
    @SerializedName("categoryList")
    @Expose
    private List<SoundsCategorylist> categoryList = null;
    @SerializedName("songlist")
    @Expose
    private List<Songlistmodel> songlist = null;
    @SerializedName("topbanners")
    @Expose
    private List<Soundbanners> topbanners = null;

    public List<SoundsCategorylist> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<SoundsCategorylist> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Songlistmodel> getSonglist() {
        return songlist;
    }

    public void setSonglist(List<Songlistmodel> songlist) {
        this.songlist = songlist;
    }

    public List<Soundbanners> getTopbanners() {
        return topbanners;
    }

    public void setTopbanners(List<Soundbanners> topbanners) {
        this.topbanners = topbanners;
    }
}
