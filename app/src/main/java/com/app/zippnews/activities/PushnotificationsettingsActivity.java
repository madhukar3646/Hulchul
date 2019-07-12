package com.app.zippnews.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.app.zippnews.R;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PushnotificationsettingsActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.switch_newfollowers)
    Switch switch_newfollowers;
    @BindView(R.id.switch_newlikes)
    Switch switch_newlikes;
    @BindView(R.id.switch_newcomments)
    Switch switch_newcomments;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushnotificationsettings);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
      connectionDetector=new ConnectionDetector(PushnotificationsettingsActivity.this);
      sessionManagement=new SessionManagement(PushnotificationsettingsActivity.this);
      if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
          userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

      iv_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
      });

      switch_newfollowers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

          }
      });

      switch_newlikes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

          }
      });

      switch_newcomments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

          }
      });
    }
}
