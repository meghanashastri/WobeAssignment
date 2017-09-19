package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.adapters.OnBoardingViewPagerActivityAdapter;

/**
 * Created by Admin on 19-09-2017.
 */

public class OnBoardingViewPagerActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private OnBoardingViewPagerActivityAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_view_pager);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new OnBoardingViewPagerActivityAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        initialiseViews();
    }

    private void initialiseViews() {
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    public void updateViewPagerCount(int position) {
        if (position >= 0 && viewPager != null) {
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnLogin:
                goToNextActivity(LoginActivity.class);
                break;
            case R.id.btnRegister:
                goToNextActivity(RegisterActivity.class);
                break;
        }
    }

    protected void goToNextActivity(Class nextActivity) {
        Intent intent = new Intent();
        intent.setClass(this, nextActivity);
        startActivity(intent);
    }
}
