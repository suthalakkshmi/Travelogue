package com.example.leno.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageView;


import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by LENO on 23-12-2016.
 */

public class AndroidFacebookActivity extends AppCompatActivity {

    // Facebook  APP ID
    private static String APP_ID = "1803733779883589";

    private AsyncFacebookRunner fbAsyncRunner;
    private SharedPreferences fbPrefs;

    // Create Object of Facebook Class
    private Facebook facebook = new Facebook(APP_ID);
    String access_token;


    // Buttons
    Button fbLoginButton;
    Button fbMyProfileButton;
    Button fbFriendButton;
    Button fbPostToWallButton;
    Button fbLogoutButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_facebook);

        //fbLoginButton = (Button) findViewById(R.id.login_btn);
        fbMyProfileButton = (Button) findViewById(R.id.profile_btn);
        //fbPostToWallButton = (Button) findViewById(R.id.wall_btn);
        fbFriendButton = (Button) findViewById(R.id.friend_btn);
        //fbLogoutButton = (Button) findViewById(R.id.logout_btn);
        fbAsyncRunner = new AsyncFacebookRunner(facebook);

        //Login button Clicked
        fbLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                facebookLogin();

            }
        });

        // My facebook Profile info
        fbMyProfileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFacebookProfileInfo();
            }
        });

        // Post at Facebook Wall
        fbPostToWallButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                facebookWallPost();

            }
        });

        // Post at Facebook Wall
        fbFriendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getFacebookFriends();

            }
        });
        fbLogoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();

            }
        });
    }

    // Function for login
    public void facebookLogin() {

        // Create shared preference to save login or not

        fbPrefs = getPreferences(MODE_PRIVATE);
        access_token = fbPrefs.getString("access_token", null);

        // Get Expire value from shared preference
        long expires = fbPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            // Get facebook access token to call facebook Graph api
            facebook.setAccessToken(access_token);

            fbLoginButton.setVisibility(View.INVISIBLE);

            // Make button visible
            fbMyProfileButton.setVisibility(View.VISIBLE);
            fbPostToWallButton.setVisibility(View.VISIBLE);
            fbFriendButton.setVisibility(View.VISIBLE);
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[]{"email", "public_profile","user_about_me","user_birthday","user_hometown","user_likes","user_location","user_tagged_places"},
                    new DialogListener() {

                        @Override
                        public void onCancel() {
                            // Here work for facebook login page cancel event
                        }

                        @Override
                        public void onComplete(Bundle values) {

                            // update Shared Preferences values
                            SharedPreferences.Editor editor = fbPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();

                            // Make button invisible
                            fbLoginButton.setVisibility(View.INVISIBLE);

                            // Make button visible
                            Toast.makeText(getApplicationContext(), "Access_token="+facebook.getAccessToken(), Toast.LENGTH_LONG).show();
                            fbMyProfileButton.setVisibility(View.VISIBLE);
                            fbPostToWallButton.setVisibility(View.VISIBLE);
                            fbFriendButton.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError(DialogError error) {
                            // Here Work for handle error
                            Toast.makeText(getApplicationContext(), "Dialog Error occured", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Here Work for handle Facebook errors
                            Toast.makeText(getApplicationContext(), "FacebookError occured", Toast.LENGTH_LONG).show();
                        }

                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }


    // My Profile information
    public void getFacebookProfileInfo() {

        // Make graph api call url like below line
        // https://graph.facebook.com/me?access_token=<your-api-token>&format=json

        fbAsyncRunner.request("me", new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Profile", response);
                String json = response;
                try {
                    // Got My Facebook Profile data in JSON format
                    JSONObject profile = new JSONObject(json);

                    // extract user name
                    final String name = profile.getString("name");
                    final String userid = profile.getString("id");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "My Name: " + name + " ID:" +userid, Toast.LENGTH_LONG).show();
                        }

                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException(IOException e, Object state) {
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                                                Object state) {
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,
                                                Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });
    }

    // My Friends information

    public void getFacebookFriends() {

        // Make graph api call url like below line
        // https://graph.facebook.com/me/friends?access_token=<your-api-token>&format=json

        fbAsyncRunner.request("me/friends", new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {

                Log.d("Number Of Friends", response);

                String json = response;

                try {
                    // Got My Facebook Freinds in JSON format
                    JSONObject profile = new JSONObject(json);

                    // extract user name
                    final String total_count = profile.getString("summary");


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Number Of Friends: " + total_count, Toast.LENGTH_LONG).show();
                        }

                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException(IOException e, Object state) {
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                                                Object state) {
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,
                                                Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });
    }


    //Post to wall function
    public void facebookWallPost() {

        facebook.dialog(this, "feed", new DialogListener() {

            @Override
            public void onFacebookError(FacebookError e) {
            }

            @Override
            public void onError(DialogError e) {
            }

            @Override
            public void onComplete(Bundle values) {
            }

            @Override
            public void onCancel() {
            }
        });

    }

    //Logout function
    public void logout(){


    }


}
