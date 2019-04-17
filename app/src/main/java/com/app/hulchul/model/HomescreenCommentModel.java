package com.app.hulchul.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomescreenCommentModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    protected HomescreenCommentModel(Parcel in) {
        id = in.readString();
        videoId = in.readString();
        userId = in.readString();
        comment = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<HomescreenCommentModel> CREATOR = new Creator<HomescreenCommentModel>() {
        @Override
        public HomescreenCommentModel createFromParcel(Parcel in) {
            return new HomescreenCommentModel(in);
        }

        @Override
        public HomescreenCommentModel[] newArray(int size) {
            return new HomescreenCommentModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(videoId);
        parcel.writeString(userId);
        parcel.writeString(comment);
        parcel.writeString(createdAt);
    }
}
