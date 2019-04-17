package com.app.hulchul.fragments;

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

import com.app.hulchul.R;
import com.app.hulchul.activities.SearchActivity;
import com.app.hulchul.adapters.HashtagsCategoriesAdapter;
import com.app.hulchul.adapters.TrendingHashtagsBannersAdapter;
import com.app.hulchul.model.Discoverhashtags;
import com.app.hulchul.model.Discoverresponse;
import com.app.hulchul.model.SearchUserResponse;
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

public class Discover_fragment extends Fragment implements View.OnClickListener {

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
    private String userid="";

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
        adapter=new TrendingHashtagsBannersAdapter(getActivity());
        rv_trendinghashtags.setAdapter(adapter);

        rv_hashtagslistcontainer.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        hashtagsCategoriesAdapter=new HashtagsCategoriesAdapter(getActivity(),discoverhashtagsList);
        rv_hashtagslistcontainer.setAdapter(hashtagsCategoriesAdapter);

        if(connectionDetector.isConnectingToInternet())
            getDiscoverhashtagsdata();
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
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
                        if(body.getData()!=null)
                            discoverhashtagsList.addAll(body.getData());
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
}
