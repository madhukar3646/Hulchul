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
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.activities.FavouritesActivity;
import com.app.zippnews.activities.HashtagSearchresultsActivity;
import com.app.zippnews.adapters.HashtagSearchAdapter;
import com.app.zippnews.model.HashtagSearchResponse;
import com.app.zippnews.model.Hashtagsearchdata;
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

public class FavouiteHashtags extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected,HashtagSearchAdapter.OnHashtagClickListener {

    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.rv_favouritehashtags)
    RecyclerView rv_favouritehashtags;
    private HashtagSearchAdapter adapter;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid,videosbasepath,musicbasepath;
    private ArrayList<Hashtagsearchdata> hashtagsearchdataList=new ArrayList<>();
    private int totalcount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favouite_hashtags, container, false);
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
        adapter=new HashtagSearchAdapter(getActivity(),hashtagsearchdataList);
        adapter.setOnHashtagClickListener(this);
        rv_favouritehashtags.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_favouritehashtags.setAdapter(adapter);

        rv_favouritehashtags.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            favouriteHashtags("hashTag", "20", "" + numItems);
                        } else
                            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                    }
                }
            }
        });
    }

    @Override
    public void onFavouriteFragmentSelected() {
        if(connectionDetector.isConnectingToInternet()) {
            hashtagsearchdataList.clear();
            favouriteHashtags("hashTag","20","0");
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    public void favouriteHashtags(String type,String limit,String offset) {
        Utils.showDialog(getActivity());
        Call<HashtagSearchResponse> call= RetrofitApis.Factory.createTemp(getActivity()).listFavouritesHashtags(userid,type,limit,offset);
        call.enqueue(new Callback<HashtagSearchResponse>() {
            @Override
            public void onResponse(Call<HashtagSearchResponse> call, Response<HashtagSearchResponse> response) {
                Utils.dismissDialog();
                HashtagSearchResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==1) {
                        if(body.getHashtagsdata()!=null && body.getHashtagsdata().size() > 0) {
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            totalcount=body.getTotalcount();
                            hashtagsearchdataList.addAll(body.getHashtagsdata());
                            tv_nodata.setVisibility(View.GONE);
                        }
                    } else {
                        if(hashtagsearchdataList.size()==0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(getActivity(), body.getMessage());
                        }
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
                }

                if(hashtagsearchdataList.size()==0)
                    tv_nodata.setVisibility(View.VISIBLE);
                else
                    tv_nodata.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HashtagSearchResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("searchuser onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onHashtagClick(Hashtagsearchdata hashtagsearchdata) {
        Intent intent=new Intent(getActivity(), HashtagSearchresultsActivity.class);
        intent.putExtra("hashtag",hashtagsearchdata.getHashTag());
        startActivity(intent);
    }
}
