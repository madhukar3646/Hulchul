package com.app.hulchul.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.ViewPagerAdapter;
import com.app.hulchul.fragments.FavouiteHashtags;
import com.app.hulchul.fragments.FavouriteSounds;
import com.app.hulchul.fragments.FavouriteVideos;
import com.app.hulchul.utils.ConnectionDetector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesActivity extends AppCompatActivity {

    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private Fragment fragment;
    private ConnectionDetector connectionDetector;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(FavouritesActivity.this);
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        iv_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavouriteVideos(), "Videos");
        adapter.addFragment(new FavouiteHashtags(), "Hashtags");
        adapter.addFragment(new FavouriteSounds(), "Sounds");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                fragment=adapter.getItem(i);
                if(fragment instanceof OnFavouritesFragmentSelected)
                    ((OnFavouritesFragmentSelected) fragment).onFavouriteFragmentSelected();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public interface OnFavouritesFragmentSelected
    {
        void onFavouriteFragmentSelected();
    }
}
