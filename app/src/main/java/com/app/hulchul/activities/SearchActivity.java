package com.app.hulchul.activities;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.ViewPagerAdapter;
import com.app.hulchul.fragments.SearchHashtags_fragment;
import com.app.hulchul.fragments.SearchSound_fragment;
import com.app.hulchul.fragments.SearchUsers_Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_search)
    TextView tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchUsers_Fragment(), "Users");
        adapter.addFragment(new SearchHashtags_fragment(), "Hashtags");
        adapter.addFragment(new SearchSound_fragment(), "Sounds");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
              Fragment fragment=adapter.getItem(i);
              if(fragment instanceof OnSearchFragmentSelected)
                  ((OnSearchFragmentSelected) fragment).onSearchFragmentSelected();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_backbtn:
                finish();
                break;
            case R.id.tv_search:
                break;
        }
    }

    public interface OnSearchFragmentSelected
    {
        void onSearchFragmentSelected();
    }
}
