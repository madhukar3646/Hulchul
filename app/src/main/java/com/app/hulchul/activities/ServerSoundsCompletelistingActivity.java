package com.app.hulchul.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hulchul.R;
import com.app.hulchul.adapters.ServerSoundsAdapter;
import com.app.hulchul.model.ServerSong;
import com.app.hulchul.model.ServerSoundsResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    private Dialog dialog;

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
    public void onSoundSelected(final ServerSong songsModel) {

        showProgress();
        new Thread(new Runnable() {
            public void run() {
                downloadFile(songsModel);
            }
        }).start();
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

    public void goToVideoScreen(ServerSong songsModel)
    {
        Intent intent=new Intent(ServerSoundsCompletelistingActivity.this,MakingVideoActivity.class);
        //intent.putExtra("songpath",songsBasepath+songsModel.getFile());
        intent.putExtra("songpath",getSoundsDownloadpath());
        intent.putExtra("songid",songsModel.getSongId());
        intent.putExtra("duration",songsModel.getDuration());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    void downloadFile(final ServerSong songsModel){

        try {
            URL url = new URL(songsBasepath+songsModel.getFile());
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
                Toast.makeText(ServerSoundsCompletelistingActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(){
        dialog = new Dialog(ServerSoundsCompletelistingActivity.this,
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
}
