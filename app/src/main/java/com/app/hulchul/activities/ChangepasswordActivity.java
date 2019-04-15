package com.app.hulchul.activities;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.servicerequestmodels.ChangepasswordRequest;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangepasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.layout_change)
    RelativeLayout layout_change;
    @BindView(R.id.et_oldpassword)
    EditText et_oldpassword;
    @BindView(R.id.et_confirmpassword)
    EditText et_confirmpassword;
    @BindView(R.id.et_newpassword)
    EditText et_newpassword;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
       back_btn.setOnClickListener(this);
       layout_change.setOnClickListener(this);

       connectionDetector=new ConnectionDetector(ChangepasswordActivity.this);
       sessionManagement=new SessionManagement(ChangepasswordActivity.this);
    }

    private void validateinputFileds()
    {
       String oldpassword=et_oldpassword.getText().toString().trim();
       String newpassword=et_newpassword.getText().toString().trim();
       String confirmpwd=et_confirmpassword.getText().toString().trim();

       if(oldpassword.length()==0)
           Utils.callToast(ChangepasswordActivity.this,"Please enter old password");
       else if(oldpassword.length()<6)
           Utils.callToast(ChangepasswordActivity.this,"You've entered incorrect old password");
       else if(newpassword.length()==0)
           Utils.callToast(ChangepasswordActivity.this,"Please enter new password");
       else if(newpassword.length()<6)
           Utils.callToast(ChangepasswordActivity.this,"Enter atleast 6 digits new password");
       else if(confirmpwd.length()==0)
           Utils.callToast(ChangepasswordActivity.this,"Please enter confirm password");
       else if(!newpassword.equals(confirmpwd))
           Utils.callToast(ChangepasswordActivity.this,"New and confirm passwords doesn't matched");
       else if(connectionDetector.isConnectingToInternet())
       {
           ChangepasswordRequest request=new ChangepasswordRequest();
           request.setNewPassword(newpassword);
           request.setOldPassword(oldpassword);
          changePassword(sessionManagement.getValueFromPreference(SessionManagement.USER_TOKEN),request);
       }
       else
           Utils.callToast(ChangepasswordActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_change:
                validateinputFileds();
                break;
        }
    }

    private void changePassword(String token, ChangepasswordRequest changepasswordRequest){
        Utils.showDialog(ChangepasswordActivity.this);
        Call<SignupResponse> call= RetrofitApis.Factory.create(ChangepasswordActivity.this).changepassword(token,changepasswordRequest);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body!=null) {
                    if (body.getSuccess()) {
                        Utils.callToast(ChangepasswordActivity.this, body.getMessage());
                        finish();
                    } else {
                        Utils.callToast(ChangepasswordActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(ChangepasswordActivity.this, "You have entered incorrect format. Please try again");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }
}
