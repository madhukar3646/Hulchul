package com.app.zippnews.presenter;

import android.content.Context;
import com.app.zippnews.servicerequestmodels.SignUpRequest;
import com.app.zippnews.model.SignupResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signuppresenter {

    private SignupResponseListeners loginResponseListeners;
    public void getSignupResponse(final Context context,SignUpRequest request) {
        loginResponseListeners=(Signuppresenter.SignupResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).signupService(request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(response.isSuccessful()) {
                    SignupResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (commonResponse.getStatus()==0) {
                            loginResponseListeners.onLoginSuccess(commonResponse);
                        } else
                            loginResponseListeners.onLoginFail(commonResponse.getMessage());
                       }
                }
                else {
                    if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        SignupResponse mError=new SignupResponse();
                        try {
                            mError= gson.fromJson(response.errorBody().string(),SignupResponse .class);
                            loginResponseListeners.onLoginFail(mError.getMessage());
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
                    loginResponseListeners.OnErrorResponse(t.getMessage());
                }
            }
        });
    }

    public interface SignupResponseListeners
    {
        void onLoginSuccess(SignupResponse data);
        void onLoginFail(String errormessage);
        void OnErrorResponse(String error);
    }
}


