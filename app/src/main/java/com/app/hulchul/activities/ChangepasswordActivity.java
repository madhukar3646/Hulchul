package com.app.hulchul.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.hulchul.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangepasswordActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
