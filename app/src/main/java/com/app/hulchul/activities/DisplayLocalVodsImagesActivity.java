package com.app.hulchul.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.ViewPagerAdapter;
import com.app.hulchul.fragments.ImagesFragment;
import com.app.hulchul.fragments.LocalVideos_fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayLocalVodsImagesActivity extends AppCompatActivity {

    @BindView(R.id.close_btn)
    ImageView close_btn;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_local_vods_images);
        ButterKnife.bind(this);

        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocalVideos_fragment(), "Videos");
        adapter.addFragment(new ImagesFragment(), "Images");
        viewPager.setAdapter(adapter);
    }
}
