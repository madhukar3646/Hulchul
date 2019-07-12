package com.app.zippnews.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.adapters.DraftsthumbnailsAdapter;
import com.app.zippnews.model.DraftModel;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftsActivity extends AppCompatActivity implements View.OnClickListener, DraftsthumbnailsAdapter.VideoSelectedListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_drafts)
    RecyclerView rv_drafts;
    @BindView(R.id.tv_select)
    TextView tv_select;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.layout_delete)
    RelativeLayout layout_delete;
    DraftsthumbnailsAdapter adapter;
    private String SELECT="Select";
    private String CANCEL="Cancel";
    private ArrayList<DraftModel> draftModelArrayList=new ArrayList<>();
    private ArrayList<String> selectedlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        back_btn.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        layout_delete.setVisibility(View.GONE);
        tv_select.setText(SELECT);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(DraftsActivity.this,3);
        rv_drafts.setLayoutManager(gridLayoutManager);
        rv_drafts.setNestedScrollingEnabled(false);
        adapter=new DraftsthumbnailsAdapter(DraftsActivity.this,draftModelArrayList);
        adapter.setOnVideoSelectedListener(this);
        rv_drafts.setAdapter(adapter);
        setDataTotheAdapter();
    }

    private void setDataTotheAdapter()
    {
        draftModelArrayList.clear();
        adapter.notifyDataSetChanged();
        DraftModel model;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchuldrafts";
        File dir = new File(file_path);
        if(dir.exists())
        {
            File[] files=dir.listFiles();
            for(File file:files)
            {
              model=new DraftModel();
              model.setVideopath(file.getAbsolutePath());
              model.setSelected(false);
              draftModelArrayList.add(model);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_select:
                if(tv_select.getText().toString().equalsIgnoreCase(SELECT))
                {
                   tv_select.setText(CANCEL);
                   adapter.setVideosSelection(true);
                   adapter.notifyDataSetChanged();
                }
                else {
                    tv_select.setText(SELECT);
                    selectedlist.clear();
                    layout_delete.setVisibility(View.GONE);
                    adapter.setVideosSelection(false);
                }
                break;
            case R.id.tv_delete:
                for(int i=0;i<selectedlist.size();i++)
                {
                    new File(selectedlist.get(i)).delete();
                }
                selectedlist.clear();
                tv_delete.setText("Delete("+selectedlist.size()+")");
                layout_delete.setVisibility(View.GONE);
                setDataTotheAdapter();
                break;
        }
    }

    @Override
    public void onVideoSelected(String path) {
        if(!selectedlist.contains(path))
          selectedlist.add(path);
        else
            selectedlist.remove(path);
        tv_delete.setText("Delete("+selectedlist.size()+")");
        if(selectedlist.size()>0)
            layout_delete.setVisibility(View.VISIBLE);
        else
            layout_delete.setVisibility(View.GONE);
    }

    @Override
    public void onGoToPostClicked(String path) {
        goToPostingScreen(null,path,"");
    }

    private void goToPostingScreen(String musicpath,String path,String songid)
    {
        Intent intent=new Intent(DraftsActivity.this,VideoPosting_Activity.class);
        intent.putExtra("musicpath",musicpath);
        intent.putExtra("videopath",path);
        intent.putExtra("songid",songid);
        startActivity(intent);
    }
}
