package com.app.zippnews.activities;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.WindowManager;
import com.app.zippnews.R;
import com.app.zippnews.adapters.SimpleAdapter;
import com.app.zippnews.adapters.SimplePlayerViewHolder;
import com.app.zippnews.fileuploadservice.FileUploadService;
import com.app.zippnews.model.SignupResponse;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.model.VideosListingResponse;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ApiUrls;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PlayvideosCategorywise_Activity extends AppCompatActivity implements SimpleAdapter.VideoActionsListener{

    @BindView(R.id.player_container)
    Container container;
    SimpleAdapter adapter;
    LinearLayoutManager layoutManager;
    private ArrayList<VideoModel> modelArrayList=new ArrayList<>();
    private ArrayList<VideoModel> videoslist=new ArrayList<>();
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String videoServerUrl;
    private SimplePlayerViewHolder holder;
    private String userid="",videoUserid,videoid;
    private int position,videoposition;
    private Dialog dialog;
    private String isFrom,hashtag,songid,profileuserid,categoryid;
    private int uploadpercentagelength;
    private boolean isUploading=false;
    private String lattitude,longnitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideos_categorywise);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(PlayvideosCategorywise_Activity.this);
        sessionManagement=new SessionManagement(PlayvideosCategorywise_Activity.this);
        Log.e("user id","is "+sessionManagement.getValueFromPreference(SessionManagement.USERID));
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        layoutManager = new LinearLayoutManager(PlayvideosCategorywise_Activity.this);
        SnapHelper snapHelper = new PagerSnapHelper();
        container.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(container);

        adapter = new SimpleAdapter(PlayvideosCategorywise_Activity.this,modelArrayList);
        adapter.setVideoActionsListener(this);
        container.setAdapter(adapter);

        if(connectionDetector.isConnectingToInternet())
        {
            modelArrayList.clear();
            adapter.notifyDataSetChanged();
            videoslist=getIntent().getParcelableArrayListExtra("videos");
            videoposition=getIntent().getIntExtra("position",0);
            isFrom=getIntent().getStringExtra("isFrom");
            if(isFrom.equalsIgnoreCase("hashTag"))
                hashtag=getIntent().getStringExtra("hashTag");
            else if(isFrom.equalsIgnoreCase("sounds"))
                songid=getIntent().getStringExtra("songid");
            else if(isFrom.equalsIgnoreCase("profilevideos"))
                profileuserid=getIntent().getStringExtra("profileuserid");
            else if(isFrom.equalsIgnoreCase("favouritevideos"))
                profileuserid=getIntent().getStringExtra("profileuserid");
            else if(isFrom.equalsIgnoreCase("category"))
                categoryid=getIntent().getStringExtra("categoryid");
            else if(isFrom.equalsIgnoreCase("NearLocation"))
            {
                categoryid=getIntent().getStringExtra("categoryid");
                lattitude=getIntent().getStringExtra("lattitude");
                longnitude=getIntent().getStringExtra("longnitude");
            }
            //setDataToContainer();

            if(isFrom.equalsIgnoreCase("hashTag")) {
                setHashtagsDataToContainer("10", "0");
            }
            else if(isFrom.equalsIgnoreCase("favouritevideos")) {
                if(videoslist.size()>0)
                {
                    modelArrayList.addAll(videoslist);
                    preload(videoslist);
                    adapter.setBasepaths(getIntent().getStringExtra("videosbasepath"), getIntent().getStringExtra("musicbasepath"));
                    adapter.notifyDataSetChanged();
                    container.scrollToPosition(getIntent().getIntExtra("position",0));
                }
                else
                   setFavouriteVideosToContainer("video", "10", "0");
            }
            else if(isFrom.equalsIgnoreCase("notification")) {
                if(videoslist.size()>0)
                {
                    modelArrayList.addAll(videoslist);
                    preload(videoslist);
                    adapter.setBasepaths(getIntent().getStringExtra("videosbasepath"), getIntent().getStringExtra("musicbasepath"));
                    adapter.notifyDataSetChanged();
                    container.scrollToPosition(getIntent().getIntExtra("position",0));
                }
            }
            else if(isFrom.equalsIgnoreCase("profilevideos")) {
                if (videoslist.size() > 0) {
                    modelArrayList.addAll(videoslist);
                    preload(videoslist);
                    adapter.setBasepaths(getIntent().getStringExtra("videosbasepath"), getIntent().getStringExtra("musicbasepath"));
                    adapter.notifyDataSetChanged();
                    container.scrollToPosition(getIntent().getIntExtra("position",0));
                } else
                    setProfilevideosDataToContainer("10", "0");
            }
            else if(isFrom.equalsIgnoreCase("category"))
                setDataTrendingToContainer("10","0");
            else if(isFrom.equalsIgnoreCase("NearLocation"))
                setDataTrendingToContainer("10","0");
        }
        else
            Utils.callToast(PlayvideosCategorywise_Activity.this,getResources().getString(R.string.internet_toast));

        container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
                Log.e("pos"+pos,"numitems "+numItems);
                if(pos>=0) {
                    adapter.notifyDataSetChanged();
                    if ((pos + 1) >= numItems) {
                        if (connectionDetector.isConnectingToInternet()) {
                            if (isFrom.equalsIgnoreCase("hashTag"))
                                setHashtagsDataToContainer("10", "" + numItems);
                            else if (isFrom.equalsIgnoreCase("sounds"))
                                setSongsDataToContainer("10", "" + numItems);
                            else if (isFrom.equalsIgnoreCase("favouritevideos"))
                                setFavouriteVideosToContainer("video", "10", "" + numItems);
                            else if (isFrom.equalsIgnoreCase("profilevideos"))
                                setProfilevideosDataToContainer("10", "" + numItems);
                            else if (isFrom.equalsIgnoreCase("category"))
                                setDataTrendingToContainer("10", "" + numItems);
                        } else
                            Utils.callToast(PlayvideosCategorywise_Activity.this, getResources().getString(R.string.internet_toast));
                    }
                }
            }
        });
    }

    private void setDataTrendingToContainer(String limit,String offset){
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).videosListingService(userid,limit,offset,categoryid,lattitude,longnitude);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            preload(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                        }
                    } else {
                        if(modelArrayList.size()==0)
                            Utils.callToast(PlayvideosCategorywise_Activity.this, body.getMessage());
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.notifyDataSetChanged();
                    Utils.callToast(PlayvideosCategorywise_Activity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                adapter.notifyDataSetChanged();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setProfilevideosDataToContainer(String limit,String offset){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).profileVideos(profileuserid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            preload(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                           // Utils.callToast(PlayvideosCategorywise_Activity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(PlayvideosCategorywise_Activity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setFavouriteVideosToContainer(String type,String limit,String offset){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).listFavouritesVideos(profileuserid,type,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            preload(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                            //Utils.callToast(PlayvideosCategorywise_Activity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(PlayvideosCategorywise_Activity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setSongsDataToContainer(String limit,String offset){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).videoBySong(userid,songid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            preload(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                            Utils.callToast(PlayvideosCategorywise_Activity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(PlayvideosCategorywise_Activity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setHashtagsDataToContainer(String limit,String offset){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).videoByHashTag(userid,hashtag,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            preload(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                           // Utils.callToast(PlayvideosCategorywise_Activity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(PlayvideosCategorywise_Activity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setLikeUnlike(final SimplePlayerViewHolder holder, String userid, String videoid, final int pos){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).likeUnlikeVideoService(userid,videoid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    adapter.updateLike(holder,pos);
                }
                else {
                    //Utils.callToast(getActivity(),body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setFollowUnFollow(final SimplePlayerViewHolder holder, String userid, String fromid, final int pos){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).followUnfollowUserService(userid,fromid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    adapter.updateFollows(holder,pos);
                }
                else {
                    //Utils.callToast(getActivity(),body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("follow onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onLikeClicked(SimplePlayerViewHolder holder, String videoid, int pos) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            setLikeUnlike(holder,sessionManagement.getValueFromPreference(SessionManagement.USERID),videoid,pos);
        }
        else {
            startActivity(new Intent(PlayvideosCategorywise_Activity.this, LoginLandingActivity.class));
        }
    }

    @Override
    public void onFavouriteClicked(SimplePlayerViewHolder holder, String videoid, int pos) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            addTofavourites(holder,pos,sessionManagement.getValueFromPreference(SessionManagement.USERID),"video",videoid);
        }
        else {
            startActivity(new Intent(PlayvideosCategorywise_Activity.this, LoginLandingActivity.class));
        }
    }

    @Override
    public void onFollowClicked(SimplePlayerViewHolder holder, String fromid, int pos) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            setFollowUnFollow(holder,sessionManagement.getValueFromPreference(SessionManagement.USERID),fromid,pos);
        }
        else {
            startActivity(new Intent(PlayvideosCategorywise_Activity.this, LoginLandingActivity.class));
        }
    }

    @Override
    public void onProfileClicked(VideoModel model) {
        videoUserid=model.getUserId();
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN)) {
            if(checkingPermissionAreEnabledOrNot()) {
                Intent intent = new Intent(PlayvideosCategorywise_Activity.this, UserProfileActivity.class);
                intent.putExtra("othersuserid",videoUserid);
                startActivity(intent);
            }
            else {
                requestMultiplePermission(300);
            }
        }
        else {
            startActivity(new Intent(PlayvideosCategorywise_Activity.this, LoginLandingActivity.class));
        }
    }

    @Override
    public void onCommentsClicked(SimplePlayerViewHolder holder,int pos,String videoid,String commentscount) {

        this.holder = holder;
        this.videoid = videoid;
        this.position = pos;

        CommentsActivity.commentsCount=null;
        Intent intent=new Intent(PlayvideosCategorywise_Activity.this,CommentsActivity.class);
        intent.putExtra("videoid",videoid);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(CommentsActivity.commentsCount!=null)
        {
            if(adapter!=null)
                adapter.updateCommentsCount(holder,position,CommentsActivity.commentsCount);
            CommentsActivity.commentsCount=null;
        }

        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
        if(!FileUploadService.isProcessing || uploadpercentagelength>6) {
            isUploading=false;
            uploadpercentagelength=0;
        }

        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onShareClicked(final String videoServerurl,SimplePlayerViewHolder holder,String videoid,int pos) {
        this.userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        if(userid!=null) {
            this.videoServerUrl = videoServerurl;
            this.holder = holder;
            this.videoid = videoid;
            this.position = pos;

            if (checkingPermissionAreEnabledOrNot())
                startDownloadAndSharing();
            else
                requestMultiplePermission(120);
        }
        else {
            startActivity(new Intent(PlayvideosCategorywise_Activity.this,LoginLandingActivity.class));
        }
    }

    public void startDownloadAndSharing()
    {
        new Thread(new Runnable() {
            public void run() {
                downloadFile(videoServerUrl,getVideoDownloadSharepath());
            }
        }).start();
    }

    @Override
    public void onAbuseClicked(VideoModel model) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN)) {
            Intent abuse = new Intent(PlayvideosCategorywise_Activity.this, AbuseSelectionActivity.class);
            abuse.putExtra("videoid",model.getId());
            startActivity(abuse);
        }
        else
            startActivity(new Intent(PlayvideosCategorywise_Activity.this,LoginLandingActivity.class));
    }

    @Override
    public void onHashtagclicked(String hashtag) {

        if(isFrom.equalsIgnoreCase("hashTag"))
        {

        }
        else {
            Intent intent=new Intent(PlayvideosCategorywise_Activity.this, PlayvideosCategorywise_Activity.class);
            intent.putExtra("isFrom","hashTag");
            intent.putExtra("hashTag",hashtag);
            intent.putParcelableArrayListExtra("videos",new ArrayList<VideoModel>());
            intent.putExtra("position",0);
            intent.putExtra("videosbasepath","");
            intent.putExtra("musicbasepath","");
            startActivity(intent);
            finish();
        }
    }

    public void shareVideo(String videopath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("*/*");
        File videofiletoshare = new File(videopath);
        Uri uri = Uri.fromFile(videofiletoshare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivityForResult(Intent.createChooser(share, "Share Video!"),100);
    }

    public void downloadFile(String videoServerUrl,String path){

        RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).downloadFileWithDynamicUrlSync(videoServerUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Integer, String>() {
                        ProgressDialog progressDialog=new ProgressDialog(PlayvideosCategorywise_Activity.this);
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressDialog.show();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            Utils.callToast(PlayvideosCategorywise_Activity.this,"Saved successfully");
                            progressDialog.dismiss();
                            shareVideo(path);
                        }

                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            super.onProgressUpdate(values);
                            progressDialog.setProgress(values[0]);
                        }


                        @Override
                        protected String doInBackground(Void... voids) {
                            ResponseBody body=response.body();
                            try {
                                // todo change the file location/name according to your needs
                                File futureStudioIconFile = new File(path);

                                InputStream inputStream = null;
                                OutputStream outputStream = null;

                                try {
                                    byte[] fileReader = new byte[4096];

                                    long fileSize = body.contentLength();
                                    long fileSizeDownloaded = 0;

                                    inputStream = body.byteStream();
                                    int available= inputStream.available();
                                    outputStream = new FileOutputStream(futureStudioIconFile);
                                    int size=0;
                                    while (true) {
                                        int read = inputStream.read(fileReader);
                                        size++;

                                        if (read == -1) {
                                            Log.e("bytes "+available,""+size*4096);
                                            break;
                                        }

                                        outputStream.write(fileReader, 0, read);

                                        if(fileSizeDownloaded>0&&fileSize>0)
                                            publishProgress((int)(fileSizeDownloaded*100/fileSize));
                                        Log.e("failed","download failed");
                                    }

                                    outputStream.flush();

                                    return futureStudioIconFile.getAbsolutePath();
                                } catch (IOException e) {
                                    return null;
                                } finally {
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }

                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                }
                            } catch (IOException e) {
                                return null;
                            }
                        }
                    }.execute();
                } else {
                    Log.e("failed", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.callToast(PlayvideosCategorywise_Activity.this,"failed");
            }
        });
    }

    public String getVideoDownloadSharepath()
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/"+getResources().getString(R.string.app_name)+"shares";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, getResources().getString(R.string.app_name)+"shares"+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+".mp4");
        return file.getAbsolutePath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100)
        {
            if(resultCode==RESULT_OK) {
                Log.e("share done", "share done");
                updateSharecount(holder,userid,videoid,position);
            }
            else
                Log.e("share cancel","share cancel");
        }
    }

    public boolean checkingVideoPermissionAreEnabledOrNot() {
        int camera = ContextCompat.checkSelfPermission(PlayvideosCategorywise_Activity.this, CAMERA);
        int write = ContextCompat.checkSelfPermission(PlayvideosCategorywise_Activity.this, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(PlayvideosCategorywise_Activity.this, RECORD_AUDIO);
        return camera == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED && read==PackageManager.PERMISSION_GRANTED;
    }

    private void requestVideoMultiplePermission() {

        ActivityCompat.requestPermissions(PlayvideosCategorywise_Activity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        RECORD_AUDIO
                }, 200);
    }

    public boolean checkingPermissionAreEnabledOrNot() {
        int write = ContextCompat.checkSelfPermission(PlayvideosCategorywise_Activity.this, WRITE_EXTERNAL_STORAGE);
        return write == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission(int requestcode) {
        ActivityCompat.requestPermissions(PlayvideosCategorywise_Activity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE
                }, requestcode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 120:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=1)
                    {
                        if(checkingPermissionAreEnabledOrNot())
                        {
                            startDownloadAndSharing();
                        }
                        else
                            requestMultiplePermission(120);
                    }
                } else {
                    requestMultiplePermission(120);
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=1)
                    {
                        if(checkingPermissionAreEnabledOrNot())
                        {
                            Intent intent = new Intent(PlayvideosCategorywise_Activity.this, UserProfileActivity.class);
                            intent.putExtra("othersuserid",videoUserid);
                            startActivity(intent);
                        }
                        else
                            requestMultiplePermission(300);
                    }
                } else {
                    requestMultiplePermission(300);
                }
                break;
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=3)
                    {
                        if(checkingVideoPermissionAreEnabledOrNot())
                        {
                            if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                                startActivity(new Intent(PlayvideosCategorywise_Activity.this,MakingVideoActivity.class));
                            else
                                startActivity(new Intent(PlayvideosCategorywise_Activity.this, LoginLandingActivity.class));
                        }
                        else
                            requestVideoMultiplePermission();
                    }
                } else {
                    requestVideoMultiplePermission();
                }
                break;
        }
    }

    private void updateSharecount(final SimplePlayerViewHolder holder, String userid, String videoid, final int pos){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).videoShareService(userid,videoid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    adapter.updateShares(holder,pos);
                }
                else {
                    Utils.callToast(PlayvideosCategorywise_Activity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoshare onFailure",""+t.getMessage());
            }
        });
    }

    private void addTofavourites(final SimplePlayerViewHolder holder,final int pos,String userid, String type, String favouriteid){
        Utils.showDialog(PlayvideosCategorywise_Activity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(PlayvideosCategorywise_Activity.this).addFavourite(userid,type,favouriteid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    adapter.updateFavourite(holder,pos);
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

    @Override
    public void onDubsmashClicked(VideoModel model) {
        if(!isUploading) {
            if (checkingVideoPermissionAreEnabledOrNot()) {
                if (sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                    startActivity(new Intent(PlayvideosCategorywise_Activity.this, MakingVideoActivity.class));
                else
                    startActivity(new Intent(PlayvideosCategorywise_Activity.this, LoginLandingActivity.class));
            } else
                requestVideoMultiplePermission();
        }
        else {
            Utils.callToast(PlayvideosCategorywise_Activity.this,"Your previous video is uploading. Please wait!");
        }
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            String path = intent.getStringExtra("videopath");
            if(result.equalsIgnoreCase("101")) {
                Utils.callToast(PlayvideosCategorywise_Activity.this,"Your video uploaded successfully");
                isUploading=false;
            }
            else if(result.trim().length()>5)
            {
                isUploading=false;
                Utils.callToast(PlayvideosCategorywise_Activity.this,"Your file uploading failed. Please try again from drafts");
            }
            else {
                isUploading=true;
            }
            uploadpercentagelength=result.length();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    private void preload(List<VideoModel> videoModels) {
        for(VideoModel videoModel:videoModels)
        {
            try {
                String video=videoModel.getVideo();
                if(video!=null && video.trim().length()>4)
                {
                    String jpeg=video.substring(0,video.length()-4);
                    jpeg=jpeg+"-image.jpeg";
                    Picasso.with(PlayvideosCategorywise_Activity.this).load(ApiUrls.VIDEOTHUMBNAIL + jpeg).fetch();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
