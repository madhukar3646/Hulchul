package com.app.zippnews.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.activities.FavouritesActivity;
import com.app.zippnews.activities.PlayvideosCategorywise_Activity;
import com.app.zippnews.adapters.HashtagsGridAdapter;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.model.VideosListingResponse;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteVideos extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected,HashtagsGridAdapter.OnHashtagItemClickListener{

    @BindView(R.id.rv_favouritevideos)
    RecyclerView rv_favouritevideos;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid,videosbasepath,musicbasepath;
    private ArrayList<VideoModel> videoslist=new ArrayList<>();
    private HashtagsGridAdapter videosadapter;
    private int totalcount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favourite_videos, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        tv_nodata.setVisibility(View.GONE);
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        videosadapter=new HashtagsGridAdapter(getActivity(),videoslist);
        videosadapter.setOnHashtagItemClickListener(this);
        rv_favouritevideos.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rv_favouritevideos.setAdapter(videosadapter);

        if(videoslist.size()==0) {
            if (connectionDetector.isConnectingToInternet()) {
                videoslist.clear();
                setDataToContainer("video", "20", "0");
            } else
                Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
        }

       rv_favouritevideos.setOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
               super.onScrolled(recyclerView, dx, dy);
               LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
               int pos = layoutManager.findLastCompletelyVisibleItemPosition();
               int numItems = recyclerView.getAdapter().getItemCount();
               Log.e("pos"+pos,"numitems "+numItems);
               if(pos>0 && totalcount!=numItems) {
                   if ((pos + 1) >= numItems) {
                       if (connectionDetector.isConnectingToInternet()) {
                           setDataToContainer("video", "20", "" + numItems);
                       } else
                           Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                   }
               }
           }
       });
    }

    @Override
    public void onFavouriteFragmentSelected()
    {
        if(connectionDetector.isConnectingToInternet()) {
            videoslist.clear();
            setDataToContainer("video","20","0");
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    private void setDataToContainer(String type,String limit,String offset){
        Utils.showDialog(getActivity());
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(getActivity()).listFavouritesVideos(userid,type,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            videoslist.addAll(body.getVideos());
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            totalcount=body.getTotalcount();
                            videosadapter.setVideobasepath(videosbasepath);
                            videosadapter.notifyDataSetChanged();
                            tv_nodata.setVisibility(View.GONE);
                        }
                    } else {
                        if(videoslist.size()==0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(getActivity(), body.getMessage());
                        }
                    }
                }
                else {
                    Utils.callToast(getActivity(),"null response came");
                }

                if(videoslist.size()==0)
                    tv_nodata.setVisibility(View.VISIBLE);
                else
                    tv_nodata.setVisibility(View.GONE);
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
        intent.putExtra("isFrom","favouritevideos");
        intent.putParcelableArrayListExtra("videos",discoverhashtagvideosList);
        intent.putExtra("position",pos);
        intent.putExtra("videosbasepath",videosbasepath);
        intent.putExtra("musicbasepath",musicbasepath);
        startActivity(intent);
    }
}
