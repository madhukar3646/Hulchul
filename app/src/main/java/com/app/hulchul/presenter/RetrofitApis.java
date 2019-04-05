package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.model.NotificationsResponse;
import com.app.hulchul.servicerequestmodels.CommentPostRequest;
import com.app.hulchul.model.CommentPostResponse;
import com.app.hulchul.model.CommentslistingResponse;
import com.app.hulchul.servicerequestmodels.CreatepasswordRequest;
import com.app.hulchul.servicerequestmodels.ForgotPasswordRequest;
import com.app.hulchul.model.ForgotPasswordResponse;
import com.app.hulchul.servicerequestmodels.LoginRequest;
import com.app.hulchul.servicerequestmodels.ReplyCommentRequest;
import com.app.hulchul.model.ServerSoundsResponse;
import com.app.hulchul.servicerequestmodels.SocialloginRequest;
import com.app.hulchul.model.Validateotp;
import com.app.hulchul.servicerequestmodels.SignUpRequest;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.model.VideosListingResponse;
import com.app.hulchul.servicerequestmodels.UseridPostRequest;
import com.app.hulchul.utils.ApiUrls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Path;

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

            public static RetrofitApis createTemp(Context contextOfApplication) {

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
                        .baseUrl("http://testingmadesimple.org/training_app/api/service/")
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

    @Multipart
    @POST("uploadVideo")
    Call<SignupResponse> uploadVideo(@Part MultipartBody.Part file, @Part("userId") RequestBody userId,@Part("songId") RequestBody songId);

    @FormUrlEncoded
    @POST("userVideos")
    Call<VideosListingResponse> videosListingService(@Field("userId") String userid);

    @GET("listSongs")
    Call<ServerSoundsResponse> serverSoundsListingService();

    @FormUrlEncoded
    @POST("videoLike")
    Call<SignupResponse> likeUnlikeVideoService(@Field("userId") String userid, @Field("videoId") String videoid);

    @FormUrlEncoded
    @POST("videoShare")
    Call<SignupResponse> videoShareService(@Field("userId") String userid, @Field("videoId") String videoid);

    @FormUrlEncoded
    @POST("videoComment")
    Call<SignupResponse> videoCommentcountupdateService(@Field("userId") String userid, @Field("videoId") String videoid,@Field("comment") String comment);

    @FormUrlEncoded
    @POST("videoFollowers")
    Call<SignupResponse> followUnfollowUserService(@Field("userId") String userid, @Field("videoId") String videoid);

    @GET("comments/{videoId}")
    Call<CommentslistingResponse> commentsListingService(@Path("videoId") String videoid);

    @POST("comments/{videoId}")
    Call<CommentPostResponse> postCommentService(@Path("videoId") String videoid, @Body CommentPostRequest commentPostRequest);

    @POST("comments/replyComment/{commentId}")
    Call<CommentPostResponse> replyCommentService(@Path("commentId") String commentid, @Body ReplyCommentRequest replyCommentRequest);

    @POST("comments/likeComment/{commentId}")
    Call<CommentPostResponse> likeCommentService(@Path("commentId") String commentid, @Body UseridPostRequest useridpostrequest);

    @GET("listNotifications")
    Call<NotificationsResponse> notificationsService();
}

