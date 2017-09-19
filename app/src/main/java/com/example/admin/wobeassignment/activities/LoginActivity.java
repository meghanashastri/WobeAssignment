package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.fragments.LoginFragment;

/**
 * Created by Admin on 19-09-2017.
 */

public class LoginActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment();
    }

    public void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.fragment_holder, loginFragment, "Login");
        fragmentTransaction.commit();
    }
}