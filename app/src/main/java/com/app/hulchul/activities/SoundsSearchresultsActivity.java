package com.app.hulchul.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.HashtagsGridAdapter;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.model.VideosListingResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SoundsSearchresultsActivity extends AppCompatActivity implements View.OnClickListener,HashtagsGridAdapter.OnHashtagItemClickListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_songthumbnail)
    ImageView iv_songthumbnail;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_authorname)
    TextView tv_authorname;
    @BindView(R.id.tv_videoscount)
    TextView tv_videoscount;
    @BindView(R.id.layout_favourites)
    RelativeLayout layout_favourites;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;
    @BindView(R.id.iv_gotorecord)
    ImageView iv_gotorecord;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.iv_favourite)
    ImageView iv_favourite;
    private boolean isUploading=false;

    private String soundname,songid,videosbasepath,musicbasepath,userid="",status;
    private HashtagsGridAdapter adapter;
    private ArrayList<VideoModel> discoverhashtagvideosList=new ArrayList<>();
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private boolean isFavorite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds_searchresults);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        soundname=getIntent().getStringExtra("soundname");
        songid=getIntent().getStringExtra("songid");
        if(getIntent().hasExtra("status"))
        {
            status=getIntent().getStringExtra("status");
            if(status!=null)
            if(status.equalsIgnoreCase("1"))
                iv_favourite.setImageResource(R.mipmap.fav_a_r);
        }

        connectionDetector=new ConnectionDetector(SoundsSearchresultsActivity.this);
        sessionManagement=new SessionManagement(SoundsSearchresultsActivity.this);
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        tv_name.setText(soundname);
        tv_title.setText(soundname);
        back_btn.setOnClickListener(this);
        layout_favourites.setOnClickListener(this);
        iv_gotorecord.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(SoundsSearchresultsActivity.this,3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        adapter=new HashtagsGridAdapter(SoundsSearchresultsActivity.this,discoverhashtagvideosList);
        adapter.setOnHashtagItemClickListener(this);
        recyclerview_videos.setAdapter(adapter);

        if (connectionDetector.isConnectingToInternet()) {
            discoverhashtagvideosList.clear();
            setDataToContainer("20", "0");
        }
        else
            Utils.callToast(SoundsSearchresultsActivity.this, getResources().getString(R.string.internet_toast));

        recyclerview_videos.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
                Log.e("pos"+pos,"numitems "+numItems);
                if((pos+1)>=numItems)
                {
                    if (connectionDetector.isConnectingToInternet()) {
                        setDataToContainer("20", ""+numItems);
                    } else
                        Utils.callToast(SoundsSearchresultsActivity.this, getResources().getString(R.string.internet_toast));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_favourites:
                if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
                {
                    if (connectionDetector.isConnectingToInternet()) {
                        addTofavourites(userid,"song", songid);
                    } else
                        Utils.callToast(SoundsSearchresultsActivity.this, getResources().getString(R.string.internet_toast));
                }
                else
                    startActivity(new Intent(SoundsSearchresultsActivity.this,LoginLandingActivity.class));

                break;
            case R.id.iv_gotorecord:
                if(!isUploading) {
                    if (checkingPermissionAreEnabledOrNot())
                    {
                        if (sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                            startActivity(new Intent(SoundsSearchresultsActivity.this, MakingVideoActivity.class));
                        else
                            startActivity(new Intent(SoundsSearchresultsActivity.this, LoginLandingActivity.class));
                    } else
                        requestMultiplePermission();
                }
                else {
                    Utils.callToast(SoundsSearchresultsActivity.this,"Your previous video is uploading. Please wait!");
                }
                break;
        }
    }

    private void setDataToContainer(String limit,String offset){
        Utils.showDialog(SoundsSearchresultsActivity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(SoundsSearchresultsActivity.this).videoBySong(userid,songid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            discoverhashtagvideosList.addAll(body.getVideos());
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            tv_videoscount.setText(""+body.getTotalcount());
                            adapter.setVideobasepath(videosbasepath);
                            adapter.notifyDataSetChanged();
                            tv_nodata.setVisibility(View.GONE);


                        }
                    } else {
                        if(discoverhashtagvideosList.size()==0) {


                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(SoundsSearchresultsActivity.this, body.getMessage());
                        }
                    }

                    if(body.getFavouriteStatus()!=null){
                        if( body.getFavouriteStatus().equalsIgnoreCase("1")){
                            iv_favourite.setImageResource(R.mipmap.fav_a_r);
                            isFavorite=true;
                        }
                    }
                }
                else {
                    Utils.callToast(SoundsSearchresultsActivity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onHashtagitemClick(ArrayList<VideoModel> discoverhashtagvideosList, int pos) {
        Intent intent=new Intent(SoundsSearchresultsActivity.this, PlayvideosCategorywise_Activity.class);
        intent.putExtra("isFrom","sounds");
        intent.putExtra("songid",songid);
        intent.putParcelableArrayListExtra("videos",discoverhashtagvideosList);
        intent.putExtra("position",pos);
        intent.putExtra("videosbasepath",videosbasepath);
        intent.putExtra("musicbasepath",musicbasepath);
        startActivity(intent);
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
            String path = intent.getStringExtra("videopath");
            if(result.equalsIgnoreCase("101")) {
                Utils.callToast(SoundsSearchresultsActivity.this,"Your video uploaded successfully");
                isUploading=false;
            }
            else {
                isUploading=true;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    public boolean checkingPermissionAreEnabledOrNot() {
        int camera = ContextCompat.checkSelfPermission(SoundsSearchresultsActivity.this, CAMERA);
        int write = ContextCompat.checkSelfPermission(SoundsSearchresultsActivity.this, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(SoundsSearchresultsActivity.this, RECORD_AUDIO);
        return camera == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED && read==PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission() {

        ActivityCompat.requestPermissions(SoundsSearchresultsActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        RECORD_AUDIO
                }, 100);
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
                                startActivity(new Intent(SoundsSearchresultsActivity.this,MakingVideoActivity.class));
                            else
                                startActivity(new Intent(SoundsSearchresultsActivity.this, LoginLandingActivity.class));
                        }
                        else
                            requestMultiplePermission();
                    }
                } else {
                    requestMultiplePermission();
                }
                break;
        }
    }

    private void addTofavourites(String userid, String type, String favouriteid){
        Utils.showDialog(SoundsSearchresultsActivity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(SoundsSearchresultsActivity.this).addFavourite(userid,type,favouriteid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    iv_favourite.setImageResource(R.mipmap.fav_a_r);
                    if(isFavorite){
                        iv_favourite.setImageResource(R.mipmap.fav_a_w);
                        isFavorite=false;
                    }
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("addfav onFailure",""+t.getMessage());
            }
        });
    }
}
