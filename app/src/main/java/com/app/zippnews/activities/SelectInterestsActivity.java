package com.app.zippnews.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.adapters.TagsListAdapter;
import com.app.zippnews.model.Tagmodel;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectInterestsActivity extends AppCompatActivity implements View.OnClickListener,TagsListAdapter.OnTagclicklistener{

    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.iv_continue)
    ImageView iv_continue;
    @BindView(R.id.layout_continue)
    RelativeLayout layout_continue;
    @BindView(R.id.layout_continuedumny)
    RelativeLayout layout_continuedumny;
    @BindView(R.id.tv_skip)
    TextView tv_skip;
    @BindView(R.id.rv_tagslist)
    RecyclerView rv_tagslist;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private TagsListAdapter tagsListAdapter;
    private ArrayList<Tagmodel> tagslist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interests);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(SelectInterestsActivity.this);
        sessionManagement=new SessionManagement(SelectInterestsActivity.this);
        iv_backbtn.setOnClickListener(this);
        layout_continue.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
        layout_continuedumny.setVisibility(View.VISIBLE);
        layout_continue.setVisibility(View.GONE);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_tagslist.setLayoutManager(flowLayoutManager);
        tagsListAdapter=new TagsListAdapter(SelectInterestsActivity.this,tagslist);
        tagsListAdapter.setOnTagclicklistener(this);
        rv_tagslist.setAdapter(tagsListAdapter);
        setTagsData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_backbtn:
                finish();
                break;
            case R.id.layout_continue:
                startActivity(new Intent(SelectInterestsActivity.this,MainActivity.class));
                finish();
                finishAffinity();
                break;
            case R.id.tv_skip:
                startActivity(new Intent(SelectInterestsActivity.this,MainActivity.class));
                finish();
                finishAffinity();
                break;
        }
    }

    private void setTagsData()
    {
        Tagmodel tagmodel=new Tagmodel();
        tagmodel.setTagname("Sports");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Movies");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Comedy");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Kids");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Baby Care");
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Health Care");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Beauty Tips");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Sports1");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Movies1");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Comedy1");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Kids1");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Baby Care1");
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Health Care1");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagmodel=new Tagmodel();
        tagmodel.setTagname("Beauty Tips1");
        tagmodel.setSelected(false);
        tagslist.add(tagmodel);

        tagsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTagsSelected(ArrayList<String> selectedTags) {

        if(selectedTags.size()>0)
        {
            layout_continuedumny.setVisibility(View.GONE);
            layout_continue.setVisibility(View.VISIBLE);
        }
        else {
            layout_continuedumny.setVisibility(View.VISIBLE);
            layout_continue.setVisibility(View.GONE);
        }
    }
}
