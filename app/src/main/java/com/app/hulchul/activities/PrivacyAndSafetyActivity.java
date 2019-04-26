package com.app.hulchul.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.app.hulchul.R;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyAndSafetyActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.switch_privateaccount)
    Switch switch_privateaccount;
    @BindView(R.id.switch_allowothers)
    Switch switch_allowothers;
    @BindView(R.id.layout_commentsoption)
    LinearLayout layout_commentsoption;
    @BindView(R.id.layout_reactoption)
    LinearLayout layout_reactoption;
    @BindView(R.id.layout_duetoption)
    LinearLayout layout_duetoption;
    @BindView(R.id.layout_messagesoption)
    LinearLayout layout_messagesoption;
    @BindView(R.id.layout_viewvideosoption)
    LinearLayout layout_viewvideosoption;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid;
    private String COMMENTS="Who Can Post Comments",REACT="Who Can React to Me",DUET="Who Can Duet With Me",MESSAGE="Who can send me messages",VIEWVIDEOS="Who can view videos I liked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_safety);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
       connectionDetector=new ConnectionDetector(PrivacyAndSafetyActivity.this);
       sessionManagement=new SessionManagement(PrivacyAndSafetyActivity.this);
       userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
       iv_back.setOnClickListener(this);
       layout_commentsoption.setOnClickListener(this);
       layout_reactoption.setOnClickListener(this);
       layout_duetoption.setOnClickListener(this);
       layout_messagesoption.setOnClickListener(this);
       layout_viewvideosoption.setOnClickListener(this);

       switch_privateaccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

           }
       });

       switch_allowothers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

           }
       });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_commentsoption:
                Intent comments=new Intent(PrivacyAndSafetyActivity.this,SafetyOptionsActivity.class);
                comments.putExtra("option",COMMENTS);
                startActivity(comments);
                break;
            case R.id.layout_reactoption:
                Intent react=new Intent(PrivacyAndSafetyActivity.this,SafetyOptionsActivity.class);
                react.putExtra("option",REACT);
                startActivity(react);
                break;
            case R.id.layout_duetoption:
                Intent duet=new Intent(PrivacyAndSafetyActivity.this,SafetyOptionsActivity.class);
                duet.putExtra("option",DUET);
                startActivity(duet);
                break;
            case R.id.layout_messagesoption:
                Intent messages=new Intent(PrivacyAndSafetyActivity.this,SafetyOptionsActivity.class);
                messages.putExtra("option",MESSAGE);
                startActivity(messages);
                break;
            case R.id.layout_viewvideosoption:
                Intent viewvideos=new Intent(PrivacyAndSafetyActivity.this,SafetyOptionsActivity.class);
                viewvideos.putExtra("option",VIEWVIDEOS);
                startActivity(viewvideos);
                break;
        }
    }
}
