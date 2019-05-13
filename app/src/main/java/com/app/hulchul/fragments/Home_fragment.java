package com.app.hulchul.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hulchul.R;
import com.app.hulchul.activities.AbuseSelectionActivity;
import com.app.hulchul.activities.CommentsActivity;
import com.app.hulchul.activities.HashtagSearchresultsActivity;
import com.app.hulchul.activities.LoginLandingActivity;
import com.app.hulchul.activities.MainActivity;
import com.app.hulchul.activities.MakingVideoActivity;
import com.app.hulchul.activities.UserProfileActivity;
import com.app.hulchul.adapters.SimpleAdapter;
import com.app.hulchul.adapters.SimplePlayerViewHolder;
import com.app.hulchul.fileuploadservice.FileUploadService;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.model.VideosListingResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.RecyclerTouchListener;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import static android.app.Activity.RESULT_OK;

public class Home_fragment extends Fragment implements View.OnClickListener,SimpleAdapter.VideoActionsListener,MainActivity.onFilePermissionListenerForFragment{

    @BindView(R.id.tv_recommended)
    TextView tv_recommended;
    @BindView(R.id.tv_trending)
    TextView tv_trending;
    @BindView(R.id.player_container)
    Container container;
    @BindView(R.id.tv_nofollowtext)
    TextView tv_nofollowtext;
    private Dialog dialog;
    private boolean isRecommended=false;
    private int totalcount=-1;

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
    private int uploadpercentagelength;
    private boolean isUploading=false;

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

        tv_recommended.setTypeface(Typeface.DEFAULT);
        tv_trending.setTypeface(Typeface.DEFAULT_BOLD);

        layoutManager = new LinearLayoutManager(getActivity());
        SnapHelper snapHelper = new PagerSnapHelper();
        container.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(container);

        adapter = new SimpleAdapter(getActivity(),modelArrayList);
        adapter.setVideoActionsListener(this);
        container.setAdapter(adapter);

