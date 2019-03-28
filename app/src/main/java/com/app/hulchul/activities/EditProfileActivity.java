package com.app.hulchul.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.hulchul.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.layout_changepwd)
    RelativeLayout layout_changepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
       back_btn.setOnClickListener(this);
       layout_changepwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_changepwd:
                startActivity(new Intent(EditProfileActivity.this,ChangepasswordActivity.class));
                break;
        }
    }
}
