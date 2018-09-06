package com.estar.nashbud.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.estar.nashbud.chatscreenpages.ChatScreenOne;
import com.estar.nashbud.R;
import com.estar.nashbud.utils.SharedPreference;

public class Splash extends AppCompatActivity {
    SharedPreference sharedPreference;
    Context context;
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = Splash.this;
        launchHomeActivity();

    }
    private void launchHomeActivity() {
        try {
            sharedPreference = new SharedPreference();

            //Log.e("login token getting" ,"" + sharedPreference.getLoginToken(getApplicationContext()));

            if (sharedPreference.getPhNo(getApplicationContext()) == null) {
                /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(Splash.this,WelcomeActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
                //startActivity(new Intent(Splash.this, WelcomeActivity.class));
               // finish();
            } else if (sharedPreference.getPhNo(getApplicationContext()) != null) {
                startActivity(new Intent(context, ChatScreenOne.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
