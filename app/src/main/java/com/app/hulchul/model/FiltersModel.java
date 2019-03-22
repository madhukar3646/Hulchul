package com.app.hulchul.model;

public class FiltersModel {
    int thumbnailid,filter_layer;
    boolean isSelected=false;

    public int getThumbnailid() {
        return thumbnailid;
    }

    public void setThumbnailid(int thumbnailid) {
        this.thumbnailid = thumbnailid;
    }

    public int getFilter_layer() {
        return filter_layer;
    }

    public void setFilter_layer(int filter_layer) {
        this.filter_layer = filter_layer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
