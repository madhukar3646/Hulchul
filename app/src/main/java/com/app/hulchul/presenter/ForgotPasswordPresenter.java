package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.servicerequestmodels.ForgotPasswordRequest;
import com.app.hulchul.model.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordPresenter {

    private ForgotpasswordResponseListeners forgotpasswordResponseListeners;
    public void getForgotpasswordResponse(final Context context,ForgotPasswordRequest request) {
        forgotpasswordResponseListeners=(ForgotpasswordResponseListeners) context;
        Call<ForgotPasswordResponse> call= RetrofitApis.Factory.create(context).getForgotpasswordResponse(request);
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {

                ForgotPasswordResponse commonResponse=response.body();
                if(commonResponse!=null)
                {
                    if(commonResponse.getSuccess())
                    {
                        forgotpasswordResponseListeners.onSuccessResponse(commonResponse);
                    }
                    else
                        forgotpasswordResponseListeners.onFailResponse(commonResponse.getMessage());
                }
                else {
                    forgotpasswordResponseListeners.onFailResponse("User not found with the given details");
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                if(t!=null)
                {
                    forgotpasswordResponseListeners.OnErrorResponse(t.getMessage());
                }
            }
        });
    }

    public interface ForgotpasswordResponseListeners
    {
        void onSuccessResponse(ForgotPasswordResponse data);
        void onFailResponse(String errormessage);
        void OnErrorResponse(String error);
    }
}
