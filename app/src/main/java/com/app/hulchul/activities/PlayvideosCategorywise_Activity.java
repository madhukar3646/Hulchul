package com.app.hulchul.activities;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.hulchul.R;
import com.app.hulchul.adapters.SimpleAdapter;
import com.app.hulchul.adapters.SimplePlayerViewHolder;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.model.VideosListingResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PlayvideosCategorywise_Activity extends AppCompatActivity implements SimpleAdapter.VideoActionsListener{

    @BindView(R.id.player_container)
    Container container;

    SimpleAdapter adapter;
    LinearLayoutManager layoutManager;
    private ArrayList<VideoModel> modelArrayList=new ArrayList<>();

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String videoServerUrl;

    private SimplePlayerViewHolder holder;
    private String userid,videoid;
    private int position;
    private Dialog dialog;

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
            modelArrayList.addAll(getIntent().getParcelableArrayListExtra("videos"));
            adapter.setBasepaths(getIntent().getStringExtra("videosbasepath"), getIntent().getStringExtra("musicbasepath"));
            adapter.notifyDataSetChanged();
            container.scrollToPosition(getIntent().getIntExtra("position",0));
            //setDataToContainer();
        }
        else
            Utils.callToast(PlayvideosCategorywise_Activity.this,getResources().getString(R.string.internet_toast));
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
        startActivity(new Intent(PlayvideosCategorywise_Activity.this, UserProfileActivity.class));
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
                requestMultiplePermission();
        }
        else {
            startActivity(new Intent(PlayvideosCategorywise_Activity.this,LoginLandingActivity.class));
        }
    }

    public void startDownloadAndSharing()
    {
        showProgress();
        new Thread(new Runnable() {
            public void run() {
                downloadFile(videoServerUrl);
            }
        }).start();
    }

    @Override
    public void onAbuseClicked() {
        Intent abuse=new Intent(PlayvideosCategorywise_Activity.this, AbuseSelectionActivity.class);
        abuse.putExtra("common","Abuse Reason selection screen");
        startActivity(abuse);
    }

    @Override
    public void onHashtagclicked(String hashtag) {

        Intent intent=new Intent(PlayvideosCategorywise_Activity.this, HashtagSearchresultsActivity.class);
        intent.putExtra("hashtag",hashtag);
        startActivity(intent);
    }

    public void shareVideo(String videopath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("*/*");
        File videofiletoshare = new File(videopath);
        Uri uri = Uri.fromFile(videofiletoshare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivityForResult(Intent.createChooser(share, "Share Video!"),100);
    }

    void downloadFile(String serverFileUrl){
        try {
            URL url = new URL(serverFileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            File file = new File(getVideoDownloadSharepath());

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    shareVideo(getVideoDownloadSharepath());
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err){
       runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(PlayvideosCategorywise_Activity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(){
        dialog = new Dialog(PlayvideosCategorywise_Activity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.progressdialog_update);
        dialog.setCancelable(false);
        dialog.show();
    }

    public String getVideoDownloadSharepath()
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchulshares";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "hulchulshares" + ".mp4");
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

    public boolean checkingPermissionAreEnabledOrNot() {
        int write = ContextCompat.checkSelfPermission(PlayvideosCategorywise_Activity.this, WRITE_EXTERNAL_STORAGE);
        return write == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission() {
        ActivityCompat.requestPermissions(PlayvideosCategorywise_Activity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE
                }, 120);
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
                            requestMultiplePermission();
                    }
                } else {
                    requestMultiplePermission();
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
}
