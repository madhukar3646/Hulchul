package com.app.zippnews.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.app.zippnews.R;
import com.app.zippnews.activities.HashtagSearchresultsActivity;
import com.app.zippnews.activities.PlayvideosCategorywise_Activity;
import com.app.zippnews.activities.SearchActivity;
import com.app.zippnews.adapters.HashtagsCategoriesAdapter;
import com.app.zippnews.adapters.HashtagsthumnailsAdapter;
import com.app.zippnews.adapters.TrendingHashtagsBannersAdapter;
import com.app.zippnews.model.Discoverhashtags;
import com.app.zippnews.model.Discoverresponse;
import com.app.zippnews.model.Hashtagbanner;
import com.app.zippnews.model.TrendingHashtagsBannersResponse;
import com.app.zippnews.model.VideoModel;
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

public class Discover_fragment extends Fragment implements View.OnClickListener,HashtagsCategoriesAdapter.OnHashtagViewAllListener, HashtagsthumnailsAdapter.OnHashtagItemClickListener {

    @BindView(R.id.rv_trendinghashtags)
    RecyclerView rv_trendinghashtags;
    @BindView(R.id.rv_hashtagslistcontainer)
    RecyclerView rv_hashtagslistcontainer;
    TrendingHashtagsBannersAdapter adapter;
    HashtagsCategoriesAdapter hashtagsCategoriesAdapter;
    @BindView(R.id.layout_search)
    RelativeLayout layout_search;
    private SessionManagement sessionManagement;
    private ConnectionDetector connectionDetector;
    private ArrayList<Discoverhashtags> discoverhashtagsList=new ArrayList<>();
    private ArrayList<Hashtagbanner> hashtagbannersList=new ArrayList<Hashtagbanner>();
    private String userid="",videosbasepath,musicbasepath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_discover_fragment, container, false);
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

        layout_search.setOnClickListener(this);
        rv_trendinghashtags.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        adapter=new TrendingHashtagsBannersAdapter(getActivity(),hashtagbannersList);
        rv_trendinghashtags.setAdapter(adapter);

        rv_hashtagslistcontainer.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        hashtagsCategoriesAdapter=new HashtagsCategoriesAdapter(getActivity(),discoverhashtagsList);
        hashtagsCategoriesAdapter.setOnHashtagItemClickListener(this);
        hashtagsCategoriesAdapter.setOnHashtagViewAllListener(this);
        rv_hashtagslistcontainer.setAdapter(hashtagsCategoriesAdapter);

        if(connectionDetector.isConnectingToInternet())
        {
            getDiscoverhashtagsdata();
            getHashTrandingBanners();

        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    private void getHashTrandingBanners() {
        hashtagbannersList.clear();
//        Utils.showDialog(getActivity());
        Call<TrendingHashtagsBannersResponse> call= RetrofitApis.Factory.createTemp(getActivity()).getTrendingTags();
         call.enqueue(new Callback<TrendingHashtagsBannersResponse>() {
             @Override
             public void onResponse(Call<TrendingHashtagsBannersResponse> call, Response<TrendingHashtagsBannersResponse> response) {
//                 Utils.dismissDialog();
                 TrendingHashtagsBannersResponse body=response.body();
                 if(body!=null) {
                     if (body.getStatus()==0) {
                         if(body.getHashtagbanners()!=null  && body.getHashtagbanners().size() > 0) {
                             hashtagbannersList.addAll(body.getHashtagbanners());

                         }
                     } else {
                         Utils.callToast(getActivity(), body.getMessage());
                     }
                 }
                 else {
                     Utils.callToast(getActivity(), "Null data came");
                 }
                 adapter.notifyDataSetChanged();
                 if(adapter.getItemCount()>0)
                 {
                     rv_trendinghashtags.setVisibility(View.VISIBLE);
                 }
             }

             @Override
             public void onFailure(Call<TrendingHashtagsBannersResponse> call, Throwable t) {
//                 Utils.dismissDialog();
                 Log.e("hasTagBanner onFailure",""+t.getMessage());
             }
         });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    public void getDiscoverhashtagsdata() {
        discoverhashtagsList.clear();
        Utils.showDialog(getActivity());
        Call<Discoverresponse> call= RetrofitApis.Factory.createTemp(getActivity()).discoverService(userid);
        call.enqueue(new Callback<Discoverresponse>() {
            @Override
            public void onResponse(Call<Discoverresponse> call, Response<Discoverresponse> response) {
                Utils.dismissDialog();
                Discoverresponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        if(body.getData()!=null  && body.getData().size() > 0) {
                            discoverhashtagsList.addAll(body.getData());
                            videosbasepath = body.getUrl();
                            musicbasepath = body.getSongurl();
                            hashtagsCategoriesAdapter.setVideobasepath(videosbasepath);
                        }
                    } else {
                        Utils.callToast(getActivity(), body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
                }
                hashtagsCategoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Discoverresponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("searchuser onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onHashtagViewAll(Discoverhashtags discoverhashtags) {

        Intent intent=new Intent(getActivity(), HashtagSearchresultsActivity.class);
        intent.putExtra("hashtag",discoverhashtags.getHashTag());
        startActivity(intent);
    }

    @Override
    public void onHashtagitemClick(ArrayList<VideoModel> discoverhashtagvideosList,int position) {

        Intent intent=new Intent(getActivity(), PlayvideosCategorywise_Activity.class);
        intent.putExtra("isFrom","hashTag");
        intent.putExtra("hashTag",discoverhashtagvideosList.get(position).getHashTag());
        intent.putParcelableArrayListExtra("videos",discoverhashtagvideosList);
        intent.putExtra("position",position);
        intent.putExtra("videosbasepath",videosbasepath);
        intent.putExtra("musicbasepath",musicbasepath);
        startActivity(intent);
    }
}
