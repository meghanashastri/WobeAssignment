package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.fragments.GoogleSignInFragment;
import com.example.admin.wobeassignment.fragments.LoginFragment;

/**
 * Created by Admin on 19-09-2017.
 */

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment();
        initialiseViews();
    }

    private void initialiseViews() {
        Button btnGoogleSignIn = (Button) findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(this);
    }

    public void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.fragment_holder, loginFragment, "Login");
        fragmentTransaction.commit();
    }

    public void replaceFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        GoogleSignInFragment googleSignInFragment = new GoogleSignInFragment();
        fragmentTransaction.replace(R.id.fragment_holder, googleSignInFragment, "GoogleSignIn");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnGoogleSignIn:
                replaceFragment();
                break;
        }
    }
}