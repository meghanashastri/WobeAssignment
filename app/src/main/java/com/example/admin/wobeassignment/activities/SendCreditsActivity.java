package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.admin.wobeassignment.R;

/**
 * Created by Admin on 21-09-2017.
 */

public class SendCreditsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_credits);
        initialiseViews();
    }

    private void initialiseViews() {
        TextView tvVerify = (TextView) findViewById(R.id.tvVerify);
        tvVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvVerify:
                break;
        }
    }
}
