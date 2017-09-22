package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;

/**
 * Created by Admin on 22-09-2017.
 */

public class PasscodeActivity extends AppCompatActivity {
    private EditText etOne, etTwo, etThree, etFour;
    private String passcode;
    String passcodeBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        passcodeBundle = bundle.getString(Constants.KEY_PASSCODE_ACTIVITY_BUNDLE);

        setContentView(R.layout.activity_passcode);
        initialiseViews();
    }

    private void initialiseViews() {
        etOne = (EditText) findViewById(R.id.etOne);
        etOne.requestFocus();
        etOne.addTextChangedListener(passcodeEntered);
        etTwo = (EditText) findViewById(R.id.etTwo);
        etTwo.addTextChangedListener(passcodeEntered);
        etThree = (EditText) findViewById(R.id.etThree);
        etThree.addTextChangedListener(passcodeEntered);
        etFour = (EditText) findViewById(R.id.etFour);
        etFour.addTextChangedListener(passcodeCheck);
    }

    private final TextWatcher passcodeEntered = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etOne.getText().toString().length() == 1) {
                etTwo.requestFocus();
            }
            if (etTwo.getText().toString().length() == 1) {
                etThree.requestFocus();
            }
            if (etThree.getText().toString().length() == 1) {
                etFour.requestFocus();
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher passcodeCheck = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            passcode = etOne.getText().toString().trim() + etTwo.getText().toString().trim()
                    + etThree.getText().toString().trim() + etFour.getText().toString().trim();
            if (passcode.length() == 4) {

                if (passcodeBundle != null) {
                    if (passcodeBundle.equalsIgnoreCase(Constants.VALUE_SPLASH_SCREEN_ACTIVITY)) {
                        if (passcode.equalsIgnoreCase(SharedPreferenceManager.getInstance(PasscodeActivity.this).
                                getString(Constants.PASSCODE))) {
                            goToNextActivity(DashboardActivity.class);
                        } else {
                            Toast.makeText(PasscodeActivity.this, getResources().getString(R.string.enter_correct_passcode),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (passcodeBundle.equalsIgnoreCase(Constants.VALUE_LOGIN_ACTIVITY)) {
                        SharedPreferenceManager.getInstance(PasscodeActivity.this).setFirstTimeLaunch(true);
                        SharedPreferenceManager.getInstance(PasscodeActivity.this).saveData(Constants.PASSCODE, passcode);
                        OnBoardingViewPagerActivity.getInstance().finish();
                        goToNextActivity(DashboardActivity.class);
                    } else if (passcodeBundle.equalsIgnoreCase(Constants.VALUE_REGISTER_ACTIVITY)) {
                        SharedPreferenceManager.getInstance(PasscodeActivity.this).setFirstTimeLaunch(true);
                        SharedPreferenceManager.getInstance(PasscodeActivity.this).saveData(Constants.PASSCODE, passcode);
                        OnBoardingViewPagerActivity.getInstance().finish();
                        goToNextActivity(DashboardActivity.class);
                    } else if (passcodeBundle.equalsIgnoreCase(Constants.VALUE_FOR_GOOGLE_SIGN)) {
                        SharedPreferenceManager.getInstance(PasscodeActivity.this).setFirstTimeLaunch(true);
                        SharedPreferenceManager.getInstance(PasscodeActivity.this).saveData(Constants.PASSCODE, passcode);
                        OnBoardingViewPagerActivity.getInstance().finish();
                        goToNextActivity(DashboardActivity.class);
                    }
                }
            }
        }
    };

    protected void goToNextActivity(Class nextActivity) {
        Intent intent = new Intent();
        intent.setClass(this, nextActivity);
        startActivity(intent);
        finish();
    }

}
