package com.app.hulchul.activities;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.MySoundsAdapter;
import com.app.hulchul.model.SongsModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySoundsActivity extends AppCompatActivity {

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
        rv_mysounds.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_mysounds.setAdapter(soundsAdapter);
        setMusicDataTolist();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
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

        soundsAdapter.notifyDataSetChanged();
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
}
