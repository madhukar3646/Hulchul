package com.app.zippnews.activities;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.app.zippnews.R;
import com.app.zippnews.adapters.TagsAdapter;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.servicerequestmodels.AbuseResponse;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbuseSelectionActivity extends AppCompatActivity implements TagsAdapter.OnTagSelectionListener{

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_rating_label)
    TextView tv_rating_label;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    float ratingcount;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    private String abusecontent,videoid,userid;
    private TagsAdapter tagsAdapter;
    private String abusereasons[]={"Sexual content","Abusive Language","Abusive Visual","Religious Content"};
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abuse_selection);
        ButterKnife.bind(this);

        if(getIntent()!=null)
            videoid=getIntent().getStringExtra("videoid");

        connectionDetector=new ConnectionDetector(AbuseSelectionActivity.this);
        sessionManagement=new SessionManagement(AbuseSelectionActivity.this);
        userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
              ratingcount=(i/10f);
                tv_rating_label.setText(""+ratingcount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        rv_tags.setLayoutManager(flowLayoutManager);
        List<String> strings=new ArrayList<>();
        for(int i=0;i<abusereasons.length;i++){
            strings.add(abusereasons[i]);
        }
        tagsAdapter=new TagsAdapter(AbuseSelectionActivity.this,strings);
        tagsAdapter.setListener(this);
        rv_tags.setAdapter(tagsAdapter);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingcount<=0)
                    Utils.callToast(AbuseSelectionActivity.this,"Please give your rating.");
                else if(abusecontent==null)
                    Utils.callToast(AbuseSelectionActivity.this,"Please select reason.");
               else {
                    if(connectionDetector.isConnectingToInternet())
                    {
                        reportVideo(userid,videoid,""+ratingcount,abusecontent);
                    }
                    else
                        Utils.callToast(AbuseSelectionActivity.this,getResources().getString(R.string.internet_toast));
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onCategorySelected(String category) {
           abusecontent=category;
    }

    private void reportVideo(String userid,String videoid,String rating,String report){
        Utils.showDialog(AbuseSelectionActivity.this);
        Call<AbuseResponse> call= RetrofitApis.Factory.createTemp(AbuseSelectionActivity.this).reportVideo(userid,videoid,rating,report);
        call.enqueue(new Callback<AbuseResponse>() {
            @Override
            public void onResponse(Call<AbuseResponse> call, Response<AbuseResponse> response) {
                Utils.dismissDialog();
                AbuseResponse body=response.body();
                if(body!=null) {
                    if(body.getStatus()==0) {
                        Utils.callToast(AbuseSelectionActivity.this, "Reported successfully.");
                        finish();
                    }
                    else {
                        Utils.callToast(AbuseSelectionActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(AbuseSelectionActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<AbuseResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }
}
