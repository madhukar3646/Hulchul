package com.app.hulchul.activities;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.HashtagsGridAdapter;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.model.VideosListingResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HashtagSearchresultsActivity extends AppCompatActivity implements View.OnClickListener,HashtagsGridAdapter.OnHashtagItemClickListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_viewscount)
    TextView tv_viewscount;
    @BindView(R.id.tv_videoscount)
    TextView tv_videoscount;
    @BindView(R.id.layout_favourites)
    RelativeLayout layout_favourites;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerview_videos;
    @BindView(R.id.iv_gotorecord)
    ImageView iv_gotorecord;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private String hashtag,videosbasepath,musicbasepath,userid="";
    private HashtagsGridAdapter adapter;
    private ArrayList<VideoModel> discoverhashtagvideosList=new ArrayList<>();
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_searchresults);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        hashtag=getIntent().getStringExtra("hashtag");
        connectionDetector=new ConnectionDetector(HashtagSearchresultsActivity.this);
        sessionManagement=new SessionManagement(HashtagSearchresultsActivity.this);
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        tv_title.setText(hashtag);
        tv_name.setText(hashtag);
        back_btn.setOnClickListener(this);
        layout_favourites.setOnClickListener(this);
        iv_gotorecord.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(HashtagSearchresultsActivity.this,3);
        recyclerview_videos.setLayoutManager(gridLayoutManager);
        adapter=new HashtagsGridAdapter(HashtagSearchresultsActivity.this,discoverhashtagvideosList);
        adapter.setOnHashtagItemClickListener(this);
        recyclerview_videos.setAdapter(adapter);

        if (connectionDetector.isConnectingToInternet()) {
            discoverhashtagvideosList.clear();
            setDataToContainer("20", "0");
        }
        else
            Utils.callToast(HashtagSearchresultsActivity.this, getResources().getString(R.string.internet_toast));

        recyclerview_videos.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
                Log.e("pos"+pos,"numitems "+numItems);
                if((pos+1)>=numItems)
                {
                    if (connectionDetector.isConnectingToInternet()) {
                        setDataToContainer("20", ""+numItems);
                    } else
                        Utils.callToast(HashtagSearchresultsActivity.this, getResources().getString(R.string.internet_toast));
                }
            }
        });
    }

    private void setDataToContainer(String limit,String offset){
        Utils.showDialog(HashtagSearchresultsActivity.this);
        Call<VideosListingResponse> call= RetrofitApis.Factory.createTemp(HashtagSearchresultsActivity.this).videoByHashTag(userid,hashtag,limit,offset);
        call.enqueue(new Callback<VideosListingResponse>() {
            @Override
            public void onResponse(Call<VideosListingResponse> call, Response<VideosListingResponse> response) {
                Utils.dismissDialog();
                VideosListingResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus() == 1) {
                        if (body.getVideos() != null && body.getVideos().size() > 0) {
                            discoverhashtagvideosList.addAll(body.getVideos());
                            videosbasepath=body.getUrl();
                            musicbasepath=body.getSongurl();
                            adapter.notifyDataSetChanged();
                            tv_nodata.setVisibility(View.GONE);
                        }
                    } else {
                        if(discoverhashtagvideosList.size()==0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            Utils.callToast(HashtagSearchresultsActivity.this, body.getMessage());
                        }
                    }
                }
                else {
                    Utils.callToast(HashtagSearchresultsActivity.this,"null response came");
                }
            }

            @Override
            public void onFailure(Call<VideosListingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_favourites:
                break;
            case R.id.iv_gotorecord:
                break;
        }
    }

    @Override
    public void onHashtagitemClick(ArrayList<VideoModel> discoverhashtagvideosList, int pos) {

        Intent intent=new Intent(HashtagSearchresultsActivity.this, PlayvideosCategorywise_Activity.class);
        intent.putParcelableArrayListExtra("videos",discoverhashtagvideosList);
        intent.putExtra("position",pos);
        intent.putExtra("videosbasepath",videosbasepath);
        intent.putExtra("musicbasepath",musicbasepath);
        startActivity(intent);
    }
}
