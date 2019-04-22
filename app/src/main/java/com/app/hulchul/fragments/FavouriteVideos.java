package com.app.hulchul.fragments;
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

import com.app.hulchul.R;
import com.app.hulchul.activities.FavouritesActivity;
import com.app.hulchul.adapters.HashtagsGridAdapter;
import com.app.hulchul.adapters.VideothumbnailsAdapter;
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

public class FavouriteVideos extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected,HashtagsGridAdapter.OnHashtagItemClickListener{

    @BindView(R.id.rv_favouritevideos)
    RecyclerView rv_favouritevideos;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private VideothumbnailsAdapter adapter;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid,videosbasepath,musicbasepath;
    private ArrayList<VideoModel> videoslist=new ArrayList<>();
    private HashtagsGridAdapter videosadapter;

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
        adapter=new VideothumbnailsAdapter(getActivity());
        rv_favouritevideos.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rv_favouritevideos.setAdapter(adapter);

       rv_favouritevideos.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                       setDataToContainer("video","10", ""+numItems);
                   } else
                       Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
               }
           }
       });
    }

    @Override
    public void onFavouriteFragmentSelected()
    {
        if(connectionDetector.isConnectingToInternet()) {
            videoslist.clear();
            setDataToContainer("video","10","0");
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

    }
}
