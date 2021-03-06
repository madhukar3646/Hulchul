package com.app.zippnews.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.app.zippnews.R;
import com.app.zippnews.adapters.SingleVideoAdapter;
import com.app.zippnews.adapters.SingleVideoPlayerViewHolder;
import com.app.zippnews.fileuploadservice.FileUploadService;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

public class SingleVideoPlayActivity extends AppCompatActivity implements View.OnClickListener, SingleVideoPlayerViewHolder.OnVideoCompletedListener{

    @BindView(R.id.player_container)
    Container container;
    @BindView(R.id.layout_post)
    RelativeLayout layout_post;

    SingleVideoAdapter adapter;
    LinearLayoutManager layoutManager;
    private ArrayList<VideoModel> modelArrayList=new ArrayList<>();
    private String videourl="";
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    private MediaPlayer musicplayer;
    private String musicpath,songid;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        if(getIntent().getStringExtra("songpath")!=null)
        {
            musicpath=getIntent().getStringExtra("songpath");
            songid=getIntent().getStringExtra("songid");
            musicplayer=new MediaPlayer();
            try {
                musicplayer.setDataSource(musicpath);
                musicplayer.prepare();
                musicplayer.start();
            }
            catch (IOException e) { Log.e("LOG_TAG", "prepare() failed"); }
        }

        if(getIntent().getStringExtra("isfrom").equalsIgnoreCase("VideoPosting_Activity"))
            layout_post.setVisibility(View.GONE);
        else
            layout_post.setVisibility(View.VISIBLE);

        connectionDetector=new ConnectionDetector(SingleVideoPlayActivity.this);
        sessionManagement=new SessionManagement(SingleVideoPlayActivity.this);
        layout_post.setOnClickListener(this);
        videourl=getIntent().getStringExtra("videourl");
        layoutManager = new LinearLayoutManager(SingleVideoPlayActivity.this);
        container.setLayoutManager(layoutManager);
        adapter = new SingleVideoAdapter(SingleVideoPlayActivity.this,modelArrayList);
        adapter.setOnVideoCompletedListener(this);
        container.setAdapter(adapter);

        handler.postDelayed(th,500);
    }


    private void setDataToContainer()
    {
        modelArrayList.clear();
        VideoModel model=new VideoModel();
        model.setVideo(videourl);
        modelArrayList.add(model);
        adapter.notifyDataSetChanged();
    }

    Runnable th=new Runnable() {
        @Override
        public void run() {
            setDataToContainer();
            handler.removeCallbacks(th);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_post:
                if(songid==null)
                    songid="";
                goToPostingScreen(videourl,songid);
               /* if(connectionDetector.isConnectingToInternet())
                {
                    if(songid==null)
                        songid="";
                   //uploadeVideo(sessionManagement.getValueFromPreference(SessionManagement.USERID),videourl,songid);
                    uploadfromBackground(sessionManagement.getValueFromPreference(SessionManagement.USERID),videourl,songid);
                }
                else
                    Utils.callToast(SingleVideoPlayActivity.this,getResources().getString(R.string.internet_toast));*/
                break;
        }
    }

    private void goToPostingScreen(String path,String songid)
    {
        Intent intent=new Intent(SingleVideoPlayActivity.this,VideoPosting_Activity.class);
        intent.putExtra("musicpath",musicpath);
        intent.putExtra("videopath",path);
        intent.putExtra("songid",songid);
        startActivity(intent);
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

        Intent intent=new Intent(SingleVideoPlayActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
    }

    public String getDraftVideoFilePath() {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/"+getResources().getString(R.string.app_name)+"drafts";
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

    /*private void uploadeVideo(String userid, String path,String songid){
        Utils.showDialog(SingleVideoPlayActivity.this);
        MultipartBody.Part fileToUpload=null;
        if(path!=null&&!path.isEmpty()) {
            final File file = new File(path);
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), mFile);
        }
        else {
            return;
        }
        RequestBody mid = RequestBody.create(MediaType.parse("text/plain"),userid);
        RequestBody sid = RequestBody.create(MediaType.parse("text/plain"),songid);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(this).uploadVideo(fileToUpload,mid,sid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    Utils.callToast(SingleVideoPlayActivity.this,"Posted Successfully");
                    new File(videourl).delete();
                    if(musicpath!=null)
                        new File(musicpath).delete();
                    Intent intent=new Intent(SingleVideoPlayActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    finishAffinity();
                }
                else {
                    Utils.callToast(SingleVideoPlayActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("add offer onFailure",""+call.toString());
            }
        });
    }*/

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

    @Override
    public void onBackPressed() {
        if(musicplayer!=null)
        musicplayer.release();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(musicplayer!=null)
            musicplayer.release();
        super.onDestroy();
    }

    @Override
    public void onVideoCompleted() {
        if(musicplayer!=null)
            musicplayer.seekTo(0);
    }
}
