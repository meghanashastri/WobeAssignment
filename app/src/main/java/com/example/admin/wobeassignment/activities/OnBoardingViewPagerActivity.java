package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.adapters.OnBoardingViewPagerActivityAdapter;

import java.util.ArrayList;

/**
 * Created by Admin on 19-09-2017.
 */

public class OnBoardingViewPagerActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private OnBoardingViewPagerActivityAdapter pagerAdapter;
    private LinearLayout dotsLayout;
    private ImageView[] ivArrayDotsPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_view_pager);

        initialiseViews();
    }

    private void initialiseViews() {
        dotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new OnBoardingViewPagerActivityAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        setupPagerIndidcatorDots();
        ivArrayDotsPager[0].setImageResource(R.drawable.selected_indicator_dot);
        setViewPagerScroll();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    private void setViewPagerScroll() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setImageResource(R.drawable.unselected_indicator_dot);
                }
                ivArrayDotsPager[position].setImageResource(R.drawable.selected_indicator_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupPagerIndidcatorDots() {
        ivArrayDotsPager = new ImageView[3];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.unselected_indicator_dot);
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setAlpha(1);
                }
            });
            dotsLayout.addView(ivArrayDotsPager[i]);
            dotsLayout.bringToFront();
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
