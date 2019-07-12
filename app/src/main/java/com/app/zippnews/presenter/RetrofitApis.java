package com.app.zippnews.presenter;
import android.content.Context;
import com.app.zippnews.model.Allplaylistresponse;
import com.app.zippnews.model.CategoriesResponse;
import com.app.zippnews.model.Discoverresponse;
import com.app.zippnews.model.HashtagSearchResponse;
import com.app.zippnews.model.NotificationsResponse;
import com.app.zippnews.model.ProfilepicUpdateResponse;
import com.app.zippnews.model.SearchUserResponse;
import com.app.zippnews.model.SoundsModuleResponse;
import com.app.zippnews.model.Soundsearchresponse;
import com.app.zippnews.model.TrendingHashtagsBannersResponse;
import com.app.zippnews.model.ViewProfileResponse;
import com.app.zippnews.servicerequestmodels.AbuseResponse;
import com.app.zippnews.servicerequestmodels.ChangepasswordRequest;
import com.app.zippnews.servicerequestmodels.CommentPostRequest;
import com.app.zippnews.model.CommentPostResponse;
import com.app.zippnews.model.CommentslistingResponse;
import com.app.zippnews.servicerequestmodels.CreatepasswordRequest;
import com.app.zippnews.servicerequestmodels.ForgotPasswordRequest;
import com.app.zippnews.model.ForgotPasswordResponse;
import com.app.zippnews.servicerequestmodels.LoginRequest;
import com.app.zippnews.servicerequestmodels.ReplyCommentRequest;
import com.app.zippnews.model.ServerSoundsResponse;
import com.app.zippnews.servicerequestmodels.ResendOtpRequest;
import com.app.zippnews.servicerequestmodels.SocialloginRequest;
import com.app.zippnews.model.Validateotp;
import com.app.zippnews.servicerequestmodels.SignUpRequest;
import com.app.zippnews.model.SignupResponse;
import com.app.zippnews.model.VideosListingResponse;
import com.app.zippnews.servicerequestmodels.UseridPostRequest;
import com.app.zippnews.utils.ApiUrls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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
                        .baseUrl("http://206.189.142.145/api/service/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
                return retrofit.create(RetrofitApis.class);
            }
        }

    @POST("signup")
    Call<SignupResponse> signupService(@Body SignUpRequest signUpRequest);

    @POST("resendOTP")
    Call<SignupResponse> resendOTP(@Body ResendOtpRequest resendOtpRequest);

    @POST("verfiyOTP")
    Call<SignupResponse> validateOtp(@Body Validateotp validateotp);

    @POST("resetPassword")
    Call<SignupResponse> createPassword(@Header("X-Api-Key") String xapikey,@Body CreatepasswordRequest createpasswordRequest);

    @POST("login")
    Call<SignupResponse> login(@Body LoginRequest loginRequest);

    @POST("socialRegistration")
    Call<SignupResponse> getSocialLoginResponse(@Body SocialloginRequest socialloginRequest);

    @POST("fogotPassword")
    Call<ForgotPasswordResponse> getForgotpasswordResponse(@Body ForgotPasswordRequest forgotPasswordRequest);

    @FormUrlEncoded
    @POST("categoryList")
    Call<CategoriesResponse> getCategorylist(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("addUserCategory")
    Call<CategoriesResponse> addUserCategory(@Field("userId") String userId, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("place") String place, @Field("type") String type);

    @FormUrlEncoded
    @POST("reportVideo")
    Call<AbuseResponse> reportVideo(@Field("userId") String userId, @Field("videoId") String videoId, @Field("rating") String rating, @Field("report") String report);

    @FormUrlEncoded
    @POST("deleteUserCategory")
    Call<CategoriesResponse> deleteUserCategory(@Field("id") String id);

    @FormUrlEncoded
    @POST("uploadVideo")
    Call<SignupResponse> uploadVideo(@Field("userId") String userId,@Field("categoryId") String categoryId,@Field("videoId") String videoId,@Field("hashtags") String hashtags,@Field("title") String title,@Field("latitude") String latitude,@Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("userVideos")
    Call<VideosListingResponse> videosListingService(@Field("userId") String userid,@Field("limit") String limit,@Field("offset") String offset,@Field("categoryId") String categoryId,@Field("latitude") String latitude,@Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("userVideos")
    Call<VideosListingResponse> getVideoDatabyId(@Field("userId") String userid,@Field("videoId") String videoId,@Field("limit") String limit,@Field("offset") String offset,@Field("categoryId") String categoryId,@Field("latitude") String latitude,@Field("longitude") String longitude);

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
    @POST("videoFollowers")
    Call<SignupResponse> followUnfollowUserService(@Field("userId") String userid, @Field("fromId") String fromid);

    @GET("commentsList/{videoId}")
    Call<CommentslistingResponse> commentsListingService(@Path("videoId") String videoid);

    @POST("addComment/{videoId}")
    Call<CommentPostResponse> postCommentService(@Path("videoId") String videoid, @Body CommentPostRequest commentPostRequest);

    @POST("replyComment/{commentId}")
    Call<CommentPostResponse> replyCommentService(@Path("commentId") String commentid, @Body ReplyCommentRequest replyCommentRequest);

    @POST("likeComment/{commentId}")
    Call<CommentPostResponse> likeCommentService(@Path("commentId") String commentid, @Body UseridPostRequest useridpostrequest);

    @FormUrlEncoded
    @POST("listNotifications")
    Call<NotificationsResponse> notificationsService(@Field("userId") String userId,@Field("limit") String limit,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("pushNotification")
    Call<CommentPostResponse> enableDisablePushNotification(@Header("X-Api-Key") String token,@Field("pushStatus") String pushStatus);

    @FormUrlEncoded
    @POST("deleteUserVideo")
    Call<VideosListingResponse> deleteUserVideo(@Header("X-Api-Key") String token,@Field("videoId") String videoId);

    @FormUrlEncoded
    @POST("deviceTokenUpdate")
    Call<CommentPostResponse> deviceTokenUpdate(@Header("X-Api-Key") String token,@Field("deviceToken") String deviceToken);

    @Multipart
    @POST("profilepic")
    Call<ProfilepicUpdateResponse> uploadProfilepic(@Header("X-Api-Key") String token,@Part MultipartBody.Part imageFile,@Part("userId") RequestBody description);

    @FormUrlEncoded
    @POST("profileupdate")
    Call<ProfilepicUpdateResponse> profileUpdate(@Header("X-Api-Key") String token,@Field("userId") String userid, @Field("fullName") String fullName,@Field("bioData") String bioData);

    @FormUrlEncoded
    @POST("viewprofile")
    Call<ViewProfileResponse> viewProfile(@Header("X-Api-Key") String token,@Field("userId") String userid,@Field("fromId") String fromid);

    @POST("changePassword")
    Call<SignupResponse> changepassword(@Header("X-Api-Key") String token,@Body ChangepasswordRequest changepasswordRequest);

    @FormUrlEncoded
    @POST("userSearch")
    Call<SearchUserResponse> searchUser(@Field("search") String search,@Field("userId") String userid);

    @FormUrlEncoded
    @POST("hashTagSearch")
    Call<HashtagSearchResponse> hashTagSearch(@Field("search") String search);

    @FormUrlEncoded
    @POST("hashTagList")
    Call<HashtagSearchResponse> hashTagList(@Field("offset") String offset);

    @FormUrlEncoded
    @POST("songSearch")
    Call<Soundsearchresponse> songSearch(@Field("search") String search);

    @FormUrlEncoded
    @POST("search")
    Call<Discoverresponse> discoverService(@Field("userId") String userid);

    @POST("trendingHashTag")
    Call<TrendingHashtagsBannersResponse> getTrendingTags();

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
    @POST("listFavourites")
    Call<HashtagSearchResponse> listFavouritesHashtags(@Field("userId") String userid, @Field("type") String type, @Field("limit") String limit, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("listFavourites")
    Call<Soundsearchresponse> listFavouritesSounds(@Field("userId") String userid, @Field("type") String type, @Field("limit") String limit, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("songCategoryList")
    Call<SoundsModuleResponse> songCategoryList(@Field("userId") String userid, @Field("limit") String limit, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("CategoryList")
    Call<Allplaylistresponse> songsAllplaylist(@Field("limit") String limit, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("songsByCategoryId")
    Call<ServerSoundsResponse> songsByCategoryId(@Field("limit") String limit, @Field("offset") String offset,@Field("categoryId") String categoryId);

    @FormUrlEncoded
    @POST("addFavourite")
    Call<SignupResponse> addFavourite(@Field("userId") String userid, @Field("type") String type,@Field("favouriteId") String favouriteId);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}

