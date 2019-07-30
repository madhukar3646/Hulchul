package com.app.zippnews.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.activities.DraftsActivity;
import com.app.zippnews.activities.EditProfileActivity;
import com.app.zippnews.activities.FavouritesActivity;
import com.app.zippnews.activities.FindFriendsActivity;
import com.app.zippnews.activities.LoginLandingActivity;
import com.app.zippnews.activities.MainActivity;
import com.app.zippnews.activities.PlayvideosCategorywise_Activity;
import com.app.zippnews.activities.SettingsActivity;
import com.app.zippnews.adapters.HashtagsGridAdapter;
import com.app.zippnews.model.ProfileViewdata;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.model.VideosListingResponse;
import com.app.zippnews.model.ViewProfileResponse;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ApiUrls;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Me_Fragment extends Fragment implements View.OnClickListener,HashtagsGridAdapter.OnHashtagItemClickListener,HashtagsGridAdapter.OnItemLongClickListener{

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
    @BindView(R.id.iv_editprofile)
    ImageView iv_editprofile;

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
    @BindView(R.id.layout_drafts)
    LinearLayout layout_drafts;
    @BindView(R.id.tv_drafts)
    TextView tv_drafts;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;
    @BindView(R.id.nestedsrollview)
    NestedScrollView nestedsrollview;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.tv_biodata)
    TextView tv_biodata;

    private SessionManagement sessionManagement;
    private ConnectionDetector connectionDetector;
    private HashtagsGridAdapter profileadapter,likedadapter;
    private ProfileViewdata viewdata;
    GridLayoutManager gridLayoutManager;
    private boolean isProfilevideos=true;
    private String userid="",videosbasepath,musicbasepath;
    private ArrayList<VideoModel> profilevideoslist=new ArrayList<>();
    private ArrayList<VideoModel> likedvideoslist=new ArrayList<>();
    private boolean isRefreshWant=false;

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
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        iv_menuburger.setOnClickListener(this);
        iv_addfriends.setOnClickListener(this);
        iv_editprofile.setOnClickListener(this);
        layout_yourvideos.setOnClickListener(this);
        layout_hearts.setOnClickListener(this);
        layout_drafts.setOnClickListener(this);

        gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        recyclerview_videos.setNestedScrollingEnabled(false);

        profileadapter=new HashtagsGridAdapter(getContext(),profilevideoslist);
        profileadapter.setOnItemLongClickListener(this);
        profileadapter.setOnHashtagItemClickListener(this);
        likedadapter=new HashtagsGridAdapter(getContext(),likedvideoslist);
        likedadapter.setOnHashtagItemClickListener(this);
        setClickableFocus(true);
        if(connectionDetector.isConnectingToInternet()) {
            viewProfile(sessionManagement.getValueFromPreference(SessionManagement.USERID),true);
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

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
                                     setfavouriteVideosDataToContainer("20",""+totalItemCount);
                            }
                        }
                        else
                            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_drafts.setText("Drafts Videos ("+getDraftsCount()+")");
        if(connectionDetector.isConnectingToInternet())
        {
            if(isRefreshWant)
            {
                likedvideoslist.clear();
                likedadapter.notifyDataSetChanged();
                setfavouriteVideosDataToContainer("20","0");
            }
        }
        else
            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
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
                    Intent intent=new Intent(getActivity(), FindFriendsActivity.class);
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
            case R.id.iv_editprofile:
                Intent editprofile=new Intent(getActivity(), EditProfileActivity.class);
                if(viewdata!=null)
                {
                    editprofile.putExtra("name",viewdata.getFullName());
                    editprofile.putExtra("biodata",viewdata.getBioData());
                    editprofile.putExtra("profilephoto",viewdata.getPhoto());
                }
                startActivityForResult(editprofile, EditProfileActivity.PROFILE_UPDATED);
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
            iv_myhearts.setImageResource(R.mipmap.bookmark_normal);
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
            iv_myhearts.setImageResource(R.mipmap.bookmark_active);
            tv_myhearts.setTextColor(Color.BLACK);
            recyclerview_videos.setAdapter(likedadapter);
            isProfilevideos=false;
            if(likedvideoslist.size()>0)
                tv_nodata.setVisibility(View.GONE);
            if(likedvideoslist.size()==0)
            {
                if(connectionDetector.isConnectingToInternet())
                    setfavouriteVideosDataToContainer("20","0");
                else
                    Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
            }
        }
    }

    private int getDraftsCount()
    {
        int draftscount=0;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/"+getResources().getString(R.string.app_name)+"drafts";
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

    private void viewProfile(String userid,boolean isUpdateVideosAlso){
        Utils.showDialog(getActivity());
        Call<ViewProfileResponse> call= RetrofitApis.Factory.create(getActivity()).viewProfile(sessionManagement.getValueFromPreference(SessionManagement.USER_TOKEN),userid,userid);
        call.enqueue(new Callback<ViewProfileResponse>() {
            @Override
            public void onResponse(Call<ViewProfileResponse> call, Response<ViewProfileResponse> response) {
                Utils.dismissDialog();
                ViewProfileResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                       if(body.getData()!=null)
                           updateFields(body.getData());
                       if(isUpdateVideosAlso)
                         setProfilevideosDataToContainer("20","0");
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
        tv_name.setText(viewdata.getFullName());
        tv_title.setText(viewdata.getFullName());
        tv_biodata.setText(viewdata.getBioData());

        if(viewdata.getVideos()!=null)
          tv_myvideos.setText("Your Videos ("+viewdata.getVideos()+")");
        else
            tv_myvideos.setText("Your Videos (0)");
        String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        String username,biodata;
        if(viewdata.getFullName()==null || viewdata.getFullName().equalsIgnoreCase("null") || viewdata.getFullName().trim().length()==0)
        {
            username="@User"+userid;
            viewdata.setFullName(username);
            tv_name.setText(username);
            tv_title.setText(username);
        }

        if(viewdata.getBioData()==null || viewdata.getBioData().equalsIgnoreCase("null"))
        {
            biodata="No bio data yet";
            viewdata.setBioData(biodata);
        }

        Picasso.with(getActivity()).load(ApiUrls.PROFILEBASEPATH+viewdata.getPhoto()).placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(profile_image);
        this.viewdata=viewdata;
    }

    private void setProfilevideosDataToContainer(String limit,String offset){
        Utils.showDialog(getActivity());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getActivity()).profileVideos(userid,limit,offset);
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
                            Utils.callToast(getActivity(), body.getMessage());
                        }
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

    private void setfavouriteVideosDataToContainer(String limit,String offset){
        Utils.showDialog(getActivity());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getActivity()).listFavouritesVideos(userid,"video",limit,offset);
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
                            Utils.callToast(getActivity(), body.getMessage());
                        }
                    }
                    isRefreshWant=false;
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

    private void deleteVideo(String videoid,int position){
        Utils.showDialog(getActivity());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getActivity()).deleteUserVideo(sessionManagement.getValueFromPreference(SessionManagement.USER_TOKEN),videoid);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 0) {
                        profilevideoslist.remove(position);
                        tv_myvideos.setText("Your Videos ("+profilevideoslist.size()+")");
                        profileadapter.notifyDataSetChanged();
                    } else {

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

    @Override
    public void onHashtagitemClick(ArrayList<VideoModel> discoverhashtagvideosList, int pos) {
        Intent intent=new Intent(getActivity(), PlayvideosCategorywise_Activity.class);
        if(isProfilevideos)
            intent.putExtra("isFrom","profilevideos");
        else {
            isRefreshWant=true;
            intent.putExtra("isFrom", "favouritevideos");
        }
        intent.putExtra("profileuserid",userid);
        intent.putParcelableArrayListExtra("videos",discoverhashtagvideosList);
        intent.putExtra("position",pos);
        intent.putExtra("videosbasepath",videosbasepath);
        intent.putExtra("musicbasepath",musicbasepath);
        startActivity(intent);
    }

    private void displayDeleteDialog(String videoid,int position)
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.main_exit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tv_confirmation=(TextView)dialog.findViewById(R.id.tv_confirmation);
        tv_confirmation.setText("Confirmation!");
        TextView tv_title=(TextView)dialog.findViewById(R.id.tv_title);
        tv_title.setText("Are you sure you want to delete this video?");
        TextView tv_exit=(TextView)dialog.findViewById(R.id.tv_exit);
        tv_exit.setText("Delete");
        TextView tv_cancel=(TextView)dialog.findViewById(R.id.tv_cancel);

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                deleteVideo(videoid,position);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onItemLongClick(VideoModel videoModel,int position) {
       displayDeleteDialog(videoModel.getId(),position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
          if(requestCode==EditProfileActivity.PROFILE_UPDATED)
          {
              if(connectionDetector.isConnectingToInternet()) {
                  viewProfile(sessionManagement.getValueFromPreference(SessionManagement.USERID),false);
              }
              else
                  Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

          }
        }
    }
}
