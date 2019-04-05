package com.app.hulchul.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.Validateotp;
import com.app.hulchul.presenter.OtpPresenter;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener,OtpPresenter.OtpResponseListeners{

    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.iv_continue)
    ImageView iv_continue;
    @BindView(R.id.tv_mobilenumber)
    TextView tv_mobilenumber;
    @BindView(R.id.tv_resend)
    TextView tv_resend;
    @BindView(R.id.tv_isnotyournumber)
    TextView tv_isnotyournumber;
    @BindView(R.id.layout_entermobilenumber)
    RelativeLayout layout_entermobilenumber;
    @BindView(R.id.tv_subtitle)
    TextView tv_subtitle;
    @BindView(R.id.tv_btntext)
    TextView tv_btntext;

    @BindView(R.id.et_otp1)
    EditText et_otp1;
    @BindView(R.id.et_otp2)
    EditText et_otp2;
    @BindView(R.id.et_otp3)
    EditText et_otp3;
    @BindView(R.id.et_otp4)
    EditText et_otp4;
    @BindView(R.id.et_otp5)
    EditText et_otp5;
    @BindView(R.id.et_otp6)
    EditText et_otp6;

    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.view6)
    View view6;
    private String isFrom;
    @BindView(R.id.layout_continue)
    RelativeLayout layout_continue;
    @BindView(R.id.layout_continuedumny)
    RelativeLayout layout_continuedumny;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(OtpActivity.this);
        sessionManagement=new SessionManagement(OtpActivity.this);

        isFrom=getIntent().getStringExtra("isfrom");
        iv_backbtn.setOnClickListener(this);
        layout_continue.setOnClickListener(this);
        tv_resend.setOnClickListener(this);
        layout_entermobilenumber.setOnClickListener(this);
        if(isFrom.equalsIgnoreCase("mobile"))
        {
            tv_subtitle.setText("Sent a verification code to verify your \n Mobile number");
            tv_mobilenumber.setText("Sent to "+getIntent().getStringExtra("input"));
            tv_isnotyournumber.setText(""+getIntent().getStringExtra("input")+" is not your Number?");
            tv_btntext.setText(" Enter your Mobile Number ");
        }
        else {
            tv_subtitle.setText("Sent a verification code to verify your \n Email");
            tv_mobilenumber.setText("Sent to "+getIntent().getStringExtra("input"));
            tv_isnotyournumber.setText(""+getIntent().getStringExtra("input")+"is not your Email?");
            tv_btntext.setText(" Enter your Email ");
        }

        layout_continuedumny.setVisibility(View.VISIBLE);
        layout_continue.setVisibility(View.GONE);

        et_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()==1) {
                    view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    et_otp2.requestFocus();
                }
                else {
                    view1.setBackgroundColor(Color.GRAY);
                }
                validateInputFields();
            }
        });
        et_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()==1) {
                    view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    et_otp3.requestFocus();
                }
                else {
                    view2.setBackgroundColor(Color.GRAY);
                }
                validateInputFields();
            }
        });
        et_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()==1) {
                    view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    et_otp4.requestFocus();
                }
                else {
                    view3.setBackgroundColor(Color.GRAY);
                }
                validateInputFields();
            }
        });
        et_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() == 1)
                {
                    view4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    et_otp5.requestFocus();
                }
                else {
                    view4.setBackgroundColor(Color.GRAY);
                }
                validateInputFields();
            }
        });
        et_otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()==1) {
                    view5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    et_otp6.requestFocus();
                }
                else {
                    view5.setBackgroundColor(Color.GRAY);
                }
                validateInputFields();
            }
        });

        et_otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()==1)
                {
                    view6.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                else {
                    view6.setBackgroundColor(Color.GRAY);
                }
                validateInputFields();
            }
        });
    }

    private void validateInputFields()
    {
        String ot1,ot2,ot3,ot4,ot5,ot6;
        ot1=et_otp1.getText().toString().trim();
        ot2=et_otp2.getText().toString().trim();
        ot3=et_otp3.getText().toString().trim();
        ot4=et_otp4.getText().toString().trim();
        ot5=et_otp5.getText().toString().trim();
        ot6=et_otp6.getText().toString().trim();
        if(ot1.length()>0 && ot2.length()>0 && ot3.length()>0 && ot4.length()>0 && ot5.length()>0 && ot6.length()>0)
        {
            layout_continuedumny.setVisibility(View.GONE);
            layout_continue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_backbtn:
                finish();
                break;
            case R.id.layout_continue:
                callValidateotp();
                break;
            case R.id.tv_resend:
                break;
            case R.id.layout_entermobilenumber:
                finish();
                break;
        }
    }

    private void callValidateotp()
    {
        String otp=et_otp1.getText().toString()+et_otp2.getText().toString()+et_otp3.getText().toString()+et_otp4.getText().toString()+et_otp5.getText().toString()+et_otp6.getText().toString();
        Validateotp request=new Validateotp();
        request.setUserId(sessionManagement.getValueFromPreference(SessionManagement.USERID));
        request.setOtp(otp);
        Log.e("userid",""+sessionManagement.getValueFromPreference(SessionManagement.USERID));
        Log.e("otp",""+otp);
        if(connectionDetector.isConnectingToInternet())
        {
            if(OtpActivity.this instanceof OtpPresenter.OtpResponseListeners) {
                new OtpPresenter().getOtpResponse(this, request);
                Utils.showDialog(OtpActivity.this);
            }
        }
        else
            Utils.callToast(OtpActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onSuccessresponse(SignupResponse data) {
        Utils.dismissDialog();
        startActivity(new Intent(OtpActivity.this,CreatePasswordActivity.class));
    }

    @Override
    public void onFailresponse(String errormessage) {
        Utils.dismissDialog();
        Utils.callToast(OtpActivity.this,errormessage);
    }

    @Override
    public void OnErrorResponse(String error) {
        Utils.dismissDialog();
    }
}
