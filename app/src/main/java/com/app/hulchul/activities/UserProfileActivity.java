package com.app.hulchul.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.VideothumbnailsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.iv_unfollow)
    ImageView iv_unfollow;
    @BindView(R.id.layout_follow)
    RelativeLayout layout_follow;
    @BindView(R.id.tv_following)
    TextView tv_following;
    @BindView(R.id.tv_followers)
    TextView tv_followers;
    @BindView(R.id.tv_hearts)
    TextView tv_hearts;
    @BindView(R.id.tv_friends)
    TextView tv_friends;
    @BindView(R.id.tv_videos)
    TextView tv_videos;
    @BindView(R.id.tv_biodata)
    TextView tv_biodata;
    @BindView(R.id.layout_yourvideos)
    LinearLayout layout_yourvideos;
    @BindView(R.id.layout_hearts)
    LinearLayout layout_hearts;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;

    @BindView(R.id.iv_myvideo)
    ImageView iv_myvideo;
    @BindView(R.id.tv_myvideos)
    TextView tv_myvideos;
    @BindView(R.id.iv_myhearts)
    ImageView iv_myhearts;
    @BindView(R.id.tv_myhearts)
    TextView tv_myhearts;

    private VideothumbnailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        layout_follow.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        iv_unfollow.setOnClickListener(this);
        layout_yourvideos.setOnClickListener(this);
        layout_hearts.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        setClickableFocus(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserProfileActivity.this,3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        recyclerview_videos.setNestedScrollingEnabled(false);
        adapter=new VideothumbnailsAdapter(UserProfileActivity.this);
        recyclerview_videos.setAdapter(adapter);
    }

    private void setClickableFocus(boolean isMyvideos)
    {
        if(isMyvideos) {
            iv_myvideo.setImageResource(R.mipmap.list_video_black);
            tv_myvideos.setTextColor(Color.BLACK);
            iv_myhearts.setImageResource(R.mipmap.hearts_gray);
            tv_myhearts.setTextColor(Color.GRAY);
        }
        else {
            iv_myvideo.setImageResource(R.mipmap.list_video_gray);
            tv_myvideos.setTextColor(Color.GRAY);
            iv_myhearts.setImageResource(R.mipmap.hearts_black);
            tv_myhearts.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_follow:
                break;
            case R.id.tv_message:
                break;
            case R.id.iv_unfollow:
                break;
            case R.id.layout_yourvideos:
                setClickableFocus(true);
                break;
            case R.id.layout_hearts:
                setClickableFocus(false);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
