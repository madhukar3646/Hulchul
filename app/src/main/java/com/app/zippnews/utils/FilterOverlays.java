package com.app.zippnews.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.daasuu.camerarecorder.egl.filter.GlOverlayFilter;

public class FilterOverlays extends GlOverlayFilter {
    private Bitmap bitmap;

    public FilterOverlays(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void drawCanvas(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Log.e("drawing overlay","overlay");
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
