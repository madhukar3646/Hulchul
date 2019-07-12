package com.app.zippnews.presenter;

import android.content.Context;

import com.app.zippnews.servicerequestmodels.LoginRequest;
import com.app.zippnews.model.SignupResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {

    private LoginResponseListeners loginResponseListeners;
    public void getLoginResponse(final Context context,LoginRequest request) {
        loginResponseListeners=(LoginResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).login(request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                SignupResponse commonResponse=response.body();
                if(response.isSuccessful()) {
                    if (commonResponse != null) {
                        if (commonResponse.getStatus()==0) {
                            loginResponseListeners.onLoginSuccess(commonResponse);
                        } else
                            loginResponseListeners.onLoginFail(commonResponse.getMessage());
                    } else {
                        loginResponseListeners.onLoginFail("Incorrect credentials");
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

    public interface LoginResponseListeners
    {
        void onLoginSuccess(SignupResponse data);
        void onLoginFail(String errormessage);
        void OnErrorResponse(String error);
    }
}
