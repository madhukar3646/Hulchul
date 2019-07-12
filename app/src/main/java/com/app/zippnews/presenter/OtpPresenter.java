package com.app.zippnews.presenter;

import android.content.Context;
import com.app.zippnews.model.SignupResponse;
import com.app.zippnews.model.Validateotp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpPresenter{

    private OtpResponseListeners otpResponseListeners;
    public void getOtpResponse(final Context context,Validateotp request) {
        otpResponseListeners=(OtpResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).validateOtp(request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                SignupResponse commonResponse=response.body();
                if(response.isSuccessful()) {
                    if (commonResponse != null) {
                        if (commonResponse.getStatus()==0) {
                            otpResponseListeners.onSuccessresponse(commonResponse);
                        } else
                            otpResponseListeners.onFailresponse(commonResponse.getMessage());
                    } else {
                        otpResponseListeners.onFailresponse("Invalid otp");
                    }
                }
                else {
                    if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        SignupResponse mError=new SignupResponse();
                        try {
                            mError= gson.fromJson(response.errorBody().string(),SignupResponse .class);
                            otpResponseListeners.onFailresponse(mError.getMessage());
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
