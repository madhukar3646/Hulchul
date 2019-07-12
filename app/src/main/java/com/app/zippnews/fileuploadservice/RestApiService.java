package com.app.zippnews.fileuploadservice;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created on : Feb 25, 2019
 * Author     : AndroidWave
 */
public interface RestApiService {

    /*@Multipart
    @POST("uploadVideo")
    Single<ResponseBody> onFileUpload(@Part MultipartBody.Part file, @Part("userId") RequestBody userId, @Part("songId") RequestBody songId);
   */

    /*@Multipart
    @POST("uploadVoDFile/one.mp4")
    Single<ResponseBody> onFileUpload(@Part MultipartBody.Part file);*/

    @Multipart
    @POST("videos")
    Single<ResponseBody> onFileUpload(@Part MultipartBody.Part file,@Part("userId") RequestBody userId);
}
