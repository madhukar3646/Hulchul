package com.app.hulchul.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.hulchul.R;

import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
    }
}
