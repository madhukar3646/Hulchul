package com.app.hulchul.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hulchul.CommonEmptyActivity;
import com.app.hulchul.R;
import com.app.hulchul.activities.AbuseSelectionActivity;
import com.app.hulchul.activities.CommentsActivity;
import com.app.hulchul.activities.LoginLandingActivity;
import com.app.hulchul.activities.MainActivity;
import com.app.hulchul.activities.MakingVideoActivity;
import com.app.hulchul.activities.ServerSoundsCompletelistingActivity;
import com.app.hulchul.activities.UserProfileActivity;
import com.app.hulchul.adapters.SimpleAdapter;
import com.app.hulchul.adapters.SimplePlayerViewHolder;
import com.app.hulchul.model.ServerSong;
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
import static android.app.Activity.RESULT_OK;

public class Home_fragment extends Fragment implements View.OnClickListener,SimpleAdapter.VideoActionsListener,MainActivity.onFilePermissionListenerForFragment{

    @BindView(R.id.tv_recommended)
    TextView tv_recommended;
    @BindView(R.id.tv_trending)
    TextView tv_trending;
    @BindView(R.id.player_container)
    Container container;
    private Dialog dialog;

    /*String urls[]={"http://testingmadesimple.org/samplevideo.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
    };*/
    SimpleAdapter adapter;
    LinearLayoutManager layoutManager;
    private ArrayList<VideoModel> modelArrayList=new ArrayList<>();

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String videoServerUrl;

    private SimplePlayerViewHolder holder;
    private String userid,videoid;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_fragment, container, false);
        ButterKnife.bind(this,view);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        Log.e("user id","is "+sessionManagement.getValueFromPreference(SessionManagement.USERID));

        tv_recommended.setOnClickListener(this);
        tv_trending.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(getActivity());
        SnapHelper snapHelper = new PagerSnapHelper();
        container.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(container);

        adapter = new SimpleAdapter(getActivity(),modelArrayList);
        adapter.setVideoActionsListener(this);
        container.setAdapter(adapter);

        if(connectionDetector.isConnectingToInternet())
        {
            setDataToContainer();
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_recommended:
                Utils.callToast(getContext(),"Recommend");
                tv_recommended.setTypeface(Typeface.DEFAULT_BOLD);
                tv_trending.setTypeface(Typeface.DEFAULT);
                if(connectionDetector.isConnectingToInternet())
                {
                    setDataToContainer();
                }
                else
                    Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

                break;
            case R.id.tv_trending:
                Utils.callToast(getContext(),"trending");
                tv_recommended.setTypeface(Typeface.DEFAULT);
                tv_trending.setTypeface(Typeface.DEFAULT_BOLD);
                break;
        }
    }

    private void setDataToContainer(){
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
           userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        Utils.showDialog(getContext());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getContext()).videosListingService(userid);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.clear();
                            modelArrayList.addAll(body.getVideos());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Utils.callToast(getActivity(), body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getActivity(),"null response came");
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
        Utils.showDialog(getContext());
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(getContext()).likeUnlikeVideoService(userid,videoid);
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

    private void setFollowUnFollow(final SimplePlayerViewHolder holder, String userid, String videoid, final int pos){
        Utils.showDialog(getContext());
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(getContext()).followUnfollowUserService(userid,videoid);
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
    public void onLikeClicked(SimplePlayerViewHolder holder,String videoid,int pos) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
          setLikeUnlike(holder,sessionManagement.getValueFromPreference(SessionManagement.USERID),videoid,pos);
        }
        else {
            startActivity(new Intent(getActivity(), LoginLandingActivity.class));
        }
    }

    @Override
    public void onFollowClicked(SimplePlayerViewHolder holder,String videoid,int pos) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            setFollowUnFollow(holder,sessionManagement.getValueFromPreference(SessionManagement.USERID),videoid,pos);
        }
        else {
            startActivity(new Intent(getActivity(), LoginLandingActivity.class));
        }
    }

    @Override
    public void onProfileClicked(VideoModel model) {
        startActivity(new Intent(getActivity(), UserProfileActivity.class));
    }

    @Override
    public void onCommentsClicked(SimplePlayerViewHolder holder,int pos,String videoid,String commentscount) {

        this.holder = holder;
        this.videoid = videoid;
        this.position = pos;

        CommentsActivity.commentsCount=null;
        Intent intent=new Intent(getActivity(),CommentsActivity.class);
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
            getContext().startActivity(new Intent(getActivity(),LoginLandingActivity.class));
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
        Intent abuse=new Intent(getActivity(), AbuseSelectionActivity.class);
        abuse.putExtra("common","Abuse Reason selection screen");
        startActivity(abuse);
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
            getActivity().runOnUiThread(new Runnable() {
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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(){
        dialog = new Dialog(getActivity(),
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
        int write = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        return write == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
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
        Utils.showDialog(getContext());
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(getContext()).videoShareService(userid,videoid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    adapter.updateShares(holder,pos);
                }
                else {
                    Utils.callToast(getActivity(),body.getMessage());
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
