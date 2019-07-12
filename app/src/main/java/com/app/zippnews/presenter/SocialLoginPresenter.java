package com.app.zippnews.presenter;

import android.content.Context;

import com.app.zippnews.model.SignupResponse;
import com.app.zippnews.servicerequestmodels.SocialloginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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
                if(response.isSuccessful()) {
                    if (commonResponse != null) {
                        if (commonResponse.getStatus()==0) {
                            loginResponseListeners.onSocialLoginSuccess(commonResponse);
                        } else
                            loginResponseListeners.onSocialLoginFail(commonResponse.getMessage());
                    } else {
                        loginResponseListeners.onSocialLoginFail("Incorrect credentials");
                    }
                }
                else {
                    if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        SignupResponse mError=new SignupResponse();
                        try {
                            mError= gson.fromJson(response.errorBody().string(),SignupResponse .class);
                            loginResponseListeners.onSocialLoginFail(mError.getMessage());
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
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
