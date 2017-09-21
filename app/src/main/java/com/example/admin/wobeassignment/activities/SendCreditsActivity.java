package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.wobeassignment.ApplicationLoader;
import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.model.VerifyUserModel;
import com.example.admin.wobeassignment.utilities.CommonUtils;
import com.example.admin.wobeassignment.utilities.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
                TextInputEditText etEmail = (TextInputEditText) findViewById(R.id.etEmail);
                String email = etEmail.getText().toString().trim();
                if (email != null) {
                    if (CommonUtils.isConnectingToInternet(SendCreditsActivity.this)) {
                        verifyUser(email);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void verifyUser(String email) {
        String url = String.format(Constants.VERIFY_USER_URL, email);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                VerifyUserModel model = new Gson().fromJson
                                        (response.toString(), VerifyUserModel.class);
                                String customerId = model.getCUSTOMER_ID().toString();
                            } else {
                                Toast.makeText(SendCreditsActivity.this, getResources().
                                                getString(R.string.invalid_user),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        ApplicationLoader.getRequestQueue().add(jsonObjectRequest);
    }
}
