package com.app.zippnews.fileuploadservice;

import com.app.zippnews.utils.ApiUrls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on : Feb 25, 2019
 * Author     : AndroidWave
 */
public class RetrofitInstance {

    private static Retrofit retrofit = null;

    public static RestApiService getApiService() {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }).build();

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    /*.baseUrl("http://testingmadesimple.org/training_app/api/service/")*/
                    /*.baseUrl("http://159.65.157.210:5080/LiveApp/rest/broadcast/")*/
                    .baseUrl(ApiUrls.VIDEOUPLOADURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(RestApiService.class);
    }
}
