package com.app.hulchul.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.CommonEmptyActivity;
import com.app.hulchul.R;
import com.app.hulchul.activities.DraftsActivity;
import com.app.hulchul.activities.EditProfileActivity;
import com.app.hulchul.activities.FavouritesActivity;
import com.app.hulchul.activities.LoginLandingActivity;
import com.app.hulchul.activities.SettingsActivity;
import com.app.hulchul.adapters.VideothumbnailsAdapter;
import com.app.hulchul.model.ProfileViewdata;
import com.app.hulchul.model.ProfilepicUpdateResponse;
import com.app.hulchul.model.ViewProfileResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ApiUrls;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Me_Fragment extends Fragment implements View.OnClickListener,VideothumbnailsAdapter.OnVideoSelectedListener{

    @BindView(R.id.iv_favourite)
    ImageView iv_favourite;
    @BindView(R.id.iv_addfriends)
    ImageView iv_addfriends;
    @BindView(R.id.iv_menuburger)
    ImageView iv_menuburger;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.layout_editprofile)
    RelativeLayout layout_editprofile;

    @BindView(R.id.tv_following)
    TextView tv_following;
    @BindView(R.id.tv_friends)
    TextView tv_friends;
    @BindView(R.id.tv_hearts)
    TextView tv_hearts;
    @BindView(R.id.tv_followers)
    TextView tv_followers;
    @BindView(R.id.tv_videos)
    TextView tv_videos;
    @BindView(R.id.tv_biodata)
    TextView tv_biodata;

    @BindView(R.id.layout_yourvideos)
    LinearLayout layout_yourvideos;
    @BindView(R.id.iv_myvideo)
    ImageView iv_myvideo;
    @BindView(R.id.tv_myvideos)
    TextView tv_myvideos;
    @BindView(R.id.layout_hearts)
    LinearLayout layout_hearts;
    @BindView(R.id.iv_myhearts)
    ImageView iv_myhearts;
    @BindView(R.id.tv_myhearts)
    TextView tv_myhearts;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;
    @BindView(R.id.layout_drafts)
    LinearLayout layout_drafts;
    @BindView(R.id.tv_drafts)
    TextView tv_drafts;

    private SessionManagement sessionManagement;
    private ConnectionDetector connectionDetector;
    private VideothumbnailsAdapter adapter;
    private ProfileViewdata viewdata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_me_, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());

        iv_favourite.setOnClickListener(this);
        iv_menuburger.setOnClickListener(this);
        iv_addfriends.setOnClickListener(this);
        layout_editprofile.setOnClickListener(this);
        layout_yourvideos.setOnClickListener(this);
        layout_hearts.setOnClickListener(this);
        layout_drafts.setOnClickListener(this);

        setClickableFocus(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        recyclerview_videos.setNestedScrollingEnabled(false);
        adapter=new VideothumbnailsAdapter(getContext());
        adapter.setOnVideoSelectedListener(this);
        recyclerview_videos.setAdapter(adapter);
        tv_drafts.setText(getDraftsCount()+" Drafts Videos");

        if(connectionDetector.isConnectingToInternet())
            viewProfile(sessionManagement.getValueFromPreference(SessionManagement.USERID));
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_favourite:
                if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                {
                    Intent intent=new Intent(getActivity(), FavouritesActivity.class);
                    startActivity(intent);
                }
                else {
                    startActivity(new Intent(getActivity(), LoginLandingActivity.class));
                }

                break;
            case R.id.iv_addfriends:
                if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                {
                    Intent intent=new Intent(getActivity(), CommonEmptyActivity.class);
                    intent.putExtra("common","Add Friends");
                    startActivity(intent);
                }
                else {
                    startActivity(new Intent(getActivity(), LoginLandingActivity.class));
                }

                break;
            case R.id.iv_menuburger:
                if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                }
                else {
                    startActivity(new Intent(getActivity(), LoginLandingActivity.class));
                }

                break;
            case R.id.layout_editprofile:
                Intent editprofile=new Intent(getActivity(), EditProfileActivity.class);
                if(viewdata!=null)
                {
                    editprofile.putExtra("name",viewdata.getFullName());
                    editprofile.putExtra("biodata",viewdata.getBioData());
                    editprofile.putExtra("profilephoto",viewdata.getPhoto());
                }
                startActivity(editprofile);
                break;
            case R.id.layout_yourvideos:
                setClickableFocus(true);
                break;
            case R.id.layout_hearts:
                setClickableFocus(false);
                break;
            case R.id.layout_drafts:
                if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                {
                    if(getDraftsCount()>0) {
                        Intent intent = new Intent(getActivity(), DraftsActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    startActivity(new Intent(getActivity(), LoginLandingActivity.class));
                }
                break;
        }
    }

    private void setClickableFocus(boolean isMyvideos)
    {
        if(isMyvideos) {
            iv_myvideo.setImageResource(R.mipmap.list_video_black);
            tv_myvideos.setTextColor(Color.BLACK);
            iv_myhearts.setImageResource(R.mipmap.hearts_gray);
            tv_myhearts.setTextColor(Color.GRAY);
        }
        else {
            iv_myvideo.setImageResource(R.mipmap.list_video_gray);
            tv_myvideos.setTextColor(Color.GRAY);
            iv_myhearts.setImageResource(R.mipmap.hearts_black);
            tv_myhearts.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onVideoSelected() {

    }

    private int getDraftsCount()
    {
        int draftscount=0;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchuldrafts";
        File dir = new File(file_path);
        if(dir.exists())
        {
            if(dir.listFiles().length>0)
                draftscount=dir.listFiles().length;
            else
                draftscount=0;
        }
       return draftscount;
    }

    private void viewProfile(String userid){
        Utils.showDialog(getActivity());
        Call<ViewProfileResponse> call= RetrofitApis.Factory.createTemp(getActivity()).viewProfile(userid);
        call.enqueue(new Callback<ViewProfileResponse>() {
            @Override
            public void onResponse(Call<ViewProfileResponse> call, Response<ViewProfileResponse> response) {
                Utils.dismissDialog();
                ViewProfileResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                       if(body.getData()!=null)
                           updateFields(body.getData());
                    } else {
                        Utils.callToast(getActivity(), body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
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

        Picasso.with(getActivity()).load(ApiUrls.PROFILEBASEPATH+viewdata.getPhoto()).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(profile_image);

        this.viewdata=viewdata;
    }
}
