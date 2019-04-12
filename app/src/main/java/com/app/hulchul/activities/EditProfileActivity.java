package com.app.hulchul.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.layout_changepwd)
    RelativeLayout layout_changepwd;
    @BindView(R.id.layout_profilecircle)
    RelativeLayout layout_profilecircle;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
       back_btn.setOnClickListener(this);
       layout_changepwd.setOnClickListener(this);
       layout_profilecircle.setOnClickListener(this);
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
                uploadProfilepic(path);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadProfilepic(String picpath)
    {
        Log.e("picpath is",picpath);
    }
}
