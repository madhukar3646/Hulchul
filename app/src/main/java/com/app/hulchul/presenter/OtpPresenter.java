package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.Validateotp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpPresenter {

    private OtpResponseListeners otpResponseListeners;
    public void getOtpResponse(final Context context,Validateotp request) {
        otpResponseListeners=(OtpResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).validateOtp(request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                SignupResponse commonResponse=response.body();
                if(commonResponse!=null)
                {
                    if(commonResponse.getSuccess())
                    {
                        otpResponseListeners.onSuccessresponse(commonResponse);
                    }
                    else
                        otpResponseListeners.onFailresponse(commonResponse.getMessage());
                }
                else {
                    otpResponseListeners.onFailresponse("Invalid otp");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                if(t!=null)
                {
                    otpResponseListeners.OnErrorResponse(t.getMessage());
                }
            }
        });
    }

    public interface OtpResponseListeners
    {
        void onSuccessresponse(SignupResponse data);
        void onFailresponse(String errormessage);
        void OnErrorResponse(String error);
    }
}
