package com.app.hulchul.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;

public class FindFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.layout_search)
    RelativeLayout layout_search;
    @BindView(R.id.layout_invitefriends)
    LinearLayout layout_invitefriends;
    @BindView(R.id.layout_contacts)
    LinearLayout layout_contacts;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
      connectionDetector=new ConnectionDetector(FindFriendsActivity.this);
      sessionManagement=new SessionManagement(FindFriendsActivity.this);
      back_btn.setOnClickListener(this);
      layout_search.setOnClickListener(this);
      layout_invitefriends.setOnClickListener(this);
      layout_contacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId())
       {
           case R.id.back_btn:
               finish();
               break;
           case R.id.layout_search:
             startActivity(new Intent(FindFriendsActivity.this,SearchActivity.class));
               break;
           case R.id.layout_invitefriends:
              if(checkingPermissionAreEnabledOrNot())
                  startActivity(new Intent(FindFriendsActivity.this,InviteFriendsActivity.class));
              else
                  requestMultiplePermission(120);
               break;
           case R.id.layout_contacts:
               if(checkingPermissionAreEnabledOrNot())
                   startActivity(new Intent(FindFriendsActivity.this,FindContactsActivity.class));
               else
                   requestMultiplePermission(100);
               break;
       }
    }

    public boolean checkingPermissionAreEnabledOrNot() {
        int write = ContextCompat.checkSelfPermission(FindFriendsActivity.this, READ_CONTACTS);
        return write == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission(int requestcode) {
        ActivityCompat.requestPermissions(FindFriendsActivity.this, new String[]
                {
                        READ_CONTACTS
                }, requestcode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 120:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=1)
                    {
                        if(checkingPermissionAreEnabledOrNot())
                        {
                            startActivity(new Intent(FindFriendsActivity.this,InviteFriendsActivity.class));
                        }
                        else
                            requestMultiplePermission(120);
                    }
                } else {
                    requestMultiplePermission(120);
                }
                break;

            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=1)
                    {
                        if(checkingPermissionAreEnabledOrNot())
                        {
                            startActivity(new Intent(FindFriendsActivity.this,FindContactsActivity.class));
                        }
                        else
                            requestMultiplePermission(100);
                    }
                } else {
                    requestMultiplePermission(100);
                }
                break;
        }
    }
}
