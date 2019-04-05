package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.servicerequestmodels.SocialloginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialLoginPresenter {

    private SocialloginResponseListeners loginResponseListeners;
    public void getSocialLoginResponse(final Context context, SocialloginRequest request) {
        loginResponseListeners=(SocialloginResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).getSocialLoginResponse(request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                SignupResponse commonResponse=response.body();
                if(commonResponse!=null)
                {
                    if(commonResponse.getSuccess())
                    {
                        loginResponseListeners.onSocialLoginSuccess(commonResponse);
                    }
                    else
                        loginResponseListeners.onSocialLoginFail(commonResponse.getMessage());
                }
                else {
                    loginResponseListeners.onSocialLoginFail("Incorrect credentials");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                if(t!=null)
                {
                    loginResponseListeners.OnSocialErrorResponse(t.getMessage());
                }
            }
        });
    }

    public interface SocialloginResponseListeners
    {
        void onSocialLoginSuccess(SignupResponse data);
        void onSocialLoginFail(String errormessage);
        void OnSocialErrorResponse(String error);
    }
}
