package com.app.hulchul.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.app.hulchul.R;
import com.app.hulchul.servicerequestmodels.CreatepasswordRequest;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.presenter.CreatepasswordPresenter;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatePasswordActivity extends AppCompatActivity implements View.OnClickListener,CreatepasswordPresenter.CreatepasswordResponseListeners{

    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.iv_continue)
    ImageView iv_continue;
    @BindView(R.id.et_enterpassword)
    EditText et_enterpassword;
    @BindView(R.id.et_reenterpassword)
    EditText et_reenterpassword;
    @BindView(R.id.layout_continue)
    RelativeLayout layout_continue;
    @BindView(R.id.layout_continuedumny)
    RelativeLayout layout_continuedumny;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(CreatePasswordActivity.this);
        sessionManagement=new SessionManagement(CreatePasswordActivity.this);

      iv_backbtn.setOnClickListener(this);
      layout_continue.setOnClickListener(this);
      layout_continuedumny.setVisibility(View.VISIBLE);
      layout_continue.setVisibility(View.GONE);

        et_enterpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                validateInputs();
            }
        });
        et_reenterpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                validateInputs();
            }
        });
    }

    private void validateInputs()
    {
        String enterpassword,reenterpassword;
        enterpassword=et_enterpassword.getText().toString().trim();
        reenterpassword=et_reenterpassword.getText().toString().trim();
        if(enterpassword.length()>=6 && reenterpassword.length()>=6)
        {
            layout_continue.setVisibility(View.VISIBLE);
            layout_continuedumny.setVisibility(View.GONE);
        }
        else {
            layout_continue.setVisibility(View.GONE);
            layout_continuedumny.setVisibility(View.VISIBLE);
        }
    }

    private void checkPasswordsMatch()
    {
        String enterpassword,reenterpassword;
        enterpassword=et_enterpassword.getText().toString().trim();
        reenterpassword=et_reenterpassword.getText().toString().trim();
        if(!enterpassword.equalsIgnoreCase(reenterpassword))
        {
           Utils.callToast(CreatePasswordActivity.this,"Passwords does not match. Please try again");
        }
        else {
           callCreatepassword();
        }
    }

    private void callCreatepassword()
    {
        CreatepasswordRequest request=new CreatepasswordRequest();
        request.setUserId(sessionManagement.getValueFromPreference(SessionManagement.USERID));
        request.setPassword(et_enterpassword.getText().toString());
        if(connectionDetector.isConnectingToInternet())
        {
            if(CreatePasswordActivity.this instanceof CreatepasswordPresenter.CreatepasswordResponseListeners) {
                new CreatepasswordPresenter().getCreatepasswordResponse(this, request);
                Utils.showDialog(CreatePasswordActivity.this);
            }
        }
        else
            Utils.callToast(CreatePasswordActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_backbtn:
                finish();
                break;
            case R.id.layout_continue:
               checkPasswordsMatch();
                break;
        }
    }

    @Override
    public void onSuccessresponse(SignupResponse data) {
        Utils.dismissDialog();
        sessionManagement.setBooleanValuetoPreference(SessionManagement.ISLOGIN,true);
        Intent intent=new Intent(CreatePasswordActivity.this,SelectInterestsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFailresponse(String errormessage) {
        Utils.dismissDialog();
        Utils.callToast(CreatePasswordActivity.this,errormessage);
    }

    @Override
    public void OnErrorResponse(String error) {
        Utils.dismissDialog();
    }
}
