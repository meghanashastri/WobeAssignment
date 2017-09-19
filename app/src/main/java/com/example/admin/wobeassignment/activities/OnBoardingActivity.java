package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.fragments.GoogleSignInFragment;

/**
 * Created by Admin on 19-09-2017.
 */

public class OnBoardingActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static String TAG = "OnBoarding Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        addFragment();
    }

    public void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        GoogleSignInFragment signInFragment = new GoogleSignInFragment();
        fragmentTransaction.add(R.id.fragment_holder, signInFragment, TAG);
        fragmentTransaction.commit();
    }
}
