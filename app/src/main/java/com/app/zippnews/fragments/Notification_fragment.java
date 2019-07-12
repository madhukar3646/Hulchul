package com.app.zippnews.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.app.zippnews.R;
import com.app.zippnews.activities.PlayvideosCategorywise_Activity;
import com.app.zippnews.adapters.NotificationsAdapter;
import com.app.zippnews.model.CommentPostResponse;
import com.app.zippnews.model.NotificationModel;
import com.app.zippnews.model.NotificationsResponse;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.model.VideosListingResponse;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ApiUrls;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification_fragment extends Fragment implements NotificationsAdapter.OnNotificationClickListener{

    @BindView(R.id.rv_notifications)
    RecyclerView rv_notifications;
    @BindView(R.id.switch_newfollowers)
    Switch switch_newfollowers;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private ConnectionDetector connectionDetector;
    SessionManagement sessionManagement;
    private int listcount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        notificationsAdapter=new NotificationsAdapter(getActivity(),notificationModelArrayList);
        notificationsAdapter.setOnNotificationClickListener(this);
        rv_notifications.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_notifications.setAdapter(notificationsAdapter);

        setPushStatusSwitch();
        if(connectionDetector.isConnectingToInternet())
            setNotificationsData("20","0");
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

        rv_notifications.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
                Log.e("pos"+pos,"numitems "+numItems);
                if ((pos + 1) >= numItems)
                {
                    if(numItems<listcount)
                    {
                        if (connectionDetector.isConnectingToInternet()) {
                            setNotificationsData("20", "" + numItems);
                        } else
                            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                    }
                }
            }
        });

        switch_newfollowers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(connectionDetector.isConnectingToInternet())
                    setEnableordisableNotifications(b);
                else
                  Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
            }
        });
    }

    private void setEnableordisableNotifications(boolean isEnable){
        String pushstatus;
        if(isEnable)
            pushstatus="1";
        else
            pushstatus="0";

        Utils.showDialog(getContext());
        Call<CommentPostResponse> call= RetrofitApis.Factory.create(getContext()).enableDisablePushNotification(sessionManagement.getValueFromPreference(SessionManagement.USER_TOKEN),pushstatus);
        call.enqueue(new Callback<CommentPostResponse>() {
            @Override
            public void onResponse(Call<CommentPostResponse> call, Response<CommentPostResponse> response) {
                Utils.dismissDialog();
                CommentPostResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 0) {
                        sessionManagement.setPushStatus(pushstatus);
                        setPushStatusSwitch();
                        //Utils.callToast(getActivity(), body.getMessage());
                    } else {
                        Utils.callToast(getActivity(), body.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentPostResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("EnbldsbleNoti onFailure",""+t.getMessage());
            }
        });
    }

    private void setNotificationsData(String limit,String offset){
        Utils.showDialog(getContext());
        Call<NotificationsResponse> call= RetrofitApis.Factory.createTemp(getContext()).notificationsService(sessionManagement.getValueFromPreference(SessionManagement.USERID),limit,offset);
        call.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                Utils.dismissDialog();
                NotificationsResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        listcount = body.getTotalcount();
                        if (body.getData() != null && body.getData().size() > 0)
                            notificationModelArrayList.addAll(body.getData());
                        notificationsAdapter.notifyDataSetChanged();
                    } else {
                        Utils.callToast(getActivity(), body.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onNotificationClick(NotificationModel model) {
        goToVideo(model.getVideoId());
    }

    private void goToVideo(String videoid){
        Log.e("video id",videoid);
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        Utils.showDialog(getContext());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getContext()).getVideoDatabyId(userid,videoid,"1","0","","","");
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                ArrayList<VideoModel> modelArrayList=new ArrayList<>();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            preload(body.getVideos());
                            goToVideoDisplayScreen(modelArrayList,body.getUrl(), body.getSongurl());
                        }
                    } else {
                        if(modelArrayList.size()==0)
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

    private void goToVideoDisplayScreen(ArrayList<VideoModel> list,String videopath,String musicpath)
    {
        Intent intent=new Intent(getActivity(), PlayvideosCategorywise_Activity.class);
        intent.putExtra("isFrom","notification");
        intent.putParcelableArrayListExtra("videos",list);
        intent.putExtra("position",0);
        intent.putExtra("videosbasepath",videopath);
        intent.putExtra("musicbasepath",musicpath);
        startActivity(intent);
    }

    private void setPushStatusSwitch()
    {
        String status=sessionManagement.getPushStatus();
        if(status.equalsIgnoreCase("0"))
            switch_newfollowers.setChecked(false);
        else
            switch_newfollowers.setChecked(true);
    }
    private void preload(List<VideoModel> videoModels) {
        for(VideoModel videoModel:videoModels)
        {
            try {
                String video=videoModel.getVideo();
                if(video!=null && video.trim().length()>4)
                {
                    String jpeg=video.substring(0,video.length()-4);
                    jpeg=jpeg+"_image.jpeg";
                    Picasso.with(getActivity()).load(ApiUrls.VIDEOTHUMBNAIL + jpeg).fetch();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
