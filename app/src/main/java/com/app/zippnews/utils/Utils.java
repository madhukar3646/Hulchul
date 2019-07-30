package com.app.zippnews.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.Toast;

import com.app.zippnews.CommonEmptyActivity;
import com.app.zippnews.R;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

public class Utils {

    public static Dialog dialog;
    public static final int FROMCOMMENTS=2000;
    public static final String ISFROMCOMMENTS="isFromComments";
    public static DilatingDotsProgressBar mDilatingDotsProgressBar;
    public static void showDialog(Context context)
    {
        dialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.loading);
        mDilatingDotsProgressBar = (DilatingDotsProgressBar)dialog.findViewById(R.id.progress);
        mDilatingDotsProgressBar.showNow();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismissDialog()
    {
        if(mDilatingDotsProgressBar!=null)
          mDilatingDotsProgressBar.hide();
        if(dialog!=null)
            dialog.dismiss();
    }

    public static void callToast(Context context,String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void goToCommonActivity(Activity activity,String displayMessage)
    {
        Intent intent=new Intent(activity, CommonEmptyActivity.class);
        intent.putExtra("common",displayMessage);
        activity.startActivity(intent);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
