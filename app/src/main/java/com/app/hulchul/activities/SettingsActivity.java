package com.app.hulchul.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.layout_pushnotifications)
    LinearLayout layout_pushnotifications;
    @BindView(R.id.layout_aboutus)
    LinearLayout layout_aboutus;
    @BindView(R.id.layout_privacyandsafety)
    LinearLayout layout_privacyandsafety;
    @BindView(R.id.layout_helpcentre)
    LinearLayout layout_helpcentre;
    @BindView(R.id.layout_termsofuse)
    LinearLayout layout_termsofuse;
    @BindView(R.id.layout_privacypolicy)
    LinearLayout layout_privacypolicy;
    @BindView(R.id.layout_copyrightspolicy)
    LinearLayout layout_copyrightspolicy;
    @BindView(R.id.layout_logout)
    LinearLayout layout_logout;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String userid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
      connectionDetector=new ConnectionDetector(SettingsActivity.this);
      sessionManagement=new SessionManagement(SettingsActivity.this);
      if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
          userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
      layout_pushnotifications.setOnClickListener(this);
      layout_privacyandsafety.setOnClickListener(this);
      layout_aboutus.setOnClickListener(this);
      layout_copyrightspolicy.setOnClickListener(this);
      layout_helpcentre.setOnClickListener(this);
      layout_termsofuse.setOnClickListener(this);
      layout_privacypolicy.setOnClickListener(this);
      layout_copyrightspolicy.setOnClickListener(this);
      layout_logout.setOnClickListener(this);
      iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_pushnotifications:
                startActivity(new Intent(SettingsActivity.this,PushnotificationsettingsActivity.class));
                break;
            case R.id.layout_aboutus:
                break;
            case R.id.layout_privacyandsafety:
                startActivity(new Intent(SettingsActivity.this,PrivacyAndSafetyActivity.class));
                break;
            case R.id.layout_helpcentre:
                break;
            case R.id.layout_termsofuse:
                break;
            case R.id.layout_privacypolicy:
                break;
            case R.id.layout_copyrightspolicy:
                break;
            case R.id.layout_logout:
                displayExitDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void displayExitDialog()
    {
        final Dialog dialog=new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exitdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RelativeLayout layout_yes=(RelativeLayout) dialog.findViewById(R.id.layout_yes);
        RelativeLayout layout_no=(RelativeLayout)dialog.findViewById(R.id.layout_no);
        TextView tv_title=(TextView)dialog.findViewById(R.id.tv_title);
        tv_title.setText("Are you sure you want to logout?");
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);
        tv_no.setText("No");
        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        tv_yes.setText("Yes");

        layout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManagement.logoutUser();
                dialog.dismiss();
                Intent intent=new Intent(SettingsActivity.this, LoginLandingActivity.class);
                startActivity(intent);
                finish();
                finishAffinity();
            }
        });

        layout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
