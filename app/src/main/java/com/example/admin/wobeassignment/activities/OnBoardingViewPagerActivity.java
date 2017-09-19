package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.adapters.OnBoardingViewPagerActivityAdapter;

/**
 * Created by Admin on 19-09-2017.
 */

public class OnBoardingViewPagerActivity extends FragmentActivity {

    private ViewPager viewPager;
    private OnBoardingViewPagerActivityAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_view_pager);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new OnBoardingViewPagerActivityAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    public void updateViewPagerCount(int position) {
        if (position >= 0 && viewPager != null) {
            viewPager.setCurrentItem(1);
        }
    }
}
