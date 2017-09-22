package com.example.admin.wobeassignment.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.admin.wobeassignment.utilities.FontManager;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

/**
 * Created by Admin on 21-09-2017.
 */

public class SendCreditsActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etCredits, etDescription, etEmail;
    private Button btnSendCredits, tvVerify;
    private String toCustomerId, fromCustomerId, toFirstName, toLastName, fromFirstName, fromLastName;
    private TextView tvName, tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_credits);
        initialiseToolbar();
        initialiseViews();
        fromCustomerId = SharedPreferenceManager.getInstance(this).getString(Constants.CUSTOMER_ID);
    }

    private void initialiseViews() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        tvVerify = (Button) findViewById(R.id.tvVerify);
        tvVerify.setOnClickListener(this);
        etCredits = (EditText) findViewById(R.id.etCredits);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSendCredits = (Button) findViewById(R.id.btnSend);
        btnSendCredits.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tvName);
        tvBalance = (TextView) findViewById(R.id.tvBalance);

        if (SharedPreferenceManager.getInstance(SendCreditsActivity.this).getString(Constants.FIRST_NAME) != null) {
            tvName.setText(SharedPreferenceManager.getInstance(SendCreditsActivity.this).getString(Constants.FIRST_NAME));
        }

        if (SharedPreferenceManager.getInstance(SendCreditsActivity.this).getString(Constants.CREDITS) != null) {
            tvBalance.setText(getResources().getString(R.string.balance) + SharedPreferenceManager.
                    getInstance(SendCreditsActivity.this).getString(Constants.CREDITS));
        }


    }

    private void initialiseToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.send_credits));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvVerify:
                String email = etEmail.getText().toString().trim();
                if (email != null && !email.isEmpty()) {
                    if (email.equalsIgnoreCase(SharedPreferenceManager.getInstance(SendCreditsActivity.this).
                            getString(Constants.EMAIL))) {
                        Toast.makeText(this, getString(R.string.cannot_send_credits), Toast.LENGTH_SHORT).show();
                    } else {
                        if (CommonUtils.isConnectingToInternet(SendCreditsActivity.this)) {
                            verifyUserApiCall(email);
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                    Toast.LENGTH_SHORT).show();
                        }
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
                                etCredits.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                                etDescription.setEnabled(true);
                                etDescription.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                                btnSendCredits.setEnabled(true);
                                etEmail.setTextColor(getResources().getColor(R.color.edit_text_disable_color));
                                toFirstName = model.getFIRST_NAME();
                                toLastName = model.getLAST_NAME();
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

        if (SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME) != null &&
                SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME) != null) {
            fromFirstName = SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME);
            fromLastName = SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME);
            ;
        }
        if (SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME) != null) {
            fromLastName = SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME);
        }

        String url = String.format(Constants.SEND_CREDITS_URL, fromCustomerId, fromFirstName, fromLastName,
                toCustomerId, toFirstName, toLastName, credits, description, null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                BaseModel model = new Gson().fromJson
                                        (response.toString(), BaseModel.class);
                                showSuccessDialog();
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

    private void showSuccessDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) this.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, null);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        TextView success = (TextView) view.findViewById(R.id.success);
        success.setTypeface(iconFont);
        Button ok = (Button) view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
