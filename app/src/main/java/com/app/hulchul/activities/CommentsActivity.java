package com.app.hulchul.activities;

import android.content.Context;
import android.content.Intent;
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
import com.app.hulchul.servicerequestmodels.CommentPostRequest;
import com.app.hulchul.model.CommentPostResponse;
import com.app.hulchul.model.CommentslistModel;
import com.app.hulchul.model.CommentslistingResponse;
import com.app.hulchul.servicerequestmodels.ReplyCommentRequest;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.servicerequestmodels.UseridPostRequest;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.KeyboardEditText;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.tv_emptylistmessage)
    TextView tv_emptylistmessage;
    private ArrayList<CommentslistModel> commentslistModelArrayList=new ArrayList<>();
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String videoid,userid,commentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        if(getIntent()!=null)
         videoid=getIntent().getStringExtra("videoid");

        connectionDetector=new ConnectionDetector(CommentsActivity.this);
        sessionManagement=new SessionManagement(CommentsActivity.this);
        userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);

        layout_replycommentinput.setVisibility(View.GONE);
        layout_commentinput.setVisibility(View.VISIBLE);
        et_replycommentinput.setOnKeyboardListener(this);
       back_btn.setOnClickListener(this);
        layout_sendcommentreply.setOnClickListener(this);
        layout_sendcomment.setOnClickListener(this);
       commentsAdapter=new CommentsAdapter(CommentsActivity.this,commentslistModelArrayList);
       commentsAdapter.setOnCommentsActionsListener(this);
       rv_totalcomments.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
       rv_totalcomments.setAdapter(commentsAdapter);

        if(connectionDetector.isConnectingToInternet())
        {
            setDataToContainer();
        }
        else
            Utils.callToast(CommentsActivity.this,getResources().getString(R.string.internet_toast));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_sendcommentreply:
                if(userid!=null)
                  validatereplyComment(commentid);
                else{
                    startActivity(new Intent(CommentsActivity.this,LoginLandingActivity.class));
                    finish();
                }
                break;
            case R.id.layout_sendcomment:
                if(userid!=null)
                   validatePostComment();
                else{
                    startActivity(new Intent(CommentsActivity.this,LoginLandingActivity.class));
                    finish();
                }
                break;
        }
    }

    public void validatePostComment()
    {
        String comment=et_commentinput.getText().toString();
        if(comment.trim().length()==0)
            Utils.callToast(CommentsActivity.this,"Please enter your comment and send");
        else if(connectionDetector.isConnectingToInternet()){
            CommentPostRequest commentPostRequest = new CommentPostRequest();
            commentPostRequest.setVideoId(videoid);
            commentPostRequest.setUserId(userid);
            commentPostRequest.setComment(comment);
            commentPostRequest.setParentId("");
            postCommentService(commentPostRequest);
        }
        else
            Utils.callToast(CommentsActivity.this,getResources().getString(R.string.internet_toast));
    }

    public void validatereplyComment(String commentid)
    {
        String comment=et_replycommentinput.getText().toString();
        if(comment.trim().length()==0)
            Utils.callToast(CommentsActivity.this,"Please enter your comment and send");
        else if(connectionDetector.isConnectingToInternet()){
            ReplyCommentRequest replyCommentRequest = new ReplyCommentRequest();
            replyCommentRequest.setVideoId(videoid);
            replyCommentRequest.setUserId(userid);
            replyCommentRequest.setComment(comment);
            replyCommentRequest.setCommentId(commentid);
            replyCommentService(commentid,replyCommentRequest);
        }
        else
            Utils.callToast(CommentsActivity.this,getResources().getString(R.string.internet_toast));
    }

    public void validatelikeComment(String commentid)
    {
        if(connectionDetector.isConnectingToInternet()){
            UseridPostRequest useridPostRequest = new UseridPostRequest();
            useridPostRequest.setUserId(userid);
            commentLikeService(commentid,useridPostRequest);
        }
        else
            Utils.callToast(CommentsActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onReplyClicked(CommentslistModel model) {
        layout_commentinput.setVisibility(View.GONE);
        layout_replycommentinput.setVisibility(View.VISIBLE);
        et_replycommentinput.requestFocus();
        String id=model.getUserId().getId();
        et_replycommentinput.setHint("Reply to @user"+id.substring(id.length()-4));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_replycommentinput, InputMethodManager.SHOW_IMPLICIT);
        commentid=model.getId();
    }

    @Override
    public void onCommentLikeClicked(CommentslistModel model) {

        if(userid!=null)
            validatelikeComment(model.getId());
        else{
            startActivity(new Intent(CommentsActivity.this,LoginLandingActivity.class));
            finish();
        }
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

    private void setDataToContainer(){
        Utils.showDialog(CommentsActivity.this);
        Call<CommentslistingResponse> call= RetrofitApis.Factory.create(CommentsActivity.this).commentsListingService(videoid);
        call.enqueue(new Callback<CommentslistingResponse>() {
            @Override
            public void onResponse(Call<CommentslistingResponse> call, Response<CommentslistingResponse> response) {
                Utils.dismissDialog();
                CommentslistingResponse body=response.body();
                if(body!=null) {
                    if (body.getSuccess()) {
                        if(body.getData()!=null && body.getData().size()>0) {
                            commentslistModelArrayList.clear();
                            commentslistModelArrayList.addAll(body.getData());
                        }
                        commentsAdapter.notifyDataSetChanged();
                        if(commentslistModelArrayList.size()>0) {
                            rv_totalcomments.scrollToPosition(0);
                            tv_emptylistmessage.setVisibility(View.GONE);
                        }
                        else {
                            tv_emptylistmessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Utils.callToast(CommentsActivity.this, body.getMessage());
                    }
                }
                else {
                    //Utils.callToast(CommentsActivity.this, "Null data came");
                    Log.e("null data came","null came");
                }
            }

            @Override
            public void onFailure(Call<CommentslistingResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }

    private void postCommentService(CommentPostRequest commentPostRequest){
        Utils.showDialog(CommentsActivity.this);
        Call<CommentPostResponse> call= RetrofitApis.Factory.create(CommentsActivity.this).postCommentService(videoid,commentPostRequest);
        call.enqueue(new Callback<CommentPostResponse>() {
            @Override
            public void onResponse(Call<CommentPostResponse> call, Response<CommentPostResponse> response) {
                Utils.dismissDialog();
                CommentPostResponse body=response.body();
                if(body!=null) {
                    if (body.getSuccess()) {
                        et_commentinput.setText("");
                        setDataToContainer();
                    } else {
                        Utils.callToast(CommentsActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(CommentsActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<CommentPostResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }

    private void replyCommentService(String commentid,ReplyCommentRequest replyCommentRequest){
        Utils.showDialog(CommentsActivity.this);
        Call<CommentPostResponse> call= RetrofitApis.Factory.create(CommentsActivity.this).replyCommentService(commentid,replyCommentRequest);
        call.enqueue(new Callback<CommentPostResponse>() {
            @Override
            public void onResponse(Call<CommentPostResponse> call, Response<CommentPostResponse> response) {
                Utils.dismissDialog();
                CommentPostResponse body=response.body();
                if(body!=null) {
                    if (body.getSuccess()) {
                        et_replycommentinput.setText("");
                        layout_replycommentinput.setVisibility(View.GONE);
                        layout_commentinput.setVisibility(View.VISIBLE);
                        setDataToContainer();
                    } else {
                        Utils.callToast(CommentsActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(CommentsActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<CommentPostResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("replyComment onFailure",""+t.getMessage());
            }
        });
    }

    private void commentLikeService(String commentid, UseridPostRequest useridpost){
        Utils.showDialog(CommentsActivity.this);
        Call<CommentPostResponse> call= RetrofitApis.Factory.create(CommentsActivity.this).likeCommentService(commentid,useridpost);
        call.enqueue(new Callback<CommentPostResponse>() {
            @Override
            public void onResponse(Call<CommentPostResponse> call, Response<CommentPostResponse> response) {
                Utils.dismissDialog();
                CommentPostResponse body=response.body();
                if(body!=null) {
                    if (body.getSuccess()) {
                        setDataToContainer();
                    } else {
                        Utils.callToast(CommentsActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(CommentsActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<CommentPostResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("replyComment onFailure",""+t.getMessage());
            }
        });
    }
}
