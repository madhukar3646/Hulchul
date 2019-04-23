package com.app.hulchul.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.adapters.PlaylistCategoriesAdapter;
import com.app.hulchul.adapters.SongslistContainerAdapter;
import com.app.hulchul.adapters.TrendingSoundsBannersAdapter;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectSoundsActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    @BindView(R.id.iv_favourite)
    ImageView iv_favourite;
    @BindView(R.id.iv_mysounds)
    ImageView iv_mysounds;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.rv_soundgroups)
    RecyclerView rv_soundgroups;
    @BindView(R.id.layout_search)
    RelativeLayout layout_search;
    @BindView(R.id.rv_playlistcategory)
    RecyclerView rv_playlistcategory;
    @BindView(R.id.rv_songslistcontainer)
    RecyclerView rv_songslistcontainer;
    private TrendingSoundsBannersAdapter adapter;
    private PlaylistCategoriesAdapter playlistCategoriesAdapter;
    private SongslistContainerAdapter songslistContainerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sounds);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(SelectSoundsActivity.this);
        sessionManagement=new SessionManagement(SelectSoundsActivity.this);
        iv_mysounds.setOnClickListener(this);

        rv_soundgroups.setLayoutManager(new LinearLayoutManager(SelectSoundsActivity.this,LinearLayoutManager.HORIZONTAL, false));
        adapter=new TrendingSoundsBannersAdapter(SelectSoundsActivity.this);
        rv_soundgroups.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(SelectSoundsActivity.this,3);
        rv_playlistcategory.setLayoutManager(gridLayoutManager);
        playlistCategoriesAdapter=new PlaylistCategoriesAdapter(SelectSoundsActivity.this);
        rv_playlistcategory.setAdapter(playlistCategoriesAdapter);

        rv_songslistcontainer.setLayoutManager(new LinearLayoutManager(SelectSoundsActivity.this,LinearLayoutManager.VERTICAL, false));
        songslistContainerAdapter=new SongslistContainerAdapter(SelectSoundsActivity.this);
        rv_songslistcontainer.setAdapter(songslistContainerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_mysounds:
                startActivity(new Intent(SelectSoundsActivity.this,MySoundsActivity.class));
                //startActivity(new Intent(SelectSoundsActivity.this,ServerSoundsCompletelistingActivity.class));
                break;
        }
    }

    /*private void setDataToContainer(String limit,String offset){
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        Utils.showDialog(SelectSoundsActivity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(SelectSoundsActivity.this).videosListingService(userid,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            modelArrayList.addAll(body.getVideos());
                            adapter.setBasepaths(body.getUrl(), body.getSongurl());
                        }
                    } else {
                        if(modelArrayList.size()==0)
                            Utils.callToast(getActivity(), body.getMessage());
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.notifyDataSetChanged();
                    Utils.callToast(getActivity(),"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                adapter.notifyDataSetChanged();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }*/
}
