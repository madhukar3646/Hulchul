package com.app.hulchul.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hulchul.CommonEmptyActivity;
import com.app.hulchul.R;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.servicerequestmodels.SocialloginRequest;
import com.app.hulchul.presenter.SocialLoginPresenter;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class LoginLandingActivity extends AppCompatActivity implements View.OnClickListener,SocialLoginPresenter.SocialloginResponseListeners {

    @BindView(R.id.tv_privacypolicy)
    TextView tv_privacypolicy;
    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.iv_mobile)
    ImageView iv_mobile;
    @BindView(R.id.iv_email)
    ImageView iv_email;
    @BindView(R.id.iv_fb)
    ImageView iv_fb;
    @BindView(R.id.iv_google)
    ImageView iv_google;
    @BindView(R.id.iv_twitter)
    ImageView iv_twitter;
    @BindView(R.id.layout_login)
    RelativeLayout layout_login;

    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN=101;

    private static String TWITTER_CONSUMER_KEY = "MIDSBP3T8kUwpR9iRN3FRjgUD";
    private static String TWITTER_CONSUMER_SECRET = "GeXGfKFNbyqGCHqiK08jpjyDGXj6R7FrsWFq3SlsUPqo5pcSf2";
    private ConnectionDetector cd;
    TwitterAuthClient mTwitterAuthClient;

    //face book code
    private CallbackManager mFacebookCallbackManager;
    private LoginManager mLoginManager;
    private AccessTokenTracker mAccessTokenTracker;

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //face book code
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_landing);
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
        cd=new ConnectionDetector(getApplicationContext());

        init();
        //getHashKeyForFacebook();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(LoginLandingActivity.this);
        sessionManagement=new SessionManagement(LoginLandingActivity.this);

        tv_privacypolicy.setText("By signing up, you confirm that you agree to our Terms of Use and have read and understood our Privacy Policy");
        singleTextView(tv_privacypolicy,"By signing up, you confirm that you agree to ","Terms of Use"," and have read and understood our ","Privacy Policy");
        iv_backbtn.setOnClickListener(this);
        iv_email.setOnClickListener(this);
        iv_mobile.setOnClickListener(this);
        iv_fb.setOnClickListener(this);
        iv_google.setOnClickListener(this);
        iv_twitter.setOnClickListener(this);
        layout_login.setOnClickListener(this);
        //face book code
        setupFacebook();
    }

    private void singleTextView(TextView textView, final String starttext, String termsofuse, final String middletext,final String privacypolicy)
    {
        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append(starttext);
        spanText.append(termsofuse);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(LoginLandingActivity.this, CommonEmptyActivity.class);
                intent.putExtra("common","Terms of Use");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.colorPrimary));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - termsofuse.length(), spanText.length(), 0);

        spanText.append(middletext);
        spanText.append(privacypolicy);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent=new Intent(LoginLandingActivity.this, CommonEmptyActivity.class);
                intent.putExtra("common","Privacy Policy");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.colorPrimary));     // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        },spanText.length() - privacypolicy.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_backbtn:
                finish();
                break;
            case R.id.iv_mobile:
                Intent intent=new Intent(LoginLandingActivity.this,SignUpActivity.class);
                intent.putExtra("isfrom","mobile");
                startActivity(intent);
                break;
            case R.id.iv_email:
                Intent email=new Intent(LoginLandingActivity.this,SignUpActivity.class);
                email.putExtra("isfrom","email");
                startActivity(email);
                break;
            case R.id.iv_fb:
                //face book code
                mAccessTokenTracker.startTracking();
                mLoginManager.logInWithReadPermissions(LoginLandingActivity.this, Arrays.asList("public_profile", "email", "user_birthday"));
                break;
            case R.id.iv_google:
               signIn();
                break;
            case R.id.iv_twitter:
                if(cd.isConnectingToInternet())
                {
                    mTwitterAuthClient.authorize(LoginLandingActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
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

                break;
            case R.id.layout_login:
                startActivity(new Intent(LoginLandingActivity.this,LoginActivity.class));
                break;
        }
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
            callSocialSignupService("google",""+id);
        }
    }

    private void gotoHome()
    {
        Intent intent=new Intent(LoginLandingActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
    }

    private void goToCommonScreen(String data)
    {
        Intent intent=new Intent(LoginLandingActivity.this,CommonEmptyActivity.class);
        intent.putExtra("common",data);
        startActivity(intent);
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
                    callSocialSignupService("twitter",""+user.getId());
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
                    Toast.makeText(LoginLandingActivity.this, "Error permissions", Toast.LENGTH_SHORT).show();
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
                    callSocialSignupService("facebook",response.getRequest().getAccessToken().getUserId());
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
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
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

    private void getHashKeyForFacebook()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash fb", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void callSocialSignupService(String type,String socialid)
    {
        SocialloginRequest request=new SocialloginRequest();
            request.setType(type);
            request.setUsername(socialid);
            request.setInfo("");
        if(connectionDetector.isConnectingToInternet())
        {
            if(LoginLandingActivity.this instanceof SocialLoginPresenter.SocialloginResponseListeners) {
                new SocialLoginPresenter().getSocialLoginResponse(this, request);
                Utils.showDialog(LoginLandingActivity.this);
            }
        }
        else
            Utils.callToast(LoginLandingActivity.this,getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onSocialLoginSuccess(SignupResponse data) {
        Utils.dismissDialog();
        if(data.getData()!=null) {
            sessionManagement.setValuetoPreference(SessionManagement.USERID, data.getData().getId());
            sessionManagement.setBooleanValuetoPreference(SessionManagement.ISLOGIN,true);
        }
        gotoHome();
    }

    @Override
    public void onSocialLoginFail(String errormessage) {
        Utils.dismissDialog();
        Utils.callToast(LoginLandingActivity.this,errormessage);
    }

    @Override
    public void OnSocialErrorResponse(String error) {
        Utils.dismissDialog();
        Log.e("social Signup service",error);
    }
}
