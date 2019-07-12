package com.app.zippnews.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SafetyOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.layout_everyone)
    LinearLayout layout_everyone;
    @BindView(R.id.layout_friends)
    LinearLayout layout_friends;
    @BindView(R.id.layout_off)
    LinearLayout layout_off;
    @BindView(R.id.layout_visibletomyself)
    LinearLayout layout_visibletomyself;
    @BindView(R.id.iv_everyone)
    ImageView iv_everyone;
    @BindView(R.id.iv_friends)
    ImageView iv_friends;
    @BindView(R.id.iv_off)
    ImageView iv_off;
    @BindView(R.id.iv_visibletomyself)
    ImageView iv_visibletomyself;
    private String COMMENTS="Who Can Post Comments",REACT="Who Can React to Me",DUET="Who Can Duet With Me",MESSAGE="Who can send me messages",VIEWVIDEOS="Who can view videos I liked";
    private String option;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_options);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
      connectionDetector=new ConnectionDetector(SafetyOptionsActivity.this);
      sessionManagement=new SessionManagement(SafetyOptionsActivity.this);
      option=getIntent().getStringExtra("option");
      tv_title.setText(option);
      if(option.equalsIgnoreCase(COMMENTS)|| option.equalsIgnoreCase(REACT) || option.equalsIgnoreCase(DUET))
      {
          layout_everyone.setVisibility(View.VISIBLE);
          layout_friends.setVisibility(View.VISIBLE);
          layout_off.setVisibility(View.VISIBLE);
          layout_visibletomyself.setVisibility(View.GONE);
      }
      else if(option.equalsIgnoreCase(MESSAGE)){
          layout_everyone.setVisibility(View.GONE);
          layout_friends.setVisibility(View.VISIBLE);
          layout_off.setVisibility(View.VISIBLE);
          layout_visibletomyself.setVisibility(View.GONE);
      }
      else if(option.equalsIgnoreCase(VIEWVIDEOS))
      {
          layout_everyone.setVisibility(View.VISIBLE);
          layout_friends.setVisibility(View.GONE);
          layout_off.setVisibility(View.GONE);
          layout_visibletomyself.setVisibility(View.VISIBLE);
      }
      iv_back.setOnClickListener(this);
      layout_everyone.setOnClickListener(this);
      layout_friends.setOnClickListener(this);
      layout_off.setOnClickListener(this);
      layout_visibletomyself.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_everyone:
                iv_everyone.setVisibility(View.VISIBLE);
                iv_friends.setVisibility(View.GONE);
                iv_off.setVisibility(View.GONE);
                iv_visibletomyself.setVisibility(View.GONE);
                break;
            case R.id.layout_friends:
                iv_everyone.setVisibility(View.GONE);
                iv_friends.setVisibility(View.VISIBLE);
                iv_off.setVisibility(View.GONE);
                iv_visibletomyself.setVisibility(View.GONE);
                break;
            case R.id.layout_off:
                iv_everyone.setVisibility(View.GONE);
                iv_friends.setVisibility(View.GONE);
                iv_off.setVisibility(View.VISIBLE);
                iv_visibletomyself.setVisibility(View.GONE);
                break;
            case R.id.layout_visibletomyself:
                iv_everyone.setVisibility(View.GONE);
                iv_friends.setVisibility(View.GONE);
                iv_off.setVisibility(View.GONE);
                iv_visibletomyself.setVisibility(View.VISIBLE);
                break;
        }
    }
}
