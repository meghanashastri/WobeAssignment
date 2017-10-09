package com.example.admin.wobeassignment.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;

/**
 * Created by Admin on 22-09-2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName, tvEmail;
    private ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialiseToolbar();
        initialiseViews();
        setData();
    }

    /*
      Method to initialise toolbar and set title
    */
    private void initialiseToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
      Method to initialise views
    */
    private void initialiseViews() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
    }

    /*
      Method to set data in views from Shared Preference
    */
    private void setData() {
        if (SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME) != null &&
                SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME) != null) {
            String firstname = SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME);
            String lastname = SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME);
            tvName.setText(firstname + " " + lastname);
        } else if (SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME) != null) {
            tvName.setText(SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME));
        }
        if (SharedPreferenceManager.getInstance(this).getString(Constants.EMAIL) != null) {
            tvEmail.setText(SharedPreferenceManager.getInstance(this).getString(Constants.EMAIL));
        }

        if (SharedPreferenceManager.getInstance(this).getString(Constants.QR_CODE) !=null){
            String qrCode = SharedPreferenceManager.getInstance(this).getString(Constants.QR_CODE);
            byte[] imageAsBytes = Base64.decode(qrCode, Base64.DEFAULT);
            ivQRCode.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
    }

    /*
       Method to handle back arrow click from toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