        if(connectionDetector.isConnectingToInternet())
        {
            modelArrayList.clear();
            setDataTrendingToContainer("10","0");
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

        container.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if(numItems<totalcount) {
                        if (connectionDetector.isConnectingToInternet()) {
                            if (isRecommended)
                                setRecommendedContainer("10", "" + numItems);
                            else
                                setDataTrendingToContainer("10", "" + numItems);
                        } else
                            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                    }

                }
            }
        });
        container.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), container,adapter));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_recommended:
                if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null) {
                    //Utils.callToast(getContext(), "Recommend");
                    tv_recommended.setTypeface(Typeface.DEFAULT_BOLD);
                    tv_trending.setTypeface(Typeface.DEFAULT);
                    if (connectionDetector.isConnectingToInternet()) {
                        isRecommended = true;
                        modelArrayList.clear();
                        totalcount=-1;
                        setRecommendedContainer("10", "0");
                    } else
                        Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                }
                else
                    startActivity(new Intent(getActivity(),LoginLandingActivity.class));

                break;
            case R.id.tv_trending:
                tv_recommended.setTypeface(Typeface.DEFAULT);
                tv_trending.setTypeface(Typeface.DEFAULT_BOLD);
                tv_nofollowtext.setVisibility(View.GONE);
                if (connectionDetector.isConnectingToInternet()) {
                    isRecommended = false;
                    modelArrayList.clear();
                    totalcount=-1;
                    setDataTrendingToContainer("10", "0");
                } else
                    Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));

                break;
        }
    }

    private void setRecommendedContainer(String limit,String offset){
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
           userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        Utils.showDialog(getContext());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getContext()).userFollowerVideos(userid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                            tv_nofollowtext.setVisibility(View.GONE);
                        }
                    } else {
                        if(modelArrayList.size()==0) {
                            tv_nofollowtext.setVisibility(View.VISIBLE);
                            Utils.callToast(getActivity(), body.getMessage());
                        }
                    }
                    if(body.getTotalcount()!=null)
                      totalcount=body.getTotalcount();
                    else
                        totalcount=-1;
                    adapter.notifyDataSetChanged();
                }
                else {
                    Utils.callToast(getActivity(),"null response came");
                    adapter.notifyDataSetChanged();
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

    private void setDataTrendingToContainer(String limit,String offset){
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        Utils.showDialog(getContext());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getContext()).videosListingService(userid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                        }
                    } else {
                        if(modelArrayList.size()==0)
                            Utils.callToast(getActivity(), body.getMessage());
                    }
                    totalcount=body.getTotalcount();
                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.notifyDataSetChanged();
                    Utils.callToast(getActivity(),"null response came");
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

    private void setFollowUnFollow(final SimplePlayerViewHolder holder, String userid, String fromid, final int pos){
        Utils.showDialog(getContext());
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(getContext()).followUnfollowUserService(userid,fromid);
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
    public void onFavouriteClicked(SimplePlayerViewHolder holder, String videoid, int pos) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            addTofavourites(holder,pos,sessionManagement.getValueFromPreference(SessionManagement.USERID),"video",videoid);
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
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN)) {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            intent.putExtra("othersuserid", model.getUserId());
            startActivity(intent);
        }
        else {
            startActivity(new Intent(getActivity(), LoginLandingActivity.class));
        }
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

        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myLocalBroadcastReceiver, intentFilter);
        if(!FileUploadService.isProcessing || uploadpercentagelength>6) {
            isUploading=false;
            uploadpercentagelength=0;
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
        new Thread(new Runnable() {
            public void run() {
                downloadFile(videoServerUrl,getVideoDownloadSharepath());
            }
        }).start();
    }

    @Override
    public void onAbuseClicked() {
        Intent abuse=new Intent(getActivity(), AbuseSelectionActivity.class);
        abuse.putExtra("common","Abuse Reason selection screen");
        startActivity(abuse);
    }

    @Override
    public void onHashtagclicked(String hashtag) {
        Intent intent=new Intent(getActivity(), HashtagSearchresultsActivity.class);
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

    public String getVideoDownloadSharepath()
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchulshares";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "hulchulshares"+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+".mp4");
        return file.getAbsolutePath();
    }

    public void downloadFile(String videoServerUrl,String path) {

        RetrofitApis.Factory.createTemp(getContext()).downloadFileWithDynamicUrlSync(videoServerUrl).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        new AsyncTask<Void, Integer, String>() {
                            ProgressDialog progressDialog=new ProgressDialog(getActivity());
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                               progressDialog.show();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                Utils.callToast(getActivity(),"Saved successfully");
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
                    Utils.callToast(getActivity(),"failed");
                }
            });
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

    public boolean checkingVideorecordPermissionAreEnabledOrNot() {
        int camera = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        int write = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);
        return camera == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED && read==PackageManager.PERMISSION_GRANTED;
    }

    private void requestVideorecordPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        RECORD_AUDIO
                }, 200);
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

            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=3)
                    {
                        if(checkingVideorecordPermissionAreEnabledOrNot())
                        {
                            if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                                startActivity(new Intent(getActivity(),MakingVideoActivity.class));
                            else
                                startActivity(new Intent(getActivity(), LoginLandingActivity.class));
                        }
                        else
                            requestVideorecordPermission();
                    }
                } else {
                    requestVideorecordPermission();
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

    private void addTofavourites(final SimplePlayerViewHolder holder,final int pos,String userid, String type, String favouriteid){
        Utils.showDialog(getActivity());
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(getActivity()).addFavourite(userid,type,favouriteid);
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
            if (checkingVideorecordPermissionAreEnabledOrNot()) {
                if (sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                    startActivity(new Intent(getActivity(), MakingVideoActivity.class));
                else
                    startActivity(new Intent(getActivity(), LoginLandingActivity.class));
            } else
                requestVideorecordPermission();
        }
        else {
            Utils.callToast(getActivity(),"Your previous video is uploading. Please wait!");
        }
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            String path = intent.getStringExtra("videopath");
            if(result.equalsIgnoreCase("101")) {
                Utils.callToast(getActivity(),"Your video uploaded successfully");
                isUploading=false;
            }
            else if(result.trim().length()>5)
            {
                isUploading=false;
                uploadpercentagelength=0;
                Utils.callToast(getActivity(),"Your file uploading failed. Please try again from drafts");
            }
            else {
                isUploading=true;
            }
            uploadpercentagelength=result.length();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myLocalBroadcastReceiver);
    }
}
