package com.app.hulchul.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.ServerSoundsAdapter;
import com.app.hulchul.model.ServerSong;
import com.app.hulchul.model.ServerSoundsResponse;
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

public class ServerSoundsCompletelistingActivity extends AppCompatActivity implements ServerSoundsAdapter.OnSoundSelectionListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_mysounds)
    RecyclerView rv_mysounds;
    private ArrayList<ServerSong> songsModelArrayList=new ArrayList<>();
    private ServerSoundsAdapter serverSoundsAdapter;
    private String songsBasepath;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_sounds_completelisting);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(ServerSoundsCompletelistingActivity.this);
        sessionManagement=new SessionManagement(ServerSoundsCompletelistingActivity.this);

        serverSoundsAdapter=new ServerSoundsAdapter(ServerSoundsCompletelistingActivity.this,songsModelArrayList);
        serverSoundsAdapter.setSoundSelectionListener(this);
        rv_mysounds.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_mysounds.setAdapter(serverSoundsAdapter);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(connectionDetector.isConnectingToInternet())
        {
            setDataToContainer();
        }
        else
            Utils.callToast(ServerSoundsCompletelistingActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onSoundSelected(ServerSong songsModel) {

        Intent intent=new Intent(ServerSoundsCompletelistingActivity.this,MakingVideoActivity.class);
        intent.putExtra("songpath",songsBasepath+songsModel.getFile());
        intent.putExtra("songid",songsModel.getSongId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setDataToContainer(){
        Utils.showDialog(ServerSoundsCompletelistingActivity.this);
        Call<ServerSoundsResponse> call= RetrofitApis.Factory.createTemp(ServerSoundsCompletelistingActivity.this).serverSoundsListingService();
        call.enqueue(new Callback<ServerSoundsResponse>() {
            @Override
            public void onResponse(Call<ServerSoundsResponse> call, Response<ServerSoundsResponse> response) {
                Utils.dismissDialog();
                ServerSoundsResponse body=response.body();
                if(body.getStatus()==1){
                    songsBasepath=body.getUrl();
                    if(body.getSongs()!=null && body.getSongs().size()>0) {
                        songsModelArrayList.clear();
                        songsModelArrayList.addAll(body.getSongs());
                    }
                    serverSoundsAdapter.setSongsBasepath(songsBasepath);
                    serverSoundsAdapter.notifyDataSetChanged();
                }
                else {
                    Utils.callToast(ServerSoundsCompletelistingActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerSoundsResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        serverSoundsAdapter.stopPlayerFromActivity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        serverSoundsAdapter.stopPlayerFromActivity();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        serverSoundsAdapter.stopPlayerFromActivity();
        super.onStop();
    }

}
