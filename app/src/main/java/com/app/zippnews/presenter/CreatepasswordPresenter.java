package com.app.zippnews.presenter;

import android.content.Context;

import com.app.zippnews.servicerequestmodels.CreatepasswordRequest;
import com.app.zippnews.model.SignupResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatepasswordPresenter {

    private CreatepasswordResponseListeners createpasswordResponseListeners;
    public void getCreatepasswordResponse(final Context context, CreatepasswordRequest request,String xapikey) {
        createpasswordResponseListeners=(CreatepasswordResponseListeners) context;
        Call<SignupResponse> call= RetrofitApis.Factory.create(context).createPassword(xapikey,request);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                SignupResponse commonResponse=response.body();
                if(response.isSuccessful()) {
                    if (commonResponse != null) {
                        if (commonResponse.getStatus()==0) {
                            createpasswordResponseListeners.onSuccessresponse(commonResponse);
                        } else
                            createpasswordResponseListeners.onFailresponse(commonResponse.getMessage());
                    } else {
                        createpasswordResponseListeners.onFailresponse("Incorrect password format please try again");
                    }
                }
                else {
                    if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        SignupResponse mError=new SignupResponse();
                        try {
                            mError= gson.fromJson(response.errorBody().string(),SignupResponse .class);
                            createpasswordResponseListeners.onFailresponse(mError.getMessage());
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
