package com.app.hulchul.presenter;

import android.content.Context;
import com.app.hulchul.model.SignUpRequest;
import com.app.hulchul.model.SignupResponse;

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
                    loginResponseListeners.onLoginFail("user already existed with us");
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


