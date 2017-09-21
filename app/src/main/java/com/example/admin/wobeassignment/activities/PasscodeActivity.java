package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                SharedPreferenceManager.getInstance(PasscodeActivity.this).saveData(Constants.PASSCODE, passcode);
            }
        }
    };
}
