package com.app.hulchul.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.adapters.SingleVideoAdapter;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

public class SingleVideoPlayActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.player_container)
    Container container;
    @BindView(R.id.layout_post)
    RelativeLayout layout_post;

    SingleVideoAdapter adapter;
    LinearLayoutManager layoutManager;
    private ArrayList<VideoModel> modelArrayList=new ArrayList<>();
    private String videourl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        layout_post.setOnClickListener(this);
        videourl=getIntent().getStringExtra("videourl");
        layoutManager = new LinearLayoutManager(SingleVideoPlayActivity.this);
        container.setLayoutManager(layoutManager);
        adapter = new SingleVideoAdapter(SingleVideoPlayActivity.this,modelArrayList);
        container.setAdapter(adapter);
        setDataToContainer();
    }

    private void setDataToContainer()
    {
        modelArrayList.clear();
        VideoModel model=new VideoModel();
        model.setVideo_url(videourl);
        modelArrayList.add(model);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_post:
                Utils.callToast(SingleVideoPlayActivity.this,"Post");
                Intent intent=new Intent(SingleVideoPlayActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                finishAffinity();
                break;
        }
    }
}
