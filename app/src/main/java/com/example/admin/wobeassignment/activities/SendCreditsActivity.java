package com.example.admin.wobeassignment.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.wobeassignment.ApplicationLoader;
import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.model.BaseModel;
import com.example.admin.wobeassignment.model.VerifyUserModel;
import com.example.admin.wobeassignment.utilities.CommonUtils;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

/**
 * Created by Admin on 21-09-2017.
 */

public class SendCreditsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvVerify;
    private TextInputEditText etCredits, etDescription, etEmail;
    private Button btnSendCredits;
    private String toCustomerId, fromCustomerId, toFirstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_credits);
        initialiseViews();
        fromCustomerId = SharedPreferenceManager.getInstance(this).getString(Constants.CUSTOMER_ID);
    }

    private void initialiseViews() {
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        tvVerify = (TextView) findViewById(R.id.tvVerify);
        tvVerify.setOnClickListener(this);
        etCredits = (TextInputEditText) findViewById(R.id.etCredits);
        etDescription = (TextInputEditText) findViewById(R.id.etDescription);
        btnSendCredits = (Button) findViewById(R.id.btnSend);
        btnSendCredits.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvVerify:
                String email = etEmail.getText().toString().trim();
                if (email != null && !email.isEmpty()) {
                    if (CommonUtils.isConnectingToInternet(SendCreditsActivity.this)) {
                        verifyUserApiCall(email);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.enter_email_to_verify), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSend:
                sendCreditsValidation();
                break;
        }
    }

    private void verifyUserApiCall(final String email) {
        String url = String.format(Constants.VERIFY_USER_URL, email);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                VerifyUserModel model = new Gson().fromJson
                                        (response.toString(), VerifyUserModel.class);
                                toCustomerId = model.getCUSTOMER_ID().toString();
                                tvVerify.setVisibility(View.GONE);
                                etEmail.setEnabled(false);
                                etCredits.setEnabled(true);
                                etDescription.setEnabled(true);
                                btnSendCredits.setEnabled(true);
                                toFirstName = email;
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

    private void sendCreditsValidation() {
        if (!(etCredits.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_cedits), Toast.LENGTH_SHORT).show();
        } else if (!(etDescription.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_description), Toast.LENGTH_SHORT).show();
        } else {
            checkCreditBalance(etCredits.getText().toString().trim());
        }
    }

    private void checkCreditBalance(String credits) {

        BigInteger toCredits = BigInteger.valueOf(Long.valueOf(credits));
        BigInteger userCredits = BigInteger.valueOf(Long.valueOf(SharedPreferenceManager.
                getInstance(this).getString(Constants.CREDITS)));

        int availableCredits = userCredits.compareTo(toCredits);

        if (availableCredits == 0) {
            if (CommonUtils.isConnectingToInternet(SendCreditsActivity.this)) {
                sendCreditsApiCall(etCredits.getText().toString().trim(), etDescription.getText().toString());
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (availableCredits == 1) {
            if (CommonUtils.isConnectingToInternet(SendCreditsActivity.this)) {
                sendCreditsApiCall(etCredits.getText().toString().trim(), etDescription.getText().toString());
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (availableCredits == -1) {
            Toast.makeText(this, getResources().getString(R.string.insufficient_credits), Toast.LENGTH_SHORT).show();
        }

    }


    private void sendCreditsApiCall(String credits, String description) {
        String fromFirstName, fromLastName;
        if (SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME) != null) {
            fromFirstName = SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME);
        } else {
            fromFirstName = null;
        }
        if (SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME) != null) {
            fromLastName = SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME);
        } else {
            fromLastName = null;
        }


        String url = String.format(Constants.SEND_CREDITS_URL, fromCustomerId, fromFirstName, fromLastName,
                toCustomerId, toFirstName, null, credits, description, null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                BaseModel model = new Gson().fromJson
                                        (response.toString(), BaseModel.class);

                                //sucess dialog to be shown
                                Toast.makeText(SendCreditsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SendCreditsActivity.this, getResources().getString(R.string.error_message),
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
