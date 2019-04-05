package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.servicerequestmodels.LoginRequest;
import com.app.hulchul.model.SignupResponse;

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
                if(commonResponse!=null)
                {
                    if(commonResponse.getSuccess())
                    {
                        loginResponseListeners.onLoginSuccess(commonResponse);
                    }
                    else
                        loginResponseListeners.onLoginFail(commonResponse.getMessage());
                }
                else {
                    loginResponseListeners.onLoginFail("Incorrect credentials");
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
