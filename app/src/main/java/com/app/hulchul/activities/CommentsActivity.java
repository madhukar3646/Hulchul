package com.app.hulchul.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.CommentsAdapter;
import com.app.hulchul.utils.KeyboardEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener, CommentsAdapter.OnCommentsActionsListener, KeyboardEditText.KeyboardListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_totalcomments)
    RecyclerView rv_totalcomments;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.layout_replycommentinput)
    LinearLayout layout_replycommentinput;
    @BindView(R.id.et_replycommentinput)
    KeyboardEditText et_replycommentinput;
    @BindView(R.id.layout_sendcommentreply)
    RelativeLayout layout_sendcommentreply;
    CommentsAdapter commentsAdapter;
    @BindView(R.id.layout_root)
    ViewGroup rootLayout;
    @BindView(R.id.layout_commentinput)
    LinearLayout layout_commentinput;
    @BindView(R.id.et_commentinput)
    EditText et_commentinput;
    @BindView(R.id.layout_sendcomment)
    RelativeLayout layout_sendcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        layout_replycommentinput.setVisibility(View.GONE);
        layout_commentinput.setVisibility(View.VISIBLE);
        et_replycommentinput.setOnKeyboardListener(this);
       back_btn.setOnClickListener(this);
        layout_sendcommentreply.setOnClickListener(this);
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
            case R.id.layout_sendcommentreply:
                break;
            case R.id.layout_sendcomment:
                break;
        }
    }

    @Override
    public void onReplyClicked(String username) {
        layout_commentinput.setVisibility(View.GONE);
        layout_replycommentinput.setVisibility(View.VISIBLE);
        et_replycommentinput.requestFocus();
        et_replycommentinput.setHint(""+username);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_replycommentinput, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onStateChanged(KeyboardEditText keyboardEditText, boolean showing) {
        if(showing)
        {
            Log.e("keyboard opened","keyboard opened");
        }
        else {
            layout_replycommentinput.setVisibility(View.GONE);
            et_replycommentinput.setText("");
            layout_commentinput.setVisibility(View.VISIBLE);
            Log.e("keyboard closed","keyboard closed");
        }
    }
}
