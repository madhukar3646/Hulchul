package com.app.hulchul.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.app.hulchul.R;
import com.app.hulchul.fileuploadservice.FileUploadService;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPosting_Activity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.iv_videothumbnail)
    ImageView iv_videothumbnail;
    @BindView(R.id.iv_addfriends)
    ImageView iv_addfriends;
    @BindView(R.id.iv_addhashtags)
    ImageView iv_addhashtags;
    @BindView(R.id.et_hashtags)
    EditText et_hashtags;
    @BindView(R.id.layout_public)
    RelativeLayout layout_public;
    @BindView(R.id.switchButton)
    Switch switchButton;
    @BindView(R.id.iv_whatsapp)
    ImageView iv_whatsapp;
    @BindView(R.id.iv_facebook)
    ImageView iv_facebook;
    @BindView(R.id.iv_twitter)
    ImageView iv_twitter;
    @BindView(R.id.iv_instagram)
    ImageView iv_instagram;
    @BindView(R.id.iv_gmail)
    ImageView iv_gmail;
    @BindView(R.id.layout_save)
    RelativeLayout layout_save;
    @BindView(R.id.layout_post)
    RelativeLayout layout_post;

    private String videopath;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String musicpath,songid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_posting_);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(VideoPosting_Activity.this);
        sessionManagement=new SessionManagement(VideoPosting_Activity.this);
        Intent intent=getIntent();
        musicpath=intent.getStringExtra("musicpath");
        songid=intent.getStringExtra("songid");
        videopath=intent.getStringExtra("videopath");
        iv_videothumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail(videopath, MediaStore.Video.Thumbnails.MINI_KIND));

        back_btn.setOnClickListener(this);
        layout_post.setOnClickListener(this);
        layout_save.setOnClickListener(this);
        iv_videothumbnail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_post:
                if(connectionDetector.isConnectingToInternet())
                {
                    uploadfromBackground(sessionManagement.getValueFromPreference(SessionManagement.USERID),videopath,songid);
                }
                else
                    Utils.callToast(VideoPosting_Activity.this,getResources().getString(R.string.internet_toast));
                break;
            case R.id.layout_save:

                String draftpath=moveFileToDraft(videopath);
                if(musicpath!=null)
                    new File(musicpath).delete();
                Intent intent=new Intent(VideoPosting_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
                finishAffinity();

                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.iv_videothumbnail:
               goToSinglePlayActivity(videopath);
                break;
        }
    }

    private String getHashTags()
    {
        String hashtags="";
        String hashes=et_hashtags.getText().toString().trim();
        String arr[]=hashes.split("#");
        hashes="";
        for(int i=0;i<arr.length;i++)
        {
            hashes=hashes+"#"+arr[i].trim()+",";
        }
        if(hashes.length()>2)
            hashtags=hashes.substring(2,hashes.length()-1);

        return hashtags;
    }

    private void uploadfromBackground(String userid, String path,String songid)
    {
        String draftpath=moveFileToDraft(path);
        if(musicpath!=null)
            new File(musicpath).delete();

        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", draftpath);
        mIntent.putExtra("songid", songid);
        mIntent.putExtra("userid", userid);
        FileUploadService.enqueueWork(this, mIntent);

        Intent intent=new Intent(VideoPosting_Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
    }

    public String getDraftVideoFilePath() {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchuldrafts";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, ""+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4");
        //return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4";
        return file.getAbsolutePath();
    }

    private String moveFileToDraft(String SourceFilePath)
    {
        File SourceFile = new File(SourceFilePath);

        File DestinationFile = new File(getDraftVideoFilePath());

        if(SourceFile.renameTo(DestinationFile))
        {
            Log.e("Moving", "Moving file successful.");
        }
        else
        {
            Log.e("Moving", "Moving file failed.");
        }
        return DestinationFile.getAbsolutePath();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    private void goToSinglePlayActivity(String filepath)
    {
        Intent intent=new Intent(VideoPosting_Activity.this,SingleVideoPlayActivity.class);
        intent.putExtra("isfrom","VideoPosting_Activity");
        intent.putExtra("videourl",filepath);
        intent.putExtra("songpath",musicpath);
        intent.putExtra("songid",songid);
        startActivity(intent);
    }
}
