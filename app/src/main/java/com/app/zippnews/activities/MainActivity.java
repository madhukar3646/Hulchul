package com.app.zippnews.activities;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.fileuploadservice.FileUploadService;
import com.app.zippnews.fragments.Discover_fragment;
import com.app.zippnews.fragments.Discovernews_fragment;
import com.app.zippnews.fragments.Home_fragment;
import com.app.zippnews.fragments.Me_Fragment;
import com.app.zippnews.fragments.Notification_fragment;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.layout_frame)
    FrameLayout layout_frame;
    @BindView(R.id.layout_home)
    LinearLayout layout_home;
    @BindView(R.id.layout_discover)
    LinearLayout layout_discover;
    @BindView(R.id.layout_video)
    LinearLayout layout_video;
    @BindView(R.id.layout_notification)
    LinearLayout layout_notification;
    @BindView(R.id.layout_me)
    LinearLayout layout_me;

    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_discover)
    ImageView iv_discover;
    @BindView(R.id.iv_video)
    ImageView iv_video;
    @BindView(R.id.iv_notification)
    ImageView iv_notification;
    @BindView(R.id.iv_me)
    ImageView iv_me;
    @BindView(R.id.layout_bottomicons)
    LinearLayout layout_bottomicons;

    @BindView(R.id.layout_uploadinglayout)
    RelativeLayout layout_uploadinglayout;
    @BindView(R.id.tv_uploadpercentage)
    TextView tv_uploadpercentage;
    @BindView(R.id.iv_gifloading)
    ImageView iv_gifloading;
    private boolean isUploading=false;

    private FragmentTransaction ft;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private SessionManagement sessionManagement;
    private int backpressclicks=0;
    private Handler handler=new Handler();
    private Bitmap uploadingbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
      /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        init();
    }

    private void init()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "getInstanceId failed "+task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        storeRegIdInPref(token);
                    }
                });

        layout_uploadinglayout.setVisibility(View.GONE);

       /* Glide.with(this)
                .load(R.drawable.gif_loading)
                .placeholder(R.drawable.gif_loading)
                .into(iv_gifloading);*/

        sessionManagement=new SessionManagement(MainActivity.this);
        layout_home.setOnClickListener(this);
        layout_discover.setOnClickListener(this);
        layout_notification.setOnClickListener(this);
        layout_video.setOnClickListener(this);
        layout_me.setOnClickListener(this);

        iv_home.setImageResource(R.mipmap.home_activeicon);
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        fragment=new Home_fragment();
        ft.add(R.id.layout_frame,fragment);
        ft.commit();
    }

    private void changeFragment(Fragment fragment)
    {
        this.fragment=fragment;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.layout_frame, fragment);
        ft.commit();
        invalidateOptionsMenu();

        if(this.fragment instanceof Home_fragment) {
            layout_bottomicons.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            layout_bottomicons.setBackgroundColor(Color.BLACK);
        }
    }

    private void setVisibleFragmentFocus()
    {
        if(this.fragment instanceof Home_fragment)
        {
            setClickableFocus();
            iv_home.setImageResource(R.mipmap.home_activeicon);
            //tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(this.fragment instanceof Discover_fragment)
        {
            setClickableFocus();
            iv_discover.setImageResource(R.mipmap.search_active);
            //tv_discover.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(this.fragment instanceof Notification_fragment)
        {
            setClickableFocus();
            iv_notification.setImageResource(R.mipmap.notification_active);
            //tv_notification.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(this.fragment instanceof Me_Fragment)
        {
            setClickableFocus();
            iv_me.setImageResource(R.mipmap.me_active);
            //tv_me.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleFragmentFocus();
        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
        if(!FileUploadService.isProcessing || tv_uploadpercentage.getText().toString().trim().length()>6) {
            uploadingbmp=null;
            isUploading=false;
            layout_uploadinglayout.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            String path = intent.getStringExtra("videopath");
            if(result.equalsIgnoreCase("101")) {
                Utils.callToast(MainActivity.this,"Your video uploaded successfully");
                isUploading=false;
                layout_uploadinglayout.setVisibility(View.GONE);
                uploadingbmp=null;
            }
            else if(result.trim().length()>5)
            {
                isUploading=false;
                layout_uploadinglayout.setVisibility(View.GONE);
                uploadingbmp=null;
                Utils.callToast(MainActivity.this,"Your file uploading failed. Please try again from drafts");
            }
            else {
                if(uploadingbmp==null) {
                    uploadingbmp = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
                    iv_gifloading.setImageBitmap(uploadingbmp);
                }
                layout_uploadinglayout.setVisibility(View.VISIBLE);
                isUploading=true;
            }
            tv_uploadpercentage.setText(result);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.layout_home:
                changeFragment(new Home_fragment());
                setClickableFocus();
                iv_home.setImageResource(R.mipmap.home_activeicon);
                //tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.layout_discover:
                changeFragment(new Discovernews_fragment());
                setClickableFocus();
                iv_discover.setImageResource(R.mipmap.search_active);
                //tv_discover.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.layout_video:
                if(!isUploading) {
                    setClickableFocus();
                    if (checkingPermissionAreEnabledOrNot()) {
                        if (sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                            startActivity(new Intent(MainActivity.this, MakingVideoActivity.class));
                        else
                            startActivity(new Intent(MainActivity.this, LoginLandingActivity.class));
                    } else
                        requestMultiplePermission();
                }
                else {
                    Utils.callToast(MainActivity.this,"Your previous video is uploading. Please wait!");
                }

                break;
            case R.id.layout_notification:
                if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null) {
                    if (checkingFilePermissions()) {
                        changeFragment(new Notification_fragment());
                        setClickableFocus();
                        iv_notification.setImageResource(R.mipmap.notification_active);
                        //tv_me.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        requestFilePermission();
                    }
                }  else {
                    startActivity(new Intent(MainActivity.this, LoginLandingActivity.class));
                }


                //tv_notification.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.layout_me:
                if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN)) {
                    if (checkingFilePermissions()) {
                        changeFragment(new Me_Fragment());
                        setClickableFocus();
                        iv_me.setImageResource(R.mipmap.me_active);
                        //tv_me.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        requestFilePermission();
                    }
                }
                else {
                    startActivity(new Intent(MainActivity.this, LoginLandingActivity.class));
                }
                break;
        }
    }

    private void setClickableFocus()
    {
      iv_home.setImageResource(R.mipmap.home_normalicon);
      iv_discover.setImageResource(R.mipmap.search_normal);
      iv_notification.setImageResource(R.mipmap.notification_normal);
      iv_me.setImageResource(R.mipmap.me_normal);
    }

    public boolean checkingPermissionAreEnabledOrNot() {
        int camera = ContextCompat.checkSelfPermission(MainActivity.this, CAMERA);
        int write = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(MainActivity.this, RECORD_AUDIO);
        return camera == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED && read==PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        RECORD_AUDIO
                }, 100);
    }

    public boolean checkingFilePermissions() {
        int write = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
        return write == PackageManager.PERMISSION_GRANTED;
    }
    private void requestFilePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE
                }, 130);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=3)
                    {
                        if(checkingPermissionAreEnabledOrNot())
                        {
                            if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                                startActivity(new Intent(MainActivity.this,MakingVideoActivity.class));
                            else
                                startActivity(new Intent(MainActivity.this, LoginLandingActivity.class));
                        }
                        else
                            requestMultiplePermission();
                    }
                } else {
                    requestMultiplePermission();
                }
                break;

            case 120:
                if(fragment instanceof onFilePermissionListenerForFragment)
                    fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
            case 200:
                if(fragment instanceof onFilePermissionListenerForFragment)
                    fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
            case 130:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=1)
                    {
                        if(checkingFilePermissions())
                        {
                            changeFragment(new Me_Fragment());
                            setClickableFocus();
                            iv_me.setImageResource(R.mipmap.me_active);
                        }
                        else
                            requestFilePermission();
                    }
                } else {
                    requestFilePermission();
                }
                break;
        }
    }

    public interface onFilePermissionListenerForFragment
    {
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }

    @Override
    public void onBackPressed() {
        if(!isUploading) {
            backpressclicks++;
            handler.postDelayed(backpress,3000);
            if (backpressclicks>=2) {
                handler.removeCallbacks(backpress);
                super.onBackPressed();
            }
            else
                Utils.callToast(MainActivity.this,"Press again to exit.");
        }
        else {
            displayExitDialog();
        }
    }

    private void displayExitDialog()
    {
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.main_exit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tv_exit=(TextView)dialog.findViewById(R.id.tv_exit);
        TextView tv_cancel=(TextView)dialog.findViewById(R.id.tv_cancel);

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                finish();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    Runnable backpress=new Runnable() {
        @Override
        public void run() {
            backpressclicks=0;
        }
    };

    private void storeRegIdInPref(String token) {
        Log.e("device token ","is "+token);
        SessionManagement sessionManagement=new SessionManagement(getApplicationContext());
        sessionManagement.setValuetoPreference(SessionManagement.DEVICETOKEN,token);
    }
}
