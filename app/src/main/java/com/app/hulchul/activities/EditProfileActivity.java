package com.app.hulchul.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.hulchul.R;
import com.app.hulchul.model.ProfilepicUpdateResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ApiUrls;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

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
    @BindView(R.id.layout_addfacebook)
    RelativeLayout layout_addfacebook;
    @BindView(R.id.layout_addgoogle)
    RelativeLayout layout_addgoogle;
    @BindView(R.id.layout_addtwitter)
    RelativeLayout layout_addtwitter;
    @BindView(R.id.layout_changphonenumber)
    LinearLayout layout_changphonenumber;

    private String name,biaodata,profilepicurl;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN=101;

    private static String TWITTER_CONSUMER_KEY = "MIDSBP3T8kUwpR9iRN3FRjgUD";
    private static String TWITTER_CONSUMER_SECRET = "GeXGfKFNbyqGCHqiK08jpjyDGXj6R7FrsWFq3SlsUPqo5pcSf2";
    TwitterAuthClient mTwitterAuthClient;

    //face book code
    private CallbackManager mFacebookCallbackManager;
    private LoginManager mLoginManager;
    private AccessTokenTracker mAccessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        mTwitterAuthClient= new TwitterAuthClient();

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
       layout_addfacebook.setOnClickListener(this);
       layout_addgoogle.setOnClickListener(this);
       layout_addtwitter.setOnClickListener(this);
       layout_changphonenumber.setOnClickListener(this);
       setupFacebook();
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
            case R.id.layout_changphonenumber:
                startActivity(new Intent(EditProfileActivity.this,ChangePhoneNumberActivity.class));
                break;
            case R.id.layout_profilecircle:
                picGalleryImage();
                break;

            case R.id.layout_save:
                validateProfileUpdateFields();
                break;
            case R.id.layout_addfacebook:
                if(connectionDetector.isConnectingToInternet())
                {
                    mAccessTokenTracker.startTracking();
                    mLoginManager.logInWithReadPermissions(EditProfileActivity.this, Arrays.asList("public_profile", "email", "user_birthday"));
                }
                else
                    Utils.callToast(EditProfileActivity.this,getResources().getString(R.string.internet_toast));
                break;
            case R.id.layout_addgoogle:
                if(connectionDetector.isConnectingToInternet())
                {
                    signIn();
                }
                else
                    Utils.callToast(EditProfileActivity.this,getResources().getString(R.string.internet_toast));
                break;

            case R.id.layout_addtwitter:
                if(connectionDetector.isConnectingToInternet())
                {
                    mTwitterAuthClient.authorize(EditProfileActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                        @Override
                        public void success(Result<TwitterSession> twitterSessionResult) {
                            // Success
                            getuserData(twitterSessionResult);
                        }
                        @Override
                        public void failure(TwitterException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else {
                    Utils.callToast(EditProfileActivity.this,getResources().getString(R.string.internet_toast));
                }
                break;
        }
    }

    private void picGalleryImage()
    {
        CropImage.activity()
                .start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("signInResultfailed", ""+e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account)
    {
        if(account!=null) {
            String email = account.getEmail();
            String id = account.getId();
            String token = account.getIdToken();
            //Toast.makeText(LoginLandingActivity.this, "" + email, Toast.LENGTH_SHORT).show();
            Log.e("googgle id",""+id);
            Log.e("email is", "" + email);
            Log.e("name",""+account.getDisplayName());
            mGoogleSignInClient.signOut();
        }
    }

    public void getuserData(final Result<TwitterSession> result) {

        TwitterSession session = result.data;

        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(true, false, true).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    //If it succeeds creating a User object from userResult.data
                    User user = response.body();

                    //Getting the profile image url
                    String profileImage = user.profileImageUrl.replace("_normal", "");
                    String username=user.name;
                    String usermail=user.email;

                    Log.e("twitter id",""+user.getId());
                    Log.e("email is", "" + usermail);
                    Log.e("name",""+username);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("error twitter",""+t.getMessage());
            }
        });
    }

    //face book code
    private void setupFacebook() {
        mLoginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // handle
            }
        };

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback() {
            @Override
            public void onSuccess(Object o) {

                LoginResult loginResult=(LoginResult)o;
                if (loginResult.getRecentlyGrantedPermissions().contains("email")) {
                    requestObjectUser(loginResult.getAccessToken());
                } else {
                    LoginManager.getInstance().logOut();
                    Toast.makeText(EditProfileActivity.this, "Error permissions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("ERROR", error.toString());
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            requestObjectUser(AccessToken.getCurrentAccessToken());
        }
    }

    private void requestObjectUser(final AccessToken accessToken) {
        final GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    // handle error
                } else {
                    //Toast.makeText(LoginLandingActivity.this, "Access Token: " + accessToken.getToken(), Toast.LENGTH_SHORT).show();
                    //Log.e("image url: ","https://graph.facebook.com/"+ object.optString("id")+"/picture?type=large");
                    Log.e("facebook id",""+response.getRequest().getAccessToken().getUserId());
                    Log.e("email is", "" + object.optString("email"));
                    Log.e("name",""+object.optString("first_name")+" "+object.optString("last_name"));
                    LoginManager.getInstance().logOut();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
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
        else {
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
            else {
                mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
                //face book code
                mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
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
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"),sessionManagement.getValueFromPreference(SessionManagement.USERID));

        Call<ProfilepicUpdateResponse> call= RetrofitApis.Factory.createTemp(EditProfileActivity.this).uploadProfilepic(fileToUpload,userid);
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
