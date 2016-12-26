package com.example.leno.materialdesign;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by LENO on 23-12-2016.
 */

public class HomeScreen extends AppCompatActivity {
    Button fbMyProfileButton;
    Button fbFriendButton;
    private static String APP_ID = "1803733779883589";

    private AsyncFacebookRunner fbAsyncRunner;
    private SharedPreferences fbPrefs;

    // Create Object of Facebook Class
    private Facebook facebook = new Facebook(APP_ID);
    String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_facebook);
        fbMyProfileButton = (Button) findViewById(R.id.profile_btn);
        fbFriendButton = (Button) findViewById(R.id.friend_btn);
        fbAsyncRunner = new AsyncFacebookRunner(facebook);

        // My facebook Profile info
        fbMyProfileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFacebookProfileInfo();
            }
        });
        //My Friends List
        fbFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFacebookFriends();
            }
        });
    }
    // My Profile information
    public void getFacebookProfileInfo() {

        // Make graph api call url like below line
        // https://graph.facebook.com/me?access_token=<your-api-token>&format=json

        fbAsyncRunner.request("me", new AsyncFacebookRunner.RequestListener() {
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

        fbAsyncRunner.request("me/friends", new AsyncFacebookRunner.RequestListener() {
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

}