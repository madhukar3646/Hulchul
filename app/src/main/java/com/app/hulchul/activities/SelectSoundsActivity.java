package com.app.hulchul.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.hulchul.R;
import com.app.hulchul.adapters.PlaylistCategoriesAdapter;
import com.app.hulchul.adapters.SimplePlayerViewHolder;
import com.app.hulchul.adapters.SongslistContainerAdapter;
import com.app.hulchul.adapters.TrendingSoundsBannersAdapter;
import com.app.hulchul.model.ServerSong;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.Songlistmodel;
import com.app.hulchul.model.Soundbanners;
import com.app.hulchul.model.SoundsCategorylist;
import com.app.hulchul.model.SoundsModuleResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSoundsActivity extends AppCompatActivity implements View.OnClickListener,TrendingSoundsBannersAdapter.OnBannerClickListener,
        PlaylistCategoriesAdapter.OnPlaylistItemClickListener,SongslistContainerAdapter.OnviewAllsongsClickListener{

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
    @BindView(R.id.layout_viewall)
    RelativeLayout layout_viewall;

    private TrendingSoundsBannersAdapter banneradapter;
    private PlaylistCategoriesAdapter playlistCategoriesAdapter;
    private SongslistContainerAdapter songslistContainerAdapter;

    private ArrayList<Soundbanners> soundbannerslist=new ArrayList<>();
    private ArrayList<ServerSong> songsArraylist=new ArrayList<>();
    private ArrayList<SoundsCategorylist> soundsCategorylist=new ArrayList<>();
    private String musicbasepath;
    private Dialog dialog;

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
        layout_search.setOnClickListener(this);
        layout_viewall.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        rv_soundgroups.setLayoutManager(new LinearLayoutManager(SelectSoundsActivity.this,LinearLayoutManager.HORIZONTAL, false));
        banneradapter=new TrendingSoundsBannersAdapter(SelectSoundsActivity.this,soundbannerslist);
        banneradapter.setOnBannerClickListener(this);
        rv_soundgroups.setAdapter(banneradapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(SelectSoundsActivity.this,3);
        rv_playlistcategory.setLayoutManager(gridLayoutManager);
        playlistCategoriesAdapter=new PlaylistCategoriesAdapter(SelectSoundsActivity.this,soundsCategorylist);
        playlistCategoriesAdapter.setOnPlaylistItemClickListener(this);
        rv_playlistcategory.setAdapter(playlistCategoriesAdapter);

        rv_songslistcontainer.setLayoutManager(new LinearLayoutManager(SelectSoundsActivity.this,LinearLayoutManager.VERTICAL, false));
        songslistContainerAdapter=new SongslistContainerAdapter(SelectSoundsActivity.this,songsArraylist);
        songslistContainerAdapter.setOnviewAllsongsClickListener(this);
        rv_songslistcontainer.setAdapter(songslistContainerAdapter);

        if(connectionDetector.isConnectingToInternet())
        {
            setDataToContainer("9","0");
        }
        else
            Utils.callToast(SelectSoundsActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_mysounds:
                startActivity(new Intent(SelectSoundsActivity.this,MySoundsActivity.class));
                break;
            case R.id.layout_search:
                startActivity(new Intent(SelectSoundsActivity.this, SearchActivity.class));
                break;
            case R.id.layout_viewall:
                startActivity(new Intent(SelectSoundsActivity.this,PlaylistActivity.class));
                break;
            case R.id.iv_close:
                onBackPressed();
                break;
        }
    }

    private void setDataToContainer(String limit,String offset){
        String userid="";
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        Utils.showDialog(SelectSoundsActivity.this);
        Call<SoundsModuleResponse> call= RetrofitApis.Factory.createTemp(SelectSoundsActivity.this).songCategoryList(userid,limit,offset);
        call.enqueue(new Callback<SoundsModuleResponse>() {
            @Override
            public void onResponse(Call<SoundsModuleResponse> call, Response<SoundsModuleResponse> response) {
                Utils.dismissDialog();
                SoundsModuleResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1)
                    {
                      musicbasepath=body.getSongurl();
                      songslistContainerAdapter.setMusicBasepath(musicbasepath);
                      if(body.getData().getTopbanners()!=null)
                          soundbannerslist.addAll(body.getData().getTopbanners());
                      if(body.getData().getCategoryList()!=null)
                          soundsCategorylist.addAll(body.getData().getCategoryList());
                      if(body.getData().getSonglist()!=null)
                          songsArraylist.addAll(getCompleteSongslist(body.getData().getSonglist()));
                    } else {
                        Utils.callToast(SelectSoundsActivity.this,""+body.getMessage());
                    }
                    banneradapter.notifyDataSetChanged();
                    songslistContainerAdapter.notifyDataSetChanged();
                    playlistCategoriesAdapter.notifyDataSetChanged();
                }
                else {
                    banneradapter.notifyDataSetChanged();
                    songslistContainerAdapter.notifyDataSetChanged();
                    playlistCategoriesAdapter.notifyDataSetChanged();
                    Utils.callToast(SelectSoundsActivity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<SoundsModuleResponse> call, Throwable t) {
                Utils.dismissDialog();
                banneradapter.notifyDataSetChanged();
                songslistContainerAdapter.notifyDataSetChanged();
                playlistCategoriesAdapter.notifyDataSetChanged();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    private ArrayList<ServerSong> getCompleteSongslist(List<Songlistmodel> list)
    {
        ArrayList<ServerSong> songslist=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            ServerSong song=new ServerSong();
            song.setHeader(true);
            song.setSongId(list.get(i).getId());
            song.setName(list.get(i).getName());
            songslist.add(song);
            songslist.addAll(list.get(i).getSongs());
        }
        return songslist;
    }

    @Override
    public void onBannerClick(Soundbanners model) {
        Intent intent=new Intent(SelectSoundsActivity.this, SoundsSearchresultsActivity.class);
        intent.putExtra("soundname",model.getName());
        intent.putExtra("songid",model.getSongId());
        startActivity(intent);
    }

    @Override
    public void onPlayListitemclick(SoundsCategorylist playlistitem) {
        Intent intent=new Intent(SelectSoundsActivity.this,ServerSoundsCompletelistingActivity.class);
        intent.putExtra("songcategoryid",playlistitem.getId());
        intent.putExtra("songcategoryname",playlistitem.getName());
        startActivity(intent);
    }

    @Override
    public void onViewAllSongs(ServerSong model) {
        Intent intent=new Intent(SelectSoundsActivity.this,ServerSoundsCompletelistingActivity.class);
        intent.putExtra("songcategoryid",model.getSongId());
        intent.putExtra("songcategoryname",model.getName());
        startActivity(intent);
    }

    @Override
    public void onSoundSelected(ServerSong songsModel) {
        showProgress();
        new Thread(new Runnable() {
            public void run() {
                downloadFile(songsModel);
            }
        }).start();
    }

    void showProgress(){
        dialog = new Dialog(SelectSoundsActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.progressdialog_update);
        dialog.setCancelable(false);
        dialog.show();
    }

    public String getSoundsDownloadpath()
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchulmusic";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "hulchulsounds" + ".mp3");
        return file.getAbsolutePath();
    }

    void downloadFile(final ServerSong songsModel){

        try {
            URL url = new URL(musicbasepath+songsModel.getFile());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File file = new File(getSoundsDownloadpath());

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading

            runOnUiThread(new Runnable() {
                public void run() {
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    goToVideoScreen(songsModel);
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SelectSoundsActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToVideoScreen(ServerSong songsModel)
    {
        Intent intent=new Intent(SelectSoundsActivity.this,MakingVideoActivity.class);
        intent.putExtra("songpath",getSoundsDownloadpath());
        intent.putExtra("songid",songsModel.getSongId());
        intent.putExtra("duration",songsModel.getDuration());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        songslistContainerAdapter.stopPlayerFromActivity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        songslistContainerAdapter.stopPlayerFromActivity();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        songslistContainerAdapter.stopPlayerFromActivity();
        super.onStop();
    }

    @Override
    public void onFavouriteClick(ServerSong song,int pos) {

        if(connectionDetector.isConnectingToInternet())
        {
            if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
              addTofavourites(sessionManagement.getValueFromPreference(SessionManagement.USERID),"song",song.getSongId(),pos);
            else
                startActivity(new Intent(SelectSoundsActivity.this,LoginLandingActivity.class));
        }
        else
            Utils.callToast(SelectSoundsActivity.this,getResources().getString(R.string.internet_toast));
    }

    private void addTofavourites(String userid, String type, String favouriteid,int pos){
        Utils.showDialog(SelectSoundsActivity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(SelectSoundsActivity.this).addFavourite(userid,type,favouriteid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    songslistContainerAdapter.updateFavourite(pos);
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("addfav onFailure",""+t.getMessage());
            }
        });
    }
}
