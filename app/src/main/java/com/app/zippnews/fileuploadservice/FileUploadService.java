package com.app.zippnews.fileuploadservice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.zippnews.model.SignupResponse;
import com.app.zippnews.model.VideoUploadingResponse;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.SessionManagement;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService";
    Disposable mDisposable;
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 102;
    private String mFilePath,songid,userid,hashtags,categoryid,title,lattitude,lognitude;
    public static boolean isProcessing=false;
    ResponseBody response;
    private SessionManagement sessionManagement;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */
        // get file file here
        sessionManagement=new SessionManagement(getApplicationContext());
        mFilePath = intent.getStringExtra("mFilePath");
        songid = intent.getStringExtra("songid");
        userid = intent.getStringExtra("userid");
        hashtags=intent.getStringExtra("hashtags");
        categoryid=intent.getStringExtra("categoryid");
        lattitude=sessionManagement.getCurrentlattitude();
        lognitude=sessionManagement.getCurrentlognitude();
        title=intent.getStringExtra("title");

        if (mFilePath == null) {
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        isProcessing=true;
        RestApiService apiService = RetrofitInstance.getApiService();
        Flowable<Double> fileObservable = Flowable.create(emitter -> {
            //response=apiService.onFileUpload(createMultipartBody(mFilePath, emitter),createRequestBodyFromText(userid), createRequestBodyFromText(songid)).blockingGet();
            response=apiService.onFileUpload(createMultipartBody(mFilePath, emitter),createRequestBodyFromText(userid)).blockingGet();
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);

        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> onProgress(progress), throwable -> onErrors(throwable), () -> onSuccess());
    }

    private void onErrors(Throwable throwable) {
        sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
        isProcessing=false;
    }

    private void onProgress(Double progress) {
        sendBroadcastMeaasge(" "+(int)(100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }

    private void onSuccess() {
        if(response!=null)
        {
            try {
                String data=response.string();
                Log.e("uploded response",""+data);
                Gson gson = new Gson();
                VideoUploadingResponse antMediaUploadResponse = gson.fromJson(data, VideoUploadingResponse.class);
                Log.e("video id is",""+antMediaUploadResponse.getData().getVideo());
                uploadeVideo(userid,antMediaUploadResponse.getData().getVideo(),categoryid,title,lattitude,lognitude);
            } catch (IOException e) {
                e.printStackTrace();
            } /*catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
        else {
            Log.e("uploded response","null");
        }
    }

    public void sendBroadcastMeaasge(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        localIntent.putExtra("videopath", mFilePath);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }

    private RequestBody createRequestBodyFromText(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"), mText);
    }
    /**
     * return multi part body in format of FlowableEmitter
     *
     * @param filePath
     * @param emitter
     * @return
     */
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("video", file.getName(), createCountingRequestBody(file, MIMEType.VIDEO.value, emitter));
        //return MultipartBody.Part.createFormData("file", file.getName(), createCountingRequestBody(file, MIMEType.VIDEO.value, emitter));
    }

    private RequestBody createCountingRequestBody(File file, String mimeType, FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }

    private void uploadeVideo(String userid, String videoid,String categoryid,String title,String lat,String logni){

        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(this).uploadVideo(userid,categoryid,videoid,hashtags,title,lat,logni);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    if(new File(mFilePath).exists())
                        new File(mFilePath).delete();
                    isProcessing=false;
                    sendBroadcastMeaasge("101");
                    Log.e(TAG, "onSuccess: File Uploaded");
                }
                else {
                    Log.e("error upload",body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.e("add offer onFailure",""+call.toString());
            }
        });
    }
}
