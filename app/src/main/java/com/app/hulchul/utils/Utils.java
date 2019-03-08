package com.app.hulchul.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.app.hulchul.R;

public class Utils {

    public static Dialog dialog;
    public static void showDialog(Context context)
    {
        dialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismissDialog()
    {
        if(dialog!=null)
            dialog.dismiss();
    }
    public static void callToast(Context context,String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
