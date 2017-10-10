package com.example.admin.wobeassignment.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

/**
 * Created by Admin on 10-10-2017.
 */

public class ScanQRCode extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etCredits, etDescription;
    private String toCustomerId, fromCustomerId, toFirstName, toLastName, fromFirstName, fromLastName;
    private Button btnPay, tvVerify;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        firebaseAnalytics = ApplicationLoader.getFirebaseInstance();
        initialiseToolbar();
        initialiseScan();
        initialiseViews();
        fromCustomerId = SharedPreferenceManager.getInstance(this).getString(Constants.CUSTOMER_ID);
    }

    /*
      Method to initialise toolbar and set title
    */
    private void initialiseToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.scan_qr_code));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
      Method to initialise the barcode scanner
    */
    private void initialiseScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt(getResources().getString(R.string.scan_qr_code));
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    private void initialiseViews() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCredits = (EditText) findViewById(R.id.etCredits);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnPay = (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        tvVerify = (Button) findViewById(R.id.tvVerify);
        tvVerify.setOnClickListener(this);
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


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, getResources().getString(R.string.result_not_found), Toast.LENGTH_LONG).show();
                tvVerify.setVisibility(View.VISIBLE);
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to view
                    etEmail.setText(obj.getString(Constants.EMAIL));
                    if (CommonUtils.isConnectingToInternet(ScanQRCode.this)) {
                        verifyUserApiCall(result.getContents());
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    etEmail.setText(result.getContents());
                    if (CommonUtils.isConnectingToInternet(ScanQRCode.this)) {
                        verifyUserApiCall(result.getContents());
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnPay:
                sendCreditsValidation();
                break;
            case R.id.tvVerify:
                String email = etEmail.getText().toString().trim();
                if (email != null && !email.isEmpty()) {
                    if (CommonUtils.isConnectingToInternet(ScanQRCode.this)) {
                        verifyUserApiCall(email);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.enter_email_to_verify), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*
    Method for User validation API call.
    Request parameters - email.
    Successful response - toCustomerId, toFirstName, toLastname
  */
    private void verifyUserApiCall(final String email) {
        if (email.equalsIgnoreCase(SharedPreferenceManager.getInstance(this).getString(Constants.EMAIL))) {
            Toast.makeText(this, getString(R.string.cannot_send_credits), Toast.LENGTH_SHORT).show();
        } else {
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
                                    btnPay.setEnabled(true);
                                    etEmail.setTextColor(getResources().getColor(R.color.edit_text_disable_color));
                                    toFirstName = model.getFIRST_NAME();
                                    toLastName = model.getLAST_NAME();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("Result", "Yes");
                                    firebaseAnalytics.logEvent("SendCreditsUserVerification", bundle);
                                } else {
                                    Toast.makeText(ScanQRCode.this, getResources().
                                                    getString(R.string.invalid_user),
                                            Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Result", "No");
                                    firebaseAnalytics.logEvent("SendCreditsUserVerification", bundle);
                                    tvVerify.setVisibility(View.VISIBLE);
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


    /*
     Method for credits entered and description entered validation
   */
    private void sendCreditsValidation() {
        if (!(etCredits.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_cedits), Toast.LENGTH_SHORT).show();
        } else if (!(etDescription.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_description), Toast.LENGTH_SHORT).show();
        } else {
            checkCreditBalance(etCredits.getText().toString().trim());
        }
    }

    /*
    Method to check credit balance with the entered credits.
    If entered credit <= the credit balance, then Send Credits API call is done.
    else, customer is indicated that the balance is insufficient
   */
    private void checkCreditBalance(String credits) {
        BigInteger toCredits = BigInteger.valueOf(Long.valueOf(credits));
        BigInteger userCredits = BigInteger.valueOf(Long.valueOf(SharedPreferenceManager.
                getInstance(this).getString(Constants.CREDITS)));
        int availableCredits = userCredits.compareTo(toCredits);
        if (availableCredits == 0) {
            if (CommonUtils.isConnectingToInternet(ScanQRCode.this)) {
                btnPay.setEnabled(false);
                sendCreditsApiCall(etCredits.getText().toString().trim(), etDescription.getText().toString());
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (availableCredits == 1) {
            if (CommonUtils.isConnectingToInternet(ScanQRCode.this)) {
                btnPay.setEnabled(false);
                sendCreditsApiCall(etCredits.getText().toString().trim(), etDescription.getText().toString());
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (availableCredits == -1) {
            Toast.makeText(this, getResources().getString(R.string.insufficient_credits), Toast.LENGTH_SHORT).show();
        }

    }


    /*
     Method to make Send Credits API call.

     Request parameters - fromCustomerId, fromFirstname, fromLastname,
     toCustomerId, toFirstname, toLastname, credits, description, noteToSelf

     Successful response - Success Message
  */
    private void sendCreditsApiCall(final String credits, String description) {
        if (SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME) != null &&
                SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME) != null) {
            fromFirstName = SharedPreferenceManager.getInstance(this).getString(Constants.FIRST_NAME);
            fromLastName = SharedPreferenceManager.getInstance(this).getString(Constants.LAST_NAME);
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

                                Bundle bundle = new Bundle();
                                bundle.putString("Type", "QRCode");
                                bundle.putString(FirebaseAnalytics.Param.VALUE, credits);
                                firebaseAnalytics.logEvent("SendCredits", bundle);

                                showSuccessDialog();
                            } else {
                                Toast.makeText(ScanQRCode.this, getResources().getString(R.string.error_message),
                                        Toast.LENGTH_SHORT).show();
                                btnPay.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnPay.setEnabled(true);
            }
        });
        ApplicationLoader.getRequestQueue().add(jsonObjectRequest);
    }

    /*
       Method to show success dialog
    */
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
