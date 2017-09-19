package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.fragments.RegisterFragment;

/**
 * Created by Admin on 20-09-2017.
 */

public class RegisterActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addFragment();
    }

    public void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        RegisterFragment registerFragment = new RegisterFragment();
        fragmentTransaction.add(R.id.fragment_holder, registerFragment, "Register");
        fragmentTransaction.commit();
    }
}