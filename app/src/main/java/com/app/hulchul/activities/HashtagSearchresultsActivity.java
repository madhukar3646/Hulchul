package com.app.hulchul.activities;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.VideothumbnailsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HashtagSearchresultsActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_viewscount)
    TextView tv_viewscount;
    @BindView(R.id.tv_videoscount)
    TextView tv_videoscount;
    @BindView(R.id.layout_favourites)
    RelativeLayout layout_favourites;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;
    @BindView(R.id.iv_gotorecord)
    ImageView iv_gotorecord;
    private VideothumbnailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_searchresults);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        back_btn.setOnClickListener(this);
        layout_favourites.setOnClickListener(this);
        iv_gotorecord.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(HashtagSearchresultsActivity.this,3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        adapter=new VideothumbnailsAdapter(HashtagSearchresultsActivity.this);
        recyclerview_videos.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_favourites:
                break;
            case R.id.iv_gotorecord:
                break;
        }
    }
}
