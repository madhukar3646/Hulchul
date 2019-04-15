package com.app.hulchul.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.model.ProfilepicUpdateResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ApiUrls;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.FlowableEmitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.layout_changepwd)
    RelativeLayout layout_changepwd;
    @BindView(R.id.layout_profilecircle)
    RelativeLayout layout_profilecircle;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_biodata)
    EditText et_biodata;
    @BindView(R.id.layout_save)
    RelativeLayout layout_save;
    @BindView(R.id.layout_password)
    RelativeLayout layout_password;

    private String name,biaodata,profilepicurl;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(EditProfileActivity.this);
        sessionManagement=new SessionManagement(EditProfileActivity.this);

        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.IS_SOCIALLOGIN))
            layout_password.setVisibility(View.GONE);
        else
            layout_password.setVisibility(View.VISIBLE);

        name=getIntent().getStringExtra("name");
        biaodata=getIntent().getStringExtra("biodata");
        profilepicurl=getIntent().getStringExtra("profilephoto");
        et_name.setText(name);
        et_biodata.setText(biaodata);

        Picasso.with(EditProfileActivity.this).load(ApiUrls.PROFILEBASEPATH+profilepicurl).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(profile_image);

       back_btn.setOnClickListener(this);
       layout_changepwd.setOnClickListener(this);
       layout_profilecircle.setOnClickListener(this);
       layout_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_changepwd:
                startActivity(new Intent(EditProfileActivity.this,ChangepasswordActivity.class));
                break;

            case R.id.layout_profilecircle:
                picGalleryImage();
                break;

            case R.id.layout_save:
                validateProfileUpdateFields();
                break;
        }
    }

    private void picGalleryImage()
    {
        CropImage.activity()
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String path=resultUri.getPath();
                Log.e("path is","path "+path);
                Picasso.with(EditProfileActivity.this).load(new File(path)).placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .into(profile_image);
                uploadProfile(path);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadProfile(String path)
    {
        if(connectionDetector.isConnectingToInternet())
        {
          uploadProfilepic(path);
        }
        else
            Utils.callToast(EditProfileActivity.this,getResources().getString(R.string.internet_toast));
    }

    private void validateProfileUpdateFields()
    {
        String name=et_name.getText().toString().trim();
        String biodata=et_biodata.getText().toString().trim();
        if(biodata==null || biodata.trim().length()==0)
            biodata="";
        String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        if(name.length()==0 || name.length()<3)
            Utils.callToast(EditProfileActivity.this,"Please enter your full name");
        /*else if(biodata.length()==0 || biodata.length()<5)
            Utils.callToast(EditProfileActivity.this,"Please enter your biodata atleast 5 charecters");*/
        else if(connectionDetector.isConnectingToInternet())
            profileUpdate(userid,name,biodata);
        else
            Utils.callToast(EditProfileActivity.this,getResources().getString(R.string.internet_toast));
    }

    private void uploadProfilepic(String picpath){
        Utils.showDialog(EditProfileActivity.this);
        MultipartBody.Part fileToUpload=null;
        if(picpath!=null&&!picpath.isEmpty()) {
            final File file = new File(picpath);
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);
        }
        else {
            return;
        }
        //RequestBody useid = RequestBody.create(MediaType.parse("text/plain"),sessionManagement.getValueFromPreference(SessionManagement.USERID));

        Call<ProfilepicUpdateResponse> call= RetrofitApis.Factory.createTemp(EditProfileActivity.this).uploadProfilepic(fileToUpload,sessionManagement.getValueFromPreference(SessionManagement.USERID));
        call.enqueue(new Callback<ProfilepicUpdateResponse>() {
            @Override
            public void onResponse(Call<ProfilepicUpdateResponse> call, Response<ProfilepicUpdateResponse> response) {
                Utils.dismissDialog();
                ProfilepicUpdateResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        Utils.callToast(EditProfileActivity.this, body.getMessage());
                    } else {
                        Utils.callToast(EditProfileActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(EditProfileActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<ProfilepicUpdateResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }

    private void profileUpdate(String userid,String fullname,String biodata){
        Utils.showDialog(EditProfileActivity.this);
        Call<ProfilepicUpdateResponse> call= RetrofitApis.Factory.createTemp(EditProfileActivity.this).profileUpdate(userid,fullname,biodata);
        call.enqueue(new Callback<ProfilepicUpdateResponse>() {
            @Override
            public void onResponse(Call<ProfilepicUpdateResponse> call, Response<ProfilepicUpdateResponse> response) {
                Utils.dismissDialog();
                ProfilepicUpdateResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        Utils.callToast(EditProfileActivity.this, body.getMessage());
                        Intent intent=new Intent(EditProfileActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        finishAffinity();
                    } else {
                        Utils.callToast(EditProfileActivity.this, body.getMessage());
                    }
                }
                else {
                    Utils.callToast(EditProfileActivity.this, "Null data came");
                }
            }

            @Override
            public void onFailure(Call<ProfilepicUpdateResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("commentslist onFailure",""+t.getMessage());
            }
        });
    }
}
