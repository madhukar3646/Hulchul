package com.app.zippnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonEmptyActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_empty);
        ButterKnife.bind(this);
        tv_title.setText(getIntent().getStringExtra("common"));
    }
}
