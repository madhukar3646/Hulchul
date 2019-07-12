package com.app.zippnews.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.zippnews.R;
import com.app.zippnews.adapters.MySoundsAdapter;
import com.app.zippnews.model.SongsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySoundsActivity extends AppCompatActivity implements MySoundsAdapter.OnSoundSelectionListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_mysounds)
    RecyclerView rv_mysounds;
    private ArrayList<SongsModel> songsModelArrayList=new ArrayList<>();
    private MySoundsAdapter soundsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sounds);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        soundsAdapter=new MySoundsAdapter(MySoundsActivity.this,songsModelArrayList);
        soundsAdapter.setSoundSelectionListener(this);
        rv_mysounds.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_mysounds.setAdapter(soundsAdapter);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
        new LoadMusicFiles().execute("");
    }

    private void setMusicDataTolist()
    {
        SongsModel songsModel;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        while(cursor.moveToNext()){
            if(cursor.getString(3).endsWith(".mp3")||cursor.getString(3).endsWith(".MP3"))
            {
                songsModel = new SongsModel();
                songsModel.setDisplayname(cursor.getString(4));
                songsModel.setSongartist(cursor.getString(0));
                songsModel.setSongtitle(cursor.getString(2));
                songsModel.setSongpath(cursor.getString(3));
                //Log.e("song path is",""+cursor.getString(3));
                songsModelArrayList.add(songsModel);
            }
        }
    }

    @Override
    public void onBackPressed() {
        soundsAdapter.stopPlayerFromActivity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        soundsAdapter.stopPlayerFromActivity();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        soundsAdapter.stopPlayerFromActivity();
        super.onStop();
    }

    @Override
    public void onSoundSelected(SongsModel songsModel) {
        Intent intent=new Intent(MySoundsActivity.this,MakingVideoActivity.class);
        intent.putExtra("songpath",songsModel.getSongpath());
        intent.putExtra("duration","15000");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class LoadMusicFiles extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MySoundsActivity.this,
                    "Loading please wait...",null);
        }

        @Override
        protected String doInBackground(String... strings) {

            setMusicDataTolist();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            soundsAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }
}
