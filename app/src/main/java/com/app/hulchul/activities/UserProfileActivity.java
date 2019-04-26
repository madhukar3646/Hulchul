package com.app.hulchul.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.HashtagsGridAdapter;
import com.app.hulchul.model.ProfileViewdata;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.model.VideosListingResponse;
import com.app.hulchul.model.ViewProfileResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ApiUrls;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener,HashtagsGridAdapter.OnHashtagItemClickListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.iv_unfollow)
    ImageView iv_unfollow;
    @BindView(R.id.layout_follow)
    RelativeLayout layout_follow;
    @BindView(R.id.layout_messageunfollow)
    RelativeLayout layout_messageunfollow;
    @BindView(R.id.tv_following)
    TextView tv_following;
    @BindView(R.id.tv_followers)
    TextView tv_followers;
    @BindView(R.id.tv_hearts)
    TextView tv_hearts;
    @BindView(R.id.tv_friends)
    TextView tv_friends;
    @BindView(R.id.tv_videos)
    TextView tv_videos;
    @BindView(R.id.tv_biodata)
    TextView tv_biodata;
    @BindView(R.id.layout_yourvideos)
    LinearLayout layout_yourvideos;
    @BindView(R.id.layout_hearts)
    LinearLayout layout_hearts;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;

    @BindView(R.id.iv_myvideo)
    ImageView iv_myvideo;
    @BindView(R.id.tv_myvideos)
    TextView tv_myvideos;
    @BindView(R.id.iv_myhearts)
    ImageView iv_myhearts;
    @BindView(R.id.tv_myhearts)
    TextView tv_myhearts;

    @BindView(R.id.nestedsrollview)
    NestedScrollView nestedsrollview;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    private SessionManagement sessionManagement;
    private ConnectionDetector connectionDetector;
    private HashtagsGridAdapter profileadapter,likedadapter;
    private ProfileViewdata viewdata;
    GridLayoutManager gridLayoutManager;
    private boolean isProfilevideos=true;
    private String othersuserid="",userid="",videosbasepath,musicbasepath;
    private ArrayList<VideoModel> profilevideoslist=new ArrayList<>();
    private ArrayList<VideoModel> likedvideoslist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        othersuserid=getIntent().getStringExtra("othersuserid");
        connectionDetector=new ConnectionDetector(UserProfileActivity.this);
        sessionManagement=new SessionManagement(UserProfileActivity.this);
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        layout_follow.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        iv_unfollow.setOnClickListener(this);
        layout_yourvideos.setOnClickListener(this);
        layout_hearts.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        gridLayoutManager = new GridLayoutManager(UserProfileActivity.this,3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        recyclerview_videos.setNestedScrollingEnabled(false);

        profileadapter=new HashtagsGridAdapter(UserProfileActivity.this,profilevideoslist);
        profileadapter.setOnHashtagItemClickListener(this);
        likedadapter=new HashtagsGridAdapter(UserProfileActivity.this,likedvideoslist);
        likedadapter.setOnHashtagItemClickListener(this);
        setClickableFocus(true);

        if(connectionDetector.isConnectingToInternet()) {
            viewProfile(userid,othersuserid);
        }
        else
            Utils.callToast(UserProfileActivity.this,getResources().getString(R.string.internet_toast));

        nestedsrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                        Log.e("pastvisible"+pastVisiblesItems,"total "+totalItemCount+" visible "+visibleItemCount);
                        if(connectionDetector.isConnectingToInternet())
                        {
                            if(visibleItemCount+pastVisiblesItems>=totalItemCount)
                            {
                                if(isProfilevideos)
                                    setProfilevideosDataToContainer("20",""+totalItemCount);
                                else
                                    setLikedvideosDataToContainer("20",""+totalItemCount);
                            }
                        }
                        else
                            Utils.callToast(UserProfileActivity.this, getResources().getString(R.string.internet_toast));
                    }
                }
            }
        });
    }

    private void setClickableFocus(boolean isMyvideos)
    {
        if(isMyvideos) {
            iv_myvideo.setImageResource(R.mipmap.list_video_black);
            tv_myvideos.setTextColor(Color.BLACK);
            iv_myhearts.setImageResource(R.mipmap.hearts_gray);
            tv_myhearts.setTextColor(Color.GRAY);
            recyclerview_videos.setAdapter(profileadapter);
            isProfilevideos=true;
            if(profilevideoslist.size()==0)
                tv_nodata.setVisibility(View.VISIBLE);
            else
                tv_nodata.setVisibility(View.GONE);
        }
        else {
            iv_myvideo.setImageResource(R.mipmap.list_video_gray);
            tv_myvideos.setTextColor(Color.GRAY);
            iv_myhearts.setImageResource(R.mipmap.hearts_black);
            tv_myhearts.setTextColor(Color.BLACK);
            recyclerview_videos.setAdapter(likedadapter);
            isProfilevideos=false;
            if(likedvideoslist.size()>0)
                tv_nodata.setVisibility(View.GONE);
            if(likedvideoslist.size()==0)
            {
                if(connectionDetector.isConnectingToInternet())
                    setLikedvideosDataToContainer("20","0");
                else
                    Utils.callToast(UserProfileActivity.this, getResources().getString(R.string.internet_toast));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_follow:
                if(!userid.equalsIgnoreCase(othersuserid)) {
                    if (connectionDetector.isConnectingToInternet())
                        setFollowUnFollow(userid, othersuserid);
                    else
                        Utils.callToast(UserProfileActivity.this, getResources().getString(R.string.internet_toast));
                }
                else
                    Utils.callToast(UserProfileActivity.this,"This is your profile only.");
                break;
            case R.id.tv_message:
                break;
            case R.id.iv_unfollow:
                if(!userid.equalsIgnoreCase(othersuserid)) {
                    if (connectionDetector.isConnectingToInternet())
                        setFollowUnFollow(userid, othersuserid);
                    else
                        Utils.callToast(UserProfileActivity.this, getResources().getString(R.string.internet_toast));
                }
                else
                    Utils.callToast(UserProfileActivity.this,"This is your profile only.");
                break;
            case R.id.layout_yourvideos:
                setClickableFocus(true);
                break;
            case R.id.layout_hearts:
                setClickableFocus(false);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void viewProfile(String userid,String othersuserid){
        Utils.showDialog(UserProfileActivity.this);
        Call<ViewProfileResponse> call= RetrofitApis.Factory.createTemp(UserProfileActivity.this).viewProfile(userid,othersuserid);
        call.enqueue(new Callback<ViewProfileResponse>() {
            @Override
            public void onResponse(Call<ViewProfileResponse> call, Response<ViewProfileResponse> response) {
                Utils.dismissDialog();
                ViewProfileResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        if(body.getData()!=null)
                            updateFields(body.getData());
                        setProfilevideosDataToContainer("20","0");
                    } else {
                        Utils.callToast(UserProfileActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(UserProfileActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<ViewProfileResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }

    private void updateFields(ProfileViewdata viewdata)
    {
        /*tv_name.setText("Satya demo");
        tv_title.setText("Satya demo");
        tv_following.setText("35 Following");
        tv_friends.setText("5 Friends");
        tv_hearts.setText("15 Hearts");
        tv_videos.setText("10 Videos");
        tv_followers.setText("15 Followers");*/

        if((viewdata.getFollwerstatus()!=null) && (viewdata.getFollwerstatus().equalsIgnoreCase("0")|| viewdata.getFollwerstatus().equalsIgnoreCase("null")))
        {
            layout_follow.setVisibility(View.VISIBLE);
            layout_messageunfollow.setVisibility(View.GONE);
        }
        else {
            layout_follow.setVisibility(View.GONE);
            layout_messageunfollow.setVisibility(View.VISIBLE);
        }

        tv_name.setText(viewdata.getFullName());
        tv_title.setText(viewdata.getFullName());
        if(viewdata.getFollowing()!=null)
            tv_following.setText(viewdata.getFollowing()+" Following");
        else
            tv_following.setText("0"+" Following");
        tv_friends.setText("5 Friends");
        tv_friends.setVisibility(View.GONE);
        if(viewdata.getLikes()!=null)
            tv_hearts.setText(viewdata.getLikes()+" Hearts");
        else
            tv_hearts.setText("0"+" Hearts");
        if(viewdata.getVideos()!=null)
            tv_videos.setText(viewdata.getVideos()+" Videos");
        else
            tv_videos.setText("0"+" Videos");
        if(viewdata.getFollowers()!=null)
            tv_followers.setText(viewdata.getFollowers()+" Followers");
        else
            tv_followers.setText("0"+" Followers");

        tv_biodata.setText(viewdata.getBioData());

        String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        String username,biodata;
        if(viewdata.getFullName()==null || viewdata.getFullName().equalsIgnoreCase("null"))
        {
            username="@User"+userid.substring(userid.length()-4);
            viewdata.setFullName(username);
            tv_name.setText(username);
            tv_title.setText(username);
        }

        if(viewdata.getBioData()==null || viewdata.getBioData().equalsIgnoreCase("null"))
        {
            biodata="No bio data yet";
            viewdata.setBioData(biodata);
        }

        Picasso.with(UserProfileActivity.this).load(ApiUrls.PROFILEBASEPATH+viewdata.getPhoto()).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(profile_image);

        this.viewdata=viewdata;
    }

    private void setProfilevideosDataToContainer(String limit,String offset){
        Utils.showDialog(UserProfileActivity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(UserProfileActivity.this).profileVideos(othersuserid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            profilevideoslist.addAll(body.getVideos());
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            profileadapter.setVideobasepath(videosbasepath);
                            profileadapter.notifyDataSetChanged();
                            tv_nodata.setVisibility(View.GONE);
                        }
                    } else {
                        if(profilevideoslist.size()==0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(UserProfileActivity.this, body.getMessage());
                        }
                    }
                }
                else {
                    Utils.callToast(UserProfileActivity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private void setLikedvideosDataToContainer(String limit,String offset){
        Utils.showDialog(UserProfileActivity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(UserProfileActivity.this).userLikeVideos(othersuserid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            likedvideoslist.addAll(body.getVideos());
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            likedadapter.setVideobasepath(videosbasepath);
                            likedadapter.notifyDataSetChanged();
                            tv_nodata.setVisibility(View.GONE);
                        }
                    } else {
                        if(likedvideoslist.size()==0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(UserProfileActivity.this, body.getMessage());
                        }
                    }
                }
                else {
                    Utils.callToast(UserProfileActivity.this,"null response came");
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
        Intent intent=new Intent(UserProfileActivity.this, PlayvideosCategorywise_Activity.class);
        if(isProfilevideos)
          intent.putExtra("isFrom","profilevideos");
        else
            intent.putExtra("isFrom","likedvideos");
        intent.putExtra("profileuserid",othersuserid);
        intent.putParcelableArrayListExtra("videos",discoverhashtagvideosList);
        intent.putExtra("position",pos);
        intent.putExtra("videosbasepath",videosbasepath);
        intent.putExtra("musicbasepath",musicbasepath);
        startActivity(intent);
    }

    private void setFollowUnFollow(String userid, String fromid){
        Utils.showDialog(UserProfileActivity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(UserProfileActivity.this).followUnfollowUserService(userid,fromid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    if(layout_follow.getVisibility()==View.VISIBLE)
                    {
                        layout_follow.setVisibility(View.GONE);
                        layout_messageunfollow.setVisibility(View.VISIBLE);
                    }
                    else {
                        layout_follow.setVisibility(View.VISIBLE);
                        layout_messageunfollow.setVisibility(View.GONE);
                    }
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
}
