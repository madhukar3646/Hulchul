package com.app.zippnews.activities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.fileuploadservice.FileUploadService;
import com.app.zippnews.model.CategoriesResponse;
import com.app.zippnews.model.CategoryModel;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPosting_Activity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.iv_videothumbnail)
    ImageView iv_videothumbnail;
    @BindView(R.id.iv_addfriends)
    ImageView iv_addfriends;
    @BindView(R.id.iv_addhashtags)
    ImageView iv_addhashtags;
    @BindView(R.id.et_hashtags)
    EditText et_hashtags;
    @BindView(R.id.spinner_categories)
    Spinner spinner_categories;
    @BindView(R.id.switchButton)
    Switch switchButton;
    @BindView(R.id.layout_save)
    RelativeLayout layout_save;
    @BindView(R.id.layout_post)
    RelativeLayout layout_post;
    @BindView(R.id.layout_choosearea)
    RelativeLayout layout_choosearea;
    @BindView(R.id.tv_newsarea)
    TextView tv_newsarea;
    @BindView(R.id.et_newstitle)
    EditText et_newstitle;

    private String videopath;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private String musicpath,songid;
    private ArrayList<CategoryModel> categories_list=new ArrayList<>();
    private String categoryid,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_posting_);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(VideoPosting_Activity.this);
        sessionManagement=new SessionManagement(VideoPosting_Activity.this);
        Intent intent=getIntent();
        musicpath=intent.getStringExtra("musicpath");
        songid=intent.getStringExtra("songid");
        videopath=intent.getStringExtra("videopath");
        iv_videothumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail(videopath, MediaStore.Video.Thumbnails.MINI_KIND));

        back_btn.setOnClickListener(this);
        layout_post.setOnClickListener(this);
        layout_save.setOnClickListener(this);
        iv_videothumbnail.setOnClickListener(this);
        layout_choosearea.setOnClickListener(this);

        categories_list.add(new CategoryModel());
        setCategoriesAdapter(categories_list);

        spinner_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0)
                {
                    categoryid=categories_list.get(i).getId();
                }
                else {
                    categoryid=null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        if(connectionDetector.isConnectingToInternet())
        {
            setCagegoriesData();
        }
        else
            Utils.callToast(VideoPosting_Activity.this,getResources().getString(R.string.internet_toast));
    }

    private void setCagegoriesData(){
        Utils.showDialog(VideoPosting_Activity.this);
        Call<CategoriesResponse> call= RetrofitApis.Factory.createTemp(VideoPosting_Activity.this).getCategorylist(sessionManagement.getValueFromPreference(SessionManagement.USERID));
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Utils.dismissDialog();
                CategoriesResponse body=response.body();
                if(body!=null)
                {
                    if (body.getStatus() == 1) {
                        if(body.getData()!=null && body.getData().size()>0)
                        {
                            categories_list.clear();
                            categories_list.add(new CategoryModel());
                            categories_list.addAll(body.getData());
                            setCategoriesAdapter(categories_list);
                        }
                    } else {
                        Utils.callToast(VideoPosting_Activity.this, "No categories found");
                    }
                }
                else {
                    Utils.callToast(VideoPosting_Activity.this, "Null response");
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("follow onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_post:
                title=et_newstitle.getText().toString();
                if(connectionDetector.isConnectingToInternet())
                {
                    if(title.trim().length()==0)
                        Utils.callToast(VideoPosting_Activity.this,"Please enter news title");
                    else if(categoryid==null)
                        Utils.callToast(VideoPosting_Activity.this,"Please select category");
                    else
                      uploadfromBackground(sessionManagement.getValueFromPreference(SessionManagement.USERID),videopath,songid);
                }
                else
                    Utils.callToast(VideoPosting_Activity.this,getResources().getString(R.string.internet_toast));
                break;
            case R.id.layout_save:

                String draftpath=moveFileToDraft(videopath);
                if(musicpath!=null)
                    new File(musicpath).delete();
                Intent intent=new Intent(VideoPosting_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
                finishAffinity();

                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.iv_videothumbnail:
               goToSinglePlayActivity(videopath);
                break;
            case R.id.layout_choosearea:
                try {
                    Intent googleplacepicker =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(googleplacepicker, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
        }
    }

    private String getHashTags()
    {
        String hashtags="";
        String hashes=et_hashtags.getText().toString().trim();
        hashes=hashes.replace(","," ");
        String arr[]=hashes.split("#");
        hashes="";
        for(int i=0;i<arr.length;i++)
        {
            hashes=hashes+"#"+arr[i].trim()+",";
        }
        if(hashes.length()>2)
            hashtags=hashes.substring(2,hashes.length()-1);

        return hashtags;
    }

    private void uploadfromBackground(String userid, String path,String songid)
    {
        String draftpath=moveFileToDraft(path);
        if(musicpath!=null)
            new File(musicpath).delete();

        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", draftpath);
        mIntent.putExtra("songid", songid);
        mIntent.putExtra("userid", userid);
        mIntent.putExtra("hashtags",getHashTags());
        mIntent.putExtra("categoryid",categoryid);
        mIntent.putExtra("title",title);
        FileUploadService.enqueueWork(this, mIntent);

        Intent intent=new Intent(VideoPosting_Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
    }

    public String getDraftVideoFilePath() {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/"+getResources().getString(R.string.app_name)+"drafts";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, ""+new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4");
        //return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4";
        return file.getAbsolutePath();
    }

    private String moveFileToDraft(String SourceFilePath)
    {
        File SourceFile = new File(SourceFilePath);

        File DestinationFile = new File(getDraftVideoFilePath());

        if(SourceFile.renameTo(DestinationFile))
        {
            Log.e("Moving", "Moving file successful.");
        }
        else
        {
            Log.e("Moving", "Moving file failed.");
        }
        return DestinationFile.getAbsolutePath();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    private void goToSinglePlayActivity(String filepath)
    {
        Intent intent=new Intent(VideoPosting_Activity.this,SingleVideoPlayActivity.class);
        intent.putExtra("isfrom","VideoPosting_Activity");
        intent.putExtra("videourl",filepath);
        intent.putExtra("songpath",musicpath);
        intent.putExtra("songid",songid);
        startActivity(intent);
    }

    public void shareVideoonSocialapps(String videopath,String packagename,String appname) {
        try {
            File videofiletoshare = new File(videopath);
            Uri uri = Uri.fromFile(videofiletoshare);
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("*/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri); // set uri
            shareIntent.setPackage(packagename);
            startActivity(shareIntent);
        }
        catch (Exception e)
        {
            if(appname.equalsIgnoreCase("Facebook"))
            {
                try {
                    File videofiletoshare = new File(videopath);
                    Uri uri = Uri.fromFile(videofiletoshare);
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri); // set uri
                    shareIntent.setPackage("com.facebook.lite");
                    startActivity(shareIntent);
                }
                catch (Exception ef)
                {
                    Utils.callToast(VideoPosting_Activity.this, "You don't have " + appname + " app in your phone");
                }
            }
            else {
                Utils.callToast(VideoPosting_Activity.this, "You don't have " + appname + " app in your phone");
            }
        }
    }

    public void setCategoriesAdapter(ArrayList<CategoryModel> categoriesArrayList)
    {
        String categories[]=new String[categoriesArrayList.size()];
        for (int i=0;i<categoriesArrayList.size();i++)
        {
            if(i==0)
                categories[i]="Select Category";
            else
                categories[i]=categoriesArrayList.get(i).getName();

        }
        ArrayAdapter stateadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categories.setAdapter(stateadapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("placeauto", "Place: " + place.getAddress() + place.getPhoneNumber());
                tv_newsarea.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("placeauto", status.getStatusMessage());
                tv_newsarea.setText("");

            } else if (resultCode == RESULT_CANCELED) {
                Log.e("placeauto", "user cancelled");
            }
        }
    }
}
