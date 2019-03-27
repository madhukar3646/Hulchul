package com.app.hulchul.model;

import android.graphics.Bitmap;

/**
 * Created by madhu on 7/30/2018.
 */

public class LocalVideo_Model {

    String videoPath,duration;
    Bitmap bitmap;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
