package com.app.zippnews.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentslistingResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<CommentslistModel> data = null;
    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;

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

    public List<CommentslistModel> getData() {
        return data;
    }

    public void setData(List<CommentslistModel> data) {
        this.data = data;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }
}
