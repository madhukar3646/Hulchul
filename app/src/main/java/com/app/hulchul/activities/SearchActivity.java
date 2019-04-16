package com.app.hulchul.activities;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.ViewPagerAdapter;
import com.app.hulchul.fragments.SearchHashtags_fragment;
import com.app.hulchul.fragments.SearchSound_fragment;
import com.app.hulchul.fragments.SearchUsers_Fragment;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.Utils;

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
    private Fragment fragment;
    @BindView(R.id.et_search)
    EditText et_search;
    private ConnectionDetector connectionDetector;
    ViewPagerAdapter adapter;
    private String search_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(SearchActivity.this);
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        iv_backbtn.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
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
              fragment=adapter.getItem(i);
              if(fragment instanceof OnSearchFragmentSelected)
                  ((OnSearchFragmentSelected) fragment).onSearchFragmentSelected(search_key);
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
                String searchkey=et_search.getText().toString();
                if(searchkey.trim().length()==0)
                    Utils.callToast(SearchActivity.this,"Please type something and search");
                else if(connectionDetector.isConnectingToInternet()){
                    this.search_key=searchkey;
                     Fragment fragment=adapter.getItem(viewpager.getCurrentItem());
                     if(fragment instanceof OnSearchFragmentSelected)
                         ((OnSearchFragmentSelected) fragment).onPerformSearch(searchkey);
                }
                else
                    Utils.callToast(SearchActivity.this,getResources().getString(R.string.internet_toast));
                break;
        }
    }

    public interface OnSearchFragmentSelected
    {
        void onSearchFragmentSelected(String searchkey);
        void onPerformSearch(String searchkey);
    }
}
