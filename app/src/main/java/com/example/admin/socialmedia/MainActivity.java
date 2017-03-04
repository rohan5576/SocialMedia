package com.example.admin.socialmedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.handle;

public class MainActivity extends AppCompatActivity {
    public static final String PACKAGE = "com.example.admin.socialmedia";
    Button fbLoginButton, friendsCount;
    private String fb_name = "", fb_profile_pic = "", fb_email = "", fb_id = "", selectedCountry = "", selectedCountryName = "";

    AccessTokenTracker accessTokenTracker;

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private Profile profile;
    AccessToken accessToken;


    CallbackManager callbackManager;
    List<String> permissionNeeds = Arrays.asList("public_profile", "email");
    TextView textviewname,textviewemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        fbLoginButton = (Button) findViewById(R.id.loginButton);
        textviewname = (TextView) findViewById(R.id.textviewname);
        textviewemail = (TextView) findViewById(R.id.textviewemail);

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        accessToken = loginResult.getAccessToken();

                    }

                    @Override
                    public void onCancel() {
                        // App code
//                        Toast.makeText(getApplication(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        Toast.makeText(getApplication(), "" + exception, Toast.LENGTH_SHORT).show();
                    }
                });

        fbLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //facebookLogin();
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissionNeeds);

            }
        });


        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                System.out.println("response" + response);
                                if (object != null) {


                                    try {
                                        fb_name = object.getString("name");
                                        System.out.println("fb_name" + fb_name);
                                        textviewname.setText(fb_name);

                                        fb_email = object.getString("email");
                                        System.out.println("fb_email" + fb_email);
                                        fb_id = object.getString("id");

                                        textviewemail.setText(fb_email);
                                        int width = 150, height = 150;
//                                        fb_profile_pic = profile.getProfilePictureUri(width, height).toString();





////                                                    }
//                                                }
//                                        ).executeAsync();
                                    } catch (Exception e) {
                                        System.out.println("Exception: " + e);
                                    }


                                } else {
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email");
                request.setParameters(parameters);
                request.executeAsync();

            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profile = currentProfile;

            }
        };
    }


    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

        super.onActivityResult(requestCode, responseCode, intent);


        mCallbackManager.onActivityResult(requestCode, responseCode, intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

//        try {
//
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    PACKAGE,
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//
//               ((TextView) findViewById(R.id.sync))
//                        .setText(Base64.encodeToString(md.digest(),
//                                Base64.NO_WRAP));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.d("Name not found", e.getMessage(), e);
//
//        } catch (NoSuchAlgorithmException e) {
//            Log.d("Error", e.getMessage(), e);
//        }

}
