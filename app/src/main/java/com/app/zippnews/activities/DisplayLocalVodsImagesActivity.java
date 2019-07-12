package com.app.zippnews.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.adapters.ViewPagerAdapter;
import com.app.zippnews.fragments.ImagesFragment;
import com.app.zippnews.fragments.LocalVideos_fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayLocalVodsImagesActivity extends AppCompatActivity implements View.OnClickListener,ImagesFragment.onSelectionImagesListener{

    @BindView(R.id.close_btn)
    ImageView close_btn;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.layout_selectedimages)
    RelativeLayout layout_selectedimages;
    @BindView(R.id.tv_selectedcount)
    TextView tv_selectedcount;
    private ArrayList<String> selected_imageslist=new ArrayList<>();
    ImagesFragment imagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_local_vods_images);
        ButterKnife.bind(this);

        tv_selectedcount.setText("Selected(0)");
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        close_btn.setOnClickListener(this);
        layout_selectedimages.setOnClickListener(this);
        layout_selectedimages.setVisibility(View.GONE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocalVideos_fragment(), "Videos");
        imagesFragment=new ImagesFragment();
        imagesFragment.setonSelectionImagesListener(this);
        adapter.addFragment(imagesFragment, "Images");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
               if(i==0)
                   layout_selectedimages.setVisibility(View.GONE);
               else {
                   if(selected_imageslist.size()==0)
                       layout_selectedimages.setVisibility(View.GONE);
                   else
                       layout_selectedimages.setVisibility(View.VISIBLE);
               }
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
            case R.id.close_btn:
                finish();
                break;

            case R.id.layout_selectedimages:
                for(int i=0;i<selected_imageslist.size();i++)
                    Log.e("path is",""+selected_imageslist.get(i));
                break;
        }
    }

    @Override
    public void onImageSelected(ArrayList<String> selected_imageslist) {
        this.selected_imageslist.clear();
        this.selected_imageslist.addAll(selected_imageslist);
        tv_selectedcount.setText("Selected("+selected_imageslist.size()+")");
        if(selected_imageslist.size()==0)
            layout_selectedimages.setVisibility(View.GONE);
        else
            layout_selectedimages.setVisibility(View.VISIBLE);
    }
}
