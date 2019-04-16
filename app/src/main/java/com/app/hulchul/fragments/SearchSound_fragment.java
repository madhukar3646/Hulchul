package com.app.hulchul.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.activities.SearchActivity;
import com.app.hulchul.adapters.HashtagSearchAdapter;
import com.app.hulchul.adapters.SoundSearchAdapter;
import com.app.hulchul.model.HashtagSearchResponse;
import com.app.hulchul.model.SoundSearchdata;
import com.app.hulchul.model.Soundsearchresponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchSound_fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected {

    @BindView(R.id.rv_sounds)
    RecyclerView rv_sounds;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private SoundSearchAdapter soundSearchAdapter;
    private ConnectionDetector connectionDetector;
    private ArrayList<SoundSearchdata> searchdataList=new ArrayList<>();
    private String search_key="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_sound_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        soundSearchAdapter=new SoundSearchAdapter(getActivity(),searchdataList);
        rv_sounds.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_sounds.setAdapter(soundSearchAdapter);
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
            else {
                if(searchdataList.size()>0)
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
        searchdataList.clear();
        Utils.showDialog(getActivity());
        Call<Soundsearchresponse> call= RetrofitApis.Factory.createTemp(getActivity()).songSearch(key);
        call.enqueue(new Callback<Soundsearchresponse>() {
            @Override
            public void onResponse(Call<Soundsearchresponse> call, Response<Soundsearchresponse> response) {
                Utils.dismissDialog();
                Soundsearchresponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        if(body.getData()!=null)
                            searchdataList.addAll(body.getData());
                    } else {
                        //Utils.callToast(getActivity(), body.getMessage());
                        tv_nodata.setText(body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
                }
                if(searchdataList.size()>0)
                    tv_nodata.setVisibility(View.GONE);
                else
                    tv_nodata.setVisibility(View.VISIBLE);

                soundSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Soundsearchresponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("searchuser onFailure",""+t.getMessage());
            }
        });
    }
}
