package com.app.hulchul.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.CommentsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener, CommentsAdapter.OnCommentsActionsListener {


    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_totalcomments)
    RecyclerView rv_totalcomments;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.layout_commentinput)
    LinearLayout layout_commentinput;
    @BindView(R.id.et_commentinput)
    EditText et_commentinput;
    @BindView(R.id.layout_sendcomment)
    RelativeLayout layout_sendcomment;
    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
       back_btn.setOnClickListener(this);
       layout_sendcomment.setOnClickListener(this);
       commentsAdapter=new CommentsAdapter(CommentsActivity.this);
       commentsAdapter.setOnCommentsActionsListener(this);
       rv_totalcomments.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
       rv_totalcomments.setAdapter(commentsAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_sendcomment:
                break;
        }
    }

    @Override
    public void onReplyClicked() {
        layout_commentinput.setVisibility(View.VISIBLE);
        et_commentinput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_commentinput, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onBackPressed() {
        if(layout_commentinput.getVisibility()==View.VISIBLE)
            layout_commentinput.setVisibility(View.GONE);
        else
            finish();
    }
}
