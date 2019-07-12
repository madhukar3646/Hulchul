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
import com.app.zippnews.activities.SearchActivity;
import com.app.zippnews.activities.SoundsSearchresultsActivity;
import com.app.zippnews.adapters.SoundSearchAdapter;
import com.app.zippnews.model.SoundSearchdata;
import com.app.zippnews.model.Soundsearchresponse;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchSound_fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected,SoundSearchAdapter.OnSoundSelectedListener{

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
        soundSearchAdapter.setOnSoundSelectedListener(this);
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

    @Override
    public void onSoundSelected(SoundSearchdata soundSearchdata) {
        Intent intent=new Intent(getActivity(), SoundsSearchresultsActivity.class);
        intent.putExtra("soundname",soundSearchdata.getName());
        intent.putExtra("songid",soundSearchdata.getSongId());
        intent.putExtra("status",soundSearchdata.getStatus());
        startActivity(intent);
    }
}
