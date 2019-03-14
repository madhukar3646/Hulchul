package com.app.hulchul.activities;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.utils.Filters;
import com.app.hulchul.utils.SampleGLView;
import com.app.hulchul.utils.Utils;
import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakingVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private SampleGLView sampleGLView;
    protected CameraRecorder cameraRecorder;
    private String filepath;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 1280;
    private AlertDialog filterDialog;
    private boolean toggleClick = false;

    @BindView(R.id.layout_close)
    RelativeLayout layout_close;
    @BindView(R.id.iv_1x2x)
    ImageView iv_1x2x;
    @BindView(R.id.iv_timerselection)
    ImageView iv_timerselection;
    @BindView(R.id.iv_startshooting)
    ImageView iv_startshooting;
    @BindView(R.id.iv_switch)
    ImageView iv_switch;
    @BindView(R.id.layout_filters)
    LinearLayout layout_filters;
    @BindView(R.id.iv_record)
    ImageView iv_record;
    @BindView(R.id.layout_selectsound)
    LinearLayout layout_selectsound;
    @BindView(R.id.layout_upload)
    LinearLayout layout_upload;
    private boolean isRecordStart=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        iv_record.setOnClickListener(this);
        iv_record.setImageResource(R.mipmap.play_video);
        layout_filters.setOnClickListener(this);
        iv_switch.setOnClickListener(this);
        layout_close.setOnClickListener(this);
        iv_1x2x.setOnClickListener(this);
        iv_timerselection.setOnClickListener(this);
        iv_startshooting.setOnClickListener(this);
        layout_selectsound.setOnClickListener(this);
        layout_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.layout_close:
                releaseCamera();
                if(filepath!=null)
                    new File(filepath).delete();
                finish();
                break;

            case R.id.iv_1x2x:
                Utils.callToast(MakingVideoActivity.this," 1x 2x ");
                break;

            case R.id.iv_timerselection:
                Utils.callToast(MakingVideoActivity.this," timer selection ");
                break;

            case R.id.iv_startshooting:
                Utils.callToast(MakingVideoActivity.this," start shooting ");
                break;

            case R.id.layout_selectsound:
                Utils.callToast(MakingVideoActivity.this," select sound ");
                break;

            case R.id.layout_upload:
                Utils.callToast(MakingVideoActivity.this," Upload ");
                break;

            case R.id.iv_record:
                if (isRecordStart) {
                    filepath = getVideoFilePath();
                    cameraRecorder.start(filepath);
                    iv_record.setImageResource(R.mipmap.tick_purple);
                    isRecordStart=false;
                } else {
                    cameraRecorder.stop();
                    iv_record.setImageResource(R.mipmap.play_video);
                    isRecordStart=true;
                    goToSinglePlayActivity();
                }
                break;

            case R.id.iv_switch:
                releaseCamera();
                if (lensFacing == LensFacing.BACK) {
                    lensFacing = LensFacing.FRONT;
                } else {
                    lensFacing = LensFacing.BACK;
                }
                toggleClick = true;
                break;
            case R.id.layout_filters:
                if (filterDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MakingVideoActivity.this);
                    builder.setTitle("Choose a filter");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            filterDialog = null;
                        }
                    });
                    final Filters[] filters = Filters.values();
                    CharSequence[] charList = new CharSequence[filters.length];
                    for (int i = 0, n = filters.length; i < n; i++) {
                        charList[i] = filters[i].name();
                    }
                    builder.setItems(charList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            changeFilter(filters[i]);
                        }
                    });
                    filterDialog = builder.show();
                } else {
                    filterDialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (cameraRecorder != null) {
            cameraRecorder.stop();
            cameraRecorder.release();
            cameraRecorder = null;
        }

        if (sampleGLView != null) {
            ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }


    private void setUpCameraView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout frameLayout = findViewById(R.id.wrap_view);
                frameLayout.removeAllViews();
                sampleGLView = null;
                sampleGLView = new SampleGLView(getApplicationContext());
                sampleGLView.setTouchListener(new SampleGLView.TouchListener() {
                    @Override
                    public void onTouch(MotionEvent event, int width, int height) {
                        if (cameraRecorder == null) return;
                        cameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
                    }
                });
                frameLayout.addView(sampleGLView);
            }
        });
    }


    private void setUpCamera() {
        setUpCameraView();

        cameraRecorder = new CameraRecorderBuilder(this, sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(final boolean flashSupport) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                    }

                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("CameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setUpCamera();
                                }
                            });
                        }
                        toggleClick = false;
                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();


    }

    private void changeFilter(Filters filters) {
        cameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
    }


    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }

    public static String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4";
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    private void goToSinglePlayActivity()
    {
        Intent intent=new Intent(MakingVideoActivity.this,SingleVideoPlayActivity.class);
        intent.putExtra("videourl",filepath);
        startActivity(intent);
    }
}
