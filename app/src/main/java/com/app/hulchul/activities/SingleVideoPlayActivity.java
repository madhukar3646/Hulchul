package com.app.hulchul.activities;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.adapters.SingleVideoAdapter;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleVideoPlayActivity extends AppCompatActivity implements View.OnClickListener{

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
    private String musicpath;

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
            musicplayer=new MediaPlayer();
            try {
                musicplayer.setDataSource(musicpath);
                musicplayer.prepare();
                musicplayer.start();
            }
            catch (IOException e) { Log.e("LOG_TAG", "prepare() failed"); }
        }

        connectionDetector=new ConnectionDetector(SingleVideoPlayActivity.this);
        sessionManagement=new SessionManagement(SingleVideoPlayActivity.this);
        layout_post.setOnClickListener(this);
        videourl=getIntent().getStringExtra("videourl");
        layoutManager = new LinearLayoutManager(SingleVideoPlayActivity.this);
        container.setLayoutManager(layoutManager);
        adapter = new SingleVideoAdapter(SingleVideoPlayActivity.this,modelArrayList);
        container.setAdapter(adapter);
        setDataToContainer();
    }

    private void setDataToContainer()
    {
        modelArrayList.clear();
        VideoModel model=new VideoModel();
        model.setVideo(videourl);
        modelArrayList.add(model);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_post:

                if(connectionDetector.isConnectingToInternet())
                {
                   uploadeVideo(sessionManagement.getValueFromPreference(SessionManagement.USERID),videourl);
                }
                else
                    Utils.callToast(SingleVideoPlayActivity.this,getResources().getString(R.string.internet_toast));
              /* Utils.callToast(SingleVideoPlayActivity.this,"Post");
                Intent intent=new Intent(SingleVideoPlayActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                finishAffinity();*/
                break;
        }
    }

    private void uploadeVideo(String userid, String path){
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
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(this).uploadVideo(fileToUpload,mid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    Utils.callToast(SingleVideoPlayActivity.this,"Posted Successfully");
                    new File(videourl).delete();
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
    }

    @Override
    public void onBackPressed() {
        if(musicplayer!=null)
        musicplayer.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(musicplayer!=null)
            musicplayer.stop();
        super.onDestroy();
    }
}
