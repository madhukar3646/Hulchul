package com.app.hulchul.activities;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.hulchul.R;
import com.app.hulchul.adapters.VideoFiltersAdapter;
import com.app.hulchul.utils.Filters;
import com.app.hulchul.utils.SampleGLView;
import com.app.hulchul.utils.Utils;
import com.bumptech.glide.Glide;
import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakingVideoActivity extends AppCompatActivity implements View.OnClickListener,VideoFiltersAdapter.onFilterClickListener{

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
    @BindView(R.id.layout_sidevertical)
    LinearLayout layout_sidevertical;
    @BindView(R.id.layout_bottomicons)
    LinearLayout layout_bottomicons;
    @BindView(R.id.layout_filterslist)
    RelativeLayout layout_filterslist;
    @BindView(R.id.iv_closefilters)
    ImageView iv_closefilters;
    @BindView(R.id.rv_filters)
    RecyclerView rv_filters;
    private boolean isRecordStart=true;
    private Handler handler=new Handler();
    private VideoFiltersAdapter videoFiltersAdapter;
    private Filters[] filters;

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
        iv_record.setImageResource(R.mipmap.video);
        layout_filters.setOnClickListener(this);
        iv_switch.setOnClickListener(this);
        layout_close.setOnClickListener(this);
        iv_1x2x.setOnClickListener(this);
        iv_timerselection.setOnClickListener(this);
        iv_startshooting.setOnClickListener(this);
        layout_selectsound.setOnClickListener(this);
        layout_upload.setOnClickListener(this);
        iv_closefilters.setOnClickListener(this);
        moveDown(true);
        setFiltersData();
    }

    private void setFiltersData()
    {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MakingVideoActivity.this,4);
        rv_filters.getLayoutParams().height=this.getResources().getDisplayMetrics().heightPixels/4;
        filters = Filters.values();
        CharSequence[] charList = new CharSequence[filters.length];
        for (int i = 0, n = filters.length; i < n; i++) {
            charList[i] = filters[i].name();
        }
        videoFiltersAdapter=new VideoFiltersAdapter(MakingVideoActivity.this,charList);
        videoFiltersAdapter.setOnFilterClickListener(this);
        rv_filters.setLayoutManager(gridLayoutManager);
        rv_filters.setAdapter(videoFiltersAdapter);
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
                Utils.goToCommonActivity(MakingVideoActivity.this," 1x 2x work in progress");
                break;

            case R.id.iv_timerselection:
                Utils.goToCommonActivity(MakingVideoActivity.this," timer selection work in progress");
                break;

            case R.id.iv_startshooting:
                Utils.goToCommonActivity(MakingVideoActivity.this," start shooting work in progress");
                break;

            case R.id.layout_selectsound:
                startActivity(new Intent(MakingVideoActivity.this,MySoundsActivity.class));
                //Utils.goToCommonActivity(MakingVideoActivity.this,"select sound work in progress");
                break;

            case R.id.layout_upload:
                Utils.goToCommonActivity(MakingVideoActivity.this,"Upload work in progress");
                break;

            case R.id.iv_record:
                if (isRecordStart) {
                    if(filepath!=null)
                        new File(filepath).delete();
                    filepath = getVideoFilePath();
                    handler.postDelayed(timethread,15000);
                    cameraRecorder.isMuteRecord(true);
                    cameraRecorder.start(filepath);
                    Glide.with(this)
                            .load(R.drawable.loader)
                            .placeholder(R.drawable.circle_placeholder)
                            .into(iv_record);
                    layout_bottomicons.setVisibility(View.INVISIBLE);
                    layout_sidevertical.setVisibility(View.INVISIBLE);
                    isRecordStart=false;
                } else {
                    cameraRecorder.stop();
                    iv_record.setImageResource(R.mipmap.video);
                    isRecordStart=true;
                    goToSinglePlayActivity();
                    handler.removeCallbacks(timethread);
                    layout_bottomicons.setVisibility(View.VISIBLE);
                    layout_sidevertical.setVisibility(View.VISIBLE);
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
            case R.id.iv_closefilters:
                moveDown(false);
                break;
            case R.id.layout_filters:
                moveUp();
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

    Runnable timethread=new Runnable() {
        @Override
        public void run() {
            cameraRecorder.stop();
            iv_record.setImageResource(R.mipmap.video);
            isRecordStart=true;
            goToSinglePlayActivity();
            handler.removeCallbacks(timethread);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timethread);
    }

    @Override
    public void onBackPressed() {
        releaseCamera();
        if(filepath!=null)
            new File(filepath).delete();
        finish();
    }

    public void moveUp(){
        layout_filterslist.setVisibility(View.VISIBLE);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_top);
        layout_filterslist.startAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout_filterslist.clearAnimation();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        layout_filterslist.getWidth(), layout_filterslist.getHeight());
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layout_filterslist.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void moveDown(boolean isFast){
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_to_bottom);
        if(isFast)
          animation1.setDuration(10);
        else
            animation1.setDuration(400);

        layout_filterslist.startAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout_filterslist.clearAnimation();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        layout_filterslist.getWidth(), layout_filterslist.getHeight());
                lp.setMargins(0, 0, 0, -layout_filterslist.getHeight());
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layout_filterslist.setLayoutParams(lp);
                layout_filterslist.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onFilterClick(int filterpos) {
        changeFilter(filters[filterpos]);
    }
}
