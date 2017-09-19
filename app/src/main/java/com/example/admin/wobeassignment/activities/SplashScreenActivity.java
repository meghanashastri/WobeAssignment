package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;

/**
 * Created by Admin on 19-09-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {
    Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    goToNextActivity();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreenActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }


    protected void goToNextActivity() {
        boolean isFirstTime = SharedPreferenceManager.getInstance(this).isFirstTimeLaunch();
        Intent intent = new Intent();
        if (!isFirstTime) {
            intent.setClass(this, OnBoardingActivity.class);
        } else {
            intent.setClass(this, DashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }

}