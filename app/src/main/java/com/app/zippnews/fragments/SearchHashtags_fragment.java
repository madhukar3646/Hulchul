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
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.activities.HashtagSearchresultsActivity;
import com.app.zippnews.activities.SearchActivity;
import com.app.zippnews.adapters.HashtagSearchAdapter;
import com.app.zippnews.model.HashtagSearchResponse;
import com.app.zippnews.model.Hashtagsearchdata;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchHashtags_fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected,HashtagSearchAdapter.OnHashtagClickListener{

    @BindView(R.id.rv_hashtags)
    RecyclerView rv_hashtags;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private HashtagSearchAdapter hashtagSearchAdapter;
    private ConnectionDetector connectionDetector;
    private ArrayList<Hashtagsearchdata> hashtagsearchdataList=new ArrayList<>();
    private String search_key="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_hashtags_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        hashtagSearchAdapter=new HashtagSearchAdapter(getActivity(),hashtagsearchdataList);
        hashtagSearchAdapter.setOnHashtagClickListener(this);
        rv_hashtags.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_hashtags.setAdapter(hashtagSearchAdapter);
    }

    @Override
    public void onSearchFragmentSelected(String searchkey) {
        if(searchkey!=null && searchkey.trim().length()!=0)
        {
            if(!this.search_key.equalsIgnoreCase(searchkey))
            {
                if(connectionDetector.isConnectingToInternet())
                    onPerformSearch(searchkey);
                else
                    Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
            }
            else
            {
                if(hashtagsearchdataList.size()>0)
                    tv_nodata.setVisibility(View.GONE);
                else
                    tv_nodata.setVisibility(View.VISIBLE);
            }
        }
        Log.e("Users fragment","Users fragment");
    }

    @Override
    public void onPerformSearch(String key) {
        this.search_key=key;
        hashtagsearchdataList.clear();
        Utils.showDialog(getActivity());
        Call<HashtagSearchResponse> call= RetrofitApis.Factory.createTemp(getActivity()).hashTagSearch(key);
        call.enqueue(new Callback<HashtagSearchResponse>() {
            @Override
            public void onResponse(Call<HashtagSearchResponse> call, Response<HashtagSearchResponse> response) {
                Utils.dismissDialog();
                HashtagSearchResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        if(body.getData()!=null)
                            hashtagsearchdataList.addAll(body.getData());
                    } else {
                        //Utils.callToast(getActivity(), body.getMessage());
                        tv_nodata.setText(body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
                }
                if(hashtagsearchdataList.size()>0)
                    tv_nodata.setVisibility(View.GONE);
                else
                    tv_nodata.setVisibility(View.VISIBLE);

                hashtagSearchAdapter.notifyDataSetChanged();
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
