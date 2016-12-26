package com.example.leno.materialdesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

public class MainActivity extends AppCompatActivity {

    private static String APP_ID = "1803733779883589";

//    private AsyncFacebookRunner fbAsyncRunner;
    private SharedPreferences fbPrefs;

    // Create Object of Facebook Class
    private Facebook facebook = new Facebook(APP_ID);
    String access_token;
    private MyPagerAdapter adapterViewPager;
    private ImageButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_fragment);
        loginButton = (ImageButton)findViewById(R.id.FbLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();

            }
        });
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

        vpPager.setAdapter(adapterViewPager);

    }
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return FirstFragment.newInstance(0, "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return SecondFragment.newInstance(1, "Page # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
                   return ThirdFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }

        // Returns the page title for the bottom indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return " " + (position+1);
        }

    }
    public void facebookLogin() {

        // Create shared preference to save login or not

        fbPrefs = getPreferences(MODE_PRIVATE);
        access_token = fbPrefs.getString("access_token", null);

        // Get Expire value from shared preference
        long expires = fbPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            // Get facebook access token to call facebook Graph api
            facebook.setAccessToken(access_token);

        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[]{"email", "public_profile","user_about_me","user_birthday","user_hometown","user_likes","user_location","user_tagged_places"},
                    new Facebook.DialogListener() {

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

                            // Make button visible
                            Toast.makeText(getApplicationContext(), "Access_token="+facebook.getAccessToken(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.this,HomeScreen.class);
                            startActivity(i);
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



}

