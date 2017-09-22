package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;

/**
 * Created by Admin on 22-09-2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialiseToolbar();
        initialiseViews();
        setData();
    }

    private void initialiseToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialiseViews() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
    }

    private void setData() {
        if (SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.FIRST_NAME) != null &&
                SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.LAST_NAME) != null) {
            tvName.setText(SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.FIRST_NAME +
                    SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.LAST_NAME)));
        }else if (SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.FIRST_NAME) != null){
            tvName.setText(SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.FIRST_NAME));
        }

        if (SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.EMAIL) != null) {
            tvEmail.setText(SharedPreferenceManager.getInstance(ProfileActivity.this).getString(Constants.EMAIL));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
