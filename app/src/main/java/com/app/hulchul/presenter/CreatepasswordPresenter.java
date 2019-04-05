package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.servicerequestmodels.CreatepasswordRequest;
import com.app.hulchul.model.SignupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatepasswordPresenter {

    private CreatepasswordResponseListeners createpasswordResponseListeners;
    public void getCreatepasswordResponse(final Context context, CreatepasswordRequest request) {
        createpasswordResponseListeners=(CreatepasswordResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).createPassword(request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                SignupResponse commonResponse=response.body();
                if(commonResponse!=null)
                {
                    if(commonResponse.getSuccess())
                    {
                        createpasswordResponseListeners.onSuccessresponse(commonResponse);
                    }
                    else
                        createpasswordResponseListeners.onFailresponse(commonResponse.getMessage());
                }
                else {
                    createpasswordResponseListeners.onFailresponse("Incorrect password format please try again");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                if(t!=null)
                {
                    createpasswordResponseListeners.OnErrorResponse(t.getMessage());
                }
            }
        });
    }

    public interface CreatepasswordResponseListeners
    {
        void onSuccessresponse(SignupResponse data);
        void onFailresponse(String errormessage);
        void OnErrorResponse(String error);
    }
}
