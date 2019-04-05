package com.app.hulchul.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.TagsAdapter;
import com.app.hulchul.adapters.TagsListAdapter;
import com.app.hulchul.utils.Utils;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AbuseSelectionActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_rating_label)
    TextView tv_rating_label;
    @BindView(R.id.rating_bar)
    RatingBar rating_bar;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    float ratingcount;
    private String abusereasons[]={"Sexual content","Abusive Language","Abusive Visual","Religious Content"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abuse_selection);
        ButterKnife.bind(this);

        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating>0&&rating<=1){
                    tv_rating_label.setText("Poor");

                }else if(rating>1&&rating<=2){
                    tv_rating_label.setText("Average");

                }else if(rating>2&&rating<=3){
                    tv_rating_label.setText("Good");

                }else if(rating>3&&rating<=4){
                    tv_rating_label.setText("Very Good");

                }else if(rating>4&&rating<=5){
                    tv_rating_label.setText("Excellent");
                }else {
                    tv_rating_label.setText("Give your rating");
                }
            }
        });

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_tags.setLayoutManager(flowLayoutManager);
        List<String> strings=new ArrayList<>();
        for(int i=0;i<=abusereasons.length;i++){
            strings.add(abusereasons[i]);
        }
        rv_tags.setAdapter(new TagsAdapter(AbuseSelectionActivity.this,strings));

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.callToast(AbuseSelectionActivity.this,"Reported Successfully.");
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
