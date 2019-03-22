package com.app.hulchul.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.daasuu.camerarecorder.egl.filter.GlOverlayFilter;

public class FilterOverlays extends GlOverlayFilter {
    private Bitmap bitmap;

    public FilterOverlays(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void drawCanvas(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
