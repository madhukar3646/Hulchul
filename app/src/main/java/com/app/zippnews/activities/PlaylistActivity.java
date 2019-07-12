package com.app.zippnews.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.adapters.AllplaylistsAdapter;
import com.app.zippnews.model.Allplaylistresponse;
import com.app.zippnews.model.PlaylistModel;
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

public class PlaylistActivity extends AppCompatActivity implements AllplaylistsAdapter.OnPlaylistClickListener {

    @BindView(R.id.rv_playlists)
    RecyclerView rv_playlists;
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private ArrayList<PlaylistModel> playlistModelArrayList=new ArrayList<>();
    private AllplaylistsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        tv_nodata.setVisibility(View.GONE);
        connectionDetector=new ConnectionDetector(PlaylistActivity.this);
        sessionManagement=new SessionManagement(PlaylistActivity.this);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rv_playlists.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new AllplaylistsAdapter(PlaylistActivity.this,playlistModelArrayList);
        adapter.setOnPlaylistClickListener(this);
        rv_playlists.setAdapter(adapter);

        if(connectionDetector.isConnectingToInternet())
            setDataToContainer("20","0");
        else
            Utils.callToast(PlaylistActivity.this,getResources().getString(R.string.internet_toast));

        rv_playlists.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        setDataToContainer("20", ""+numItems);
                    } else
                        Utils.callToast(PlaylistActivity.this, getResources().getString(R.string.internet_toast));
                }
            }
        });
    }

    private void setDataToContainer(String limit,String offset){
        Utils.showDialog(PlaylistActivity.this);
        Call<Allplaylistresponse> call= RetrofitApis.Factory.createTemp(PlaylistActivity.this).songsAllplaylist(limit,offset);
        call.enqueue(new Callback<Allplaylistresponse>() {
            @Override
            public void onResponse(Call<Allplaylistresponse> call, Response<Allplaylistresponse> response) {
                Utils.dismissDialog();
                Allplaylistresponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1)
                    {
                      if(body.getData()!=null)
                          playlistModelArrayList.addAll(body.getData());
                    } else {
                        Utils.callToast(PlaylistActivity.this,""+body.getMessage());
                    }
                }
                else {
                    adapter.notifyDataSetChanged();
                    Utils.callToast(PlaylistActivity.this,"null response came");
                }
                adapter.notifyDataSetChanged();
                if(playlistModelArrayList.size()==0)
                    tv_nodata.setVisibility(View.VISIBLE);
                else
                    tv_nodata.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Allplaylistresponse> call, Throwable t) {
                Utils.dismissDialog();
                adapter.notifyDataSetChanged();
                if(playlistModelArrayList.size()==0)
                    tv_nodata.setVisibility(View.VISIBLE);
                else
                    tv_nodata.setVisibility(View.GONE);
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onPlaylistItemclick(PlaylistModel songsModel) {
         Intent intent=new Intent(PlaylistActivity.this,ServerSoundsCompletelistingActivity.class);
         intent.putExtra("songcategoryid",songsModel.getId());
         intent.putExtra("songcategoryname",songsModel.getName());
         startActivity(intent);
    }
}
