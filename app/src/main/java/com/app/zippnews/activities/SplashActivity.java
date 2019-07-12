package com.app.zippnews.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.zippnews.R;
import com.app.zippnews.utils.SessionManagement;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    @BindView(R.id.iv_splash)
    ImageView iv_splash;
    private Handler handler=new Handler();
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private boolean timeout=false;
    private SessionManagement sessionManagement;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Picasso.with(SplashActivity.this).load(R.mipmap.app_icon).placeholder(R.mipmap.app_icon)
                .error(R.mipmap.app_icon)
                .into(iv_splash);
        init();
        handler.postDelayed(th,2000);
    }

    private void init()
    {
        sessionManagement=new SessionManagement(SplashActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (!checkingPermissionAreEnabledOrNot())
            requestMultiplePermission();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
//            lattitude=String.valueOf(mLocation.getLatitude());
//            longnitude=String.valueOf(mLocation.getLongitude());
        } else {
            callToast("Location not Detected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("info", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("info", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(latLng!=null)
        {
            sessionManagement.saveCurrentlatlongs(""+latLng.latitude,""+latLng.longitude);
            goToHome();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }


    public boolean checkingPermissionAreEnabledOrNot() {
        int coarseloc = ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_COARSE_LOCATION);
        int accessloc = ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION);
        return coarseloc == PackageManager.PERMISSION_GRANTED && accessloc == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermission() {

        ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                {
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION
                }, 100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults.length>=1)
                    {
                        if(checkingPermissionAreEnabledOrNot())
                        {
                            startLocationUpdates();
                        }
                        else
                            requestMultiplePermission();
                    }
                } else {
                    requestMultiplePermission();
                }
                return;
        }
    }

    private void callToast(String msg) {
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    Runnable th=new Runnable() {
        @Override
        public void run() {
            timeout=true;
            goToHome();
        }
    };

    private void goToHome()
    {
        if(timeout)
        {
            if(latLng!=null){
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                finishAffinity();
            }
        }
    }
}
