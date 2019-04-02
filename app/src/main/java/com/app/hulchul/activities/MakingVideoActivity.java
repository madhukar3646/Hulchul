package com.app.hulchul.activities;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.VideoEffectsAdapter;
import com.app.hulchul.adapters.VideoFilteroverlaysAdapter;
import com.app.hulchul.model.EffectsModel;
import com.app.hulchul.model.FiltersModel;
import com.app.hulchul.utils.Effects;
import com.app.hulchul.utils.FilterOverlays;
import com.app.hulchul.utils.SampleGLView;
import com.app.hulchul.utils.Utils;
import com.bumptech.glide.Glide;
import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakingVideoActivity extends AppCompatActivity implements View.OnClickListener, VideoEffectsAdapter.onEffectClickListener,VideoFilteroverlaysAdapter.onFilterClickListener{

    private SampleGLView sampleGLView;
    protected CameraRecorder cameraRecorder;
    private String filepath;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 1280;
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
    @BindView(R.id.layout_selecteffects)
    LinearLayout layout_selecteffects;
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
    @BindView(R.id.tv_selectiontitle)
    TextView tv_selectiontitle;
    @BindView(R.id.rv_filters)
    RecyclerView rv_filters;

    private boolean isRecordStart=true;
    private Handler handler=new Handler();
    private VideoEffectsAdapter videoEffectsAdapter;
    private VideoFilteroverlaysAdapter videoFilteroverlaysAdapter;
    private Effects[] effects;
    private MediaPlayer musicplayer;
    private String musicpath,songid;
    private long duration=15000;

    private ArrayList<EffectsModel> effectsModelArrayList=new ArrayList<>();
    private ArrayList<FiltersModel> filtersModelArrayList=new ArrayList<>();
    private int filterthumbnails[]={R.drawable.f1_thumb,R.drawable.f2_thumb,R.drawable.f3_thumb,R.drawable.f4_thumb,
            R.drawable.f5_thumb,R.drawable.f6_thumb,R.drawable.f7_thumb,R.drawable.f8_thumb,R.drawable.f9_thumb,R.drawable.f10_thumb,
            R.drawable.f11_thumb,R.drawable.f12_thumb,R.drawable.f13_thumb,R.drawable.f14_thumb,R.drawable.f15_thumb};
    private int filteroverlays[]={R.drawable.f1,R.drawable.f2,R.drawable.f3,R.drawable.f4,
            R.drawable.f5,R.drawable.f6,R.drawable.f7,R.drawable.f8,R.drawable.f9,R.drawable.f10,
            R.drawable.f11,R.drawable.f12,R.drawable.f13,R.drawable.f14,R.drawable.f15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        if(getIntent().getStringExtra("songpath")!=null)
        {
            musicpath=getIntent().getStringExtra("songpath");
            Log.e("musicpath",""+musicpath);
            songid=getIntent().getStringExtra("songid");
            duration=Long.parseLong(getIntent().getStringExtra("duration"));
            musicplayer=new MediaPlayer();
            try {
                musicplayer.setDataSource(musicpath);
                musicplayer.prepare();
            }
            catch (IOException e) { Log.e("LOG_TAG", "prepare() failed"); }
        }

        init();
    }

    private void init()
    {
        deleteTempFiles();
        if(getTempVideosDir().exists())
            getTempVideosDir().delete();

        DisplayMetrics metrics=getResources().getDisplayMetrics();
        Log.e("width and height",""+metrics.widthPixels+", "+metrics.heightPixels);
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
        layout_selecteffects.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MakingVideoActivity.this,4);
        rv_filters.getLayoutParams().height=this.getResources().getDisplayMetrics().heightPixels/4;
        rv_filters.setLayoutManager(gridLayoutManager);

        moveDown(true);
        setEffectsData();
        setFiltersData();
    }

    private void setEffectsData()
    {
        EffectsModel model;
        effectsModelArrayList.clear();
        effects = Effects.values();
        CharSequence[] charList = new CharSequence[effects.length];
        for (int i = 0, n = effects.length; i < n; i++) {
            model=new EffectsModel();
            model.setFiltername(effects[i].name());
            model.setSelected(false);
            effectsModelArrayList.add(model);
        }
        videoEffectsAdapter=new VideoEffectsAdapter(MakingVideoActivity.this,effectsModelArrayList);
        videoEffectsAdapter.setOnEffectClickListener(this);
    }

    private void setFiltersData()
    {
        filtersModelArrayList.clear();
        FiltersModel model;
       for(int i=0;i<15;i++)
       {
           model=new FiltersModel();
           model.setFilter_layer(filteroverlays[i]);
           model.setThumbnailid(filterthumbnails[i]);
           model.setSelected(false);
           filtersModelArrayList.add(model);
       }
       videoFilteroverlaysAdapter=new VideoFilteroverlaysAdapter(MakingVideoActivity.this,filtersModelArrayList);
       videoFilteroverlaysAdapter.setOnFilterClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.layout_close:
                releaseCamera();
                if(musicplayer!=null)
                musicplayer.stop();
                if(filepath!=null)
                    new File(filepath).delete();
                if(musicpath!=null)
                    new File(musicpath).delete();
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
                startActivity(new Intent(MakingVideoActivity.this,ServerSoundsCompletelistingActivity.class));
                //startActivity(new Intent(MakingVideoActivity.this,MySoundsActivity.class));
                break;

            case R.id.layout_upload:
                startActivity(new Intent(MakingVideoActivity.this,DisplayLocalVodsImagesActivity.class));
                break;

            case R.id.iv_record:
                if (isRecordStart) {
                   if(getTempVideosDir().exists() && getTempVideosDir().listFiles().length>0)
                   {
                     if(filepath!=null)
                         goToSinglePlayActivity();
                   }
                   else {
                       filepath = getVideoFilePath();
                       handler.postDelayed(timethread, duration);
                       if (musicpath == null)
                           cameraRecorder.isMuteRecord(false);
                       else
                           cameraRecorder.isMuteRecord(true);

                       cameraRecorder.start(filepath);
                       if (musicplayer != null)
                           musicplayer.start();
                       Glide.with(this)
                               .load(R.drawable.loader)
                               .placeholder(R.drawable.circle_placeholder)
                               .into(iv_record);
                       layout_bottomicons.setVisibility(View.INVISIBLE);
                       layout_sidevertical.setVisibility(View.INVISIBLE);
                       isRecordStart = false;
                   }
                } else {
                    cameraRecorder.stop();
                    if(musicplayer!=null)
                    musicplayer.stop();
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
            case R.id.layout_selecteffects:
                tv_selectiontitle.setText("Add effects to video");
                rv_filters.setAdapter(videoEffectsAdapter);
                moveUp();
                break;
            case R.id.layout_filters:
                tv_selectiontitle.setText("Add filters to video");
                rv_filters.setAdapter(videoFilteroverlaysAdapter);
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

    private void changeEffects(Effects filters) {
        cameraRecorder.setEffect(Effects.getFilterInstance(filters, getApplicationContext()));
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
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchultempvideos";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, ""+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4");
        //return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4";
        return file.getAbsolutePath();
    }

    public static File getTempVideosDir()
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Hulchultempvideos";
        File dir = new File(file_path);
        return dir;
    }

    public static void deleteTempFiles()
    {
        if(getTempVideosDir().exists())
        {
            File dir=getTempVideosDir();
            File files[]=dir.listFiles();
            int length=dir.listFiles().length;
            for(int i=0;i<length;i++)
                new File(files[i].getAbsolutePath()).delete();
        }
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    private void goToSinglePlayActivity()
    {
        Intent intent=new Intent(MakingVideoActivity.this,SingleVideoPlayActivity.class);
        intent.putExtra("videourl",filepath);
        intent.putExtra("songpath",musicpath);
        intent.putExtra("songid",songid);
        startActivity(intent);
    }

    Runnable timethread=new Runnable() {
        @Override
        public void run() {
            cameraRecorder.stop();
            if(musicplayer!=null)
            musicplayer.release();
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
        displayExitDialog();
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
    public void onEffectClick(int filterpos) {
        changeEffects(effects[filterpos]);
    }

    @Override
    public void onFilterClick(Bitmap bitmap) {
        cameraRecorder.setEffect(new FilterOverlays(bitmap));
    }

    private void displayExitDialog()
    {
        final Dialog dialog=new Dialog(MakingVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reshoot_exitdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tv_reshoot=(TextView)dialog.findViewById(R.id.tv_reshoot);
        TextView tv_exit=(TextView)dialog.findViewById(R.id.tv_exit);
        TextView tv_cancel=(TextView)dialog.findViewById(R.id.tv_cancel);

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                releaseCamera();
                if(musicplayer!=null)
                    musicplayer.release();
                deleteTempFiles();
                if(getTempVideosDir().exists())
                    getTempVideosDir().delete();

                if(musicpath!=null)
                    new File(musicpath).delete();
                dialog.dismiss();
                finish();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        tv_reshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteTempFiles();
                if(getTempVideosDir().exists())
                    getTempVideosDir().delete();

                if(musicpath!=null) {
                    if(musicplayer!=null)
                        musicplayer.release();
                    musicplayer=null;
                    musicplayer = new MediaPlayer();
                    try {
                        musicplayer.setDataSource(musicpath);
                        musicplayer.prepare();
                    } catch (IOException e) {
                        Log.e("LOG_TAG", "prepare() failed");
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
