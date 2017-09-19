package com.example.admin.wobeassignment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.wobeassignment.fragments.GoogleSignInFragment;
import com.example.admin.wobeassignment.fragments.RegisterFragment;
import com.example.admin.wobeassignment.fragments.SignInFragment;

/**
 * Created by Admin on 19-09-2017.
 */

public class OnBoardingViewPagerActivityAdapter extends FragmentPagerAdapter {
    public static final int PAGE_COUNT = 3;
    private final int FRAGMENT_GOOGLE_SIGN_IN = 0;
    private final int FRAGMENT_SIGN_IN = FRAGMENT_GOOGLE_SIGN_IN + 1;
    private final int FRAGMENT_REGISTER = FRAGMENT_SIGN_IN + 1;

    public OnBoardingViewPagerActivityAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_GOOGLE_SIGN_IN:
                return new GoogleSignInFragment();
            case FRAGMENT_SIGN_IN:
                return new SignInFragment();
            case FRAGMENT_REGISTER:
                return new RegisterFragment();
            default:
                return new GoogleSignInFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
