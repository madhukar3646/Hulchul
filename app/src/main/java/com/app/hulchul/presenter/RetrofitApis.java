package com.app.hulchul.presenter;

import android.content.Context;
import com.app.hulchul.model.CreatepasswordRequest;
import com.app.hulchul.model.ForgotPasswordRequest;
import com.app.hulchul.model.ForgotPasswordResponse;
import com.app.hulchul.model.LoginRequest;
import com.app.hulchul.model.SocialloginRequest;
import com.app.hulchul.model.Validateotp;
import com.app.hulchul.model.SignUpRequest;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.utils.ApiUrls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
/**
 * Created by madhu on 8/2/2018.
 */

public interface RetrofitApis {

        class Factory {
            public static RetrofitApis create(Context contextOfApplication) {

                // default time out is 15 seconds
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .build();

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiUrls.BASEURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
                return retrofit.create(RetrofitApis.class);
            }
        }

    @POST("users")
    Call<SignupResponse> signupService(@Body SignUpRequest signUpRequest);

    @POST("users/verfiyOTP")
    Call<SignupResponse> validateOtp(@Body Validateotp validateotp);

    @POST("users/reset-password")
    Call<SignupResponse> createPassword(@Body CreatepasswordRequest createpasswordRequest);

    @POST("users/login")
    Call<SignupResponse> login(@Body LoginRequest loginRequest);

    @POST("users/socialRegistration")
    Call<SignupResponse> getSocialLoginResponse(@Body SocialloginRequest socialloginRequest);

    @POST("users/fogot-password")
    Call<ForgotPasswordResponse> getForgotpasswordResponse(@Body ForgotPasswordRequest forgotPasswordRequest);
}

