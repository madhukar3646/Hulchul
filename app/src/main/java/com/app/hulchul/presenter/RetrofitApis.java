package com.app.hulchul.presenter;

import android.content.Context;

import com.app.hulchul.model.Discoverresponse;
import com.app.hulchul.model.HashtagSearchResponse;
import com.app.hulchul.model.NotificationsResponse;
import com.app.hulchul.model.ProfilepicUpdateResponse;
import com.app.hulchul.model.SearchUserResponse;
import com.app.hulchul.model.Soundsearchresponse;
import com.app.hulchul.model.ViewProfileResponse;
import com.app.hulchul.servicerequestmodels.ChangepasswordRequest;
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
import retrofit2.http.Header;
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

    @FormUrlEncoded
    @POST("uploadVideo")
    Call<SignupResponse> uploadVideo(@Field("userId") String userId,@Field("songId") String songId,@Field("videoId") String videoId,@Field("hashtags") String hashtags);

    @FormUrlEncoded
    @POST("userVideos")
    Call<VideosListingResponse> videosListingService(@Field("userId") String userid,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("userFollowerVideos")
    Call<VideosListingResponse> userFollowerVideos(@Field("userId") String userid,@Field("limit") String limit,@Field("offset") String offset);


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
    Call<SignupResponse> followUnfollowUserService(@Field("userId") String userid, @Field("fromId") String fromid);

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

    @Multipart
    @POST("hprofilepic")
    Call<ProfilepicUpdateResponse> uploadProfilepic(@Part MultipartBody.Part imageFile,@Part("userId") RequestBody description);

    @FormUrlEncoded
    @POST("hprofileupdate")
    Call<ProfilepicUpdateResponse> profileUpdate(@Field("userId") String userid, @Field("fullName") String fullName,@Field("bioData") String bioData);

    @FormUrlEncoded
    @POST("viewprofile")
    Call<ViewProfileResponse> viewProfile(@Field("userId") String userid,@Field("fromId") String fromid);

    @POST("users/change-password")
    Call<SignupResponse> changepassword(@Header("x-auth-code") String token,@Body ChangepasswordRequest changepasswordRequest);

    @FormUrlEncoded
    @POST("userSearch")
    Call<SearchUserResponse> searchUser(@Field("search") String search,@Field("userId") String userid);

    @FormUrlEncoded
    @POST("hashTagSearch")
    Call<HashtagSearchResponse> hashTagSearch(@Field("search") String search);

    @FormUrlEncoded
    @POST("songSearch")
    Call<Soundsearchresponse> songSearch(@Field("search") String search);

    @FormUrlEncoded
    @POST("search")
    Call<Discoverresponse> discoverService(@Field("userId") String userid);

    @FormUrlEncoded
    @POST("videoBySong")
    Call<VideosListingResponse> videoBySong(@Field("userId") String userid,@Field("songId") String songid,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("videoByHashTag")
    Call<VideosListingResponse> videoByHashTag(@Field("userId") String userid,@Field("hashTag") String hashtag,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("profileVideos")
    Call<VideosListingResponse> profileVideos(@Field("userId") String userid,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("userLikeVideos")
    Call<VideosListingResponse> userLikeVideos(@Field("userId") String userid,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("listFavourites")
    Call<VideosListingResponse> listFavouritesVideos(@Field("userId") String userid,@Field("type") String type,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("songCategoryList")
    Call<VideosListingResponse> songCategoryList(@Field("userId") String userid,@Field("limit") String limit,@Field("offset") String offset);

}

