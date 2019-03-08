package com.app.hulchul.activities;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.fragments.Discover_fragment;
import com.app.hulchul.fragments.Home_fragment;
import com.app.hulchul.fragments.Me_Fragment;
import com.app.hulchul.fragments.Notification_fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.layout_frame)
    FrameLayout layout_frame;
    @BindView(R.id.layout_home)
    LinearLayout layout_home;
    @BindView(R.id.layout_discover)
    LinearLayout layout_discover;
    @BindView(R.id.layout_video)
    LinearLayout layout_video;
    @BindView(R.id.layout_notification)
    LinearLayout layout_notification;
    @BindView(R.id.layout_me)
    LinearLayout layout_me;

    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.iv_discover)
    ImageView iv_discover;
    @BindView(R.id.tv_discover)
    TextView tv_discover;
    @BindView(R.id.iv_video)
    ImageView iv_video;
    @BindView(R.id.tv_video)
    TextView tv_video;
    @BindView(R.id.iv_notification)
    ImageView iv_notification;
    @BindView(R.id.tv_notification)
    TextView tv_notification;
    @BindView(R.id.iv_me)
    ImageView iv_me;
    @BindView(R.id.tv_me)
    TextView tv_me;
    @BindView(R.id.layout_bottomicons)
    LinearLayout layout_bottomicons;

    private FragmentTransaction ft;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        init();
    }

    private void init()
    {
        layout_home.setOnClickListener(this);
        layout_discover.setOnClickListener(this);
        layout_notification.setOnClickListener(this);
        layout_video.setOnClickListener(this);
        layout_me.setOnClickListener(this);

        iv_home.setImageResource(R.mipmap.home_activeicon);
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        fragment=new Home_fragment();
        ft.add(R.id.layout_frame,fragment);
        ft.commit();
    }

    private void changeFragment(Fragment fragment)
    {
        this.fragment=fragment;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.layout_frame, fragment);
        ft.commit();
        invalidateOptionsMenu();

        if(this.fragment instanceof Home_fragment)
            layout_bottomicons.setBackgroundResource(R.drawable.rectanglebg_screenbgtransparent);
        else
            layout_bottomicons.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.layout_home:
                changeFragment(new Home_fragment());
                setClickableFocus();
                iv_home.setImageResource(R.mipmap.home_activeicon);
                tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.layout_discover:
                changeFragment(new Discover_fragment());
                setClickableFocus();
                iv_discover.setImageResource(R.mipmap.search_active);
                tv_discover.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.layout_video:
                setClickableFocus();
                tv_video.setTextColor(getResources().getColor(R.color.colorPrimary));
                startActivity(new Intent(MainActivity.this,MakingVideoActivity.class));
                break;
            case R.id.layout_notification:
                changeFragment(new Notification_fragment());
                setClickableFocus();
                iv_notification.setImageResource(R.mipmap.notification_active);
                tv_notification.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.layout_me:
                changeFragment(new Me_Fragment());
                setClickableFocus();
                iv_me.setImageResource(R.mipmap.me_active);
                tv_me.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    private void setClickableFocus()
    {
      iv_home.setImageResource(R.mipmap.home_normalicon);
      tv_home.setTextColor(Color.WHITE);
      iv_discover.setImageResource(R.mipmap.search_normal);
      tv_discover.setTextColor(Color.WHITE);
      tv_video.setTextColor(Color.WHITE);
      iv_notification.setImageResource(R.mipmap.notification_normal);
      tv_notification.setTextColor(Color.WHITE);
      iv_me.setImageResource(R.mipmap.me_normal);
      tv_me.setTextColor(Color.WHITE);
    }
}
