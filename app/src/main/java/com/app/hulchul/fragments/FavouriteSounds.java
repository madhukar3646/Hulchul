package com.app.hulchul.fragments;

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

import com.app.hulchul.R;
import com.app.hulchul.activities.FavouritesActivity;
import com.app.hulchul.activities.SoundsSearchresultsActivity;
import com.app.hulchul.adapters.SoundSearchAdapter;
import com.app.hulchul.model.SoundSearchdata;
import com.app.hulchul.model.Soundsearchresponse;
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


public class FavouriteSounds extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected,SoundSearchAdapter.OnSoundSelectedListener{

    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.rv_favouritesounds)
    RecyclerView rv_favouritesounds;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid,videosbasepath,musicbasepath;
    private SoundSearchAdapter adapter;
    private ArrayList<SoundSearchdata> soundsdataList=new ArrayList<>();
    private int totalcount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favourite_sounds, container, false);
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
        adapter=new SoundSearchAdapter(getActivity(),soundsdataList);
        adapter.setOnSoundSelectedListener(this);
        rv_favouritesounds.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_favouritesounds.setAdapter(adapter);

        rv_favouritesounds.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            favouriteSounds("song", "20", "" + numItems);
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
            soundsdataList.clear();
            favouriteSounds("song","20","0");
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    public void favouriteSounds(String type,String limit,String offset) {
        Utils.showDialog(getActivity());
        Call<Soundsearchresponse> call= RetrofitApis.Factory.createTemp(getActivity()).listFavouritesSounds(userid,type,limit,offset);
        call.enqueue(new Callback<Soundsearchresponse>() {
            @Override
            public void onResponse(Call<Soundsearchresponse> call, Response<Soundsearchresponse> response) {
                Utils.dismissDialog();
                Soundsearchresponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==1) {
                        if(body.getSoundsdata()!=null && body.getSoundsdata().size() > 0) {
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            totalcount=body.getTotalcount();
                            soundsdataList.addAll(body.getSoundsdata());
                            tv_nodata.setVisibility(View.GONE);
                        }
                    } else {
                        if(soundsdataList.size()==0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(getActivity(), body.getMessage());
                        }
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
                }

                if(soundsdataList.size()==0)
                    tv_nodata.setVisibility(View.VISIBLE);
                else
                    tv_nodata.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
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
        startActivity(intent);
    }
}
