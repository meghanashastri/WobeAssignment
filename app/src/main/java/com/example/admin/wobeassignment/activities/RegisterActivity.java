package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.wobeassignment.ApplicationLoader;
import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.fragments.GoogleSignInFragment;
import com.example.admin.wobeassignment.model.BaseModel;
import com.example.admin.wobeassignment.utilities.CommonUtils;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 20-09-2017.
 */

public class RegisterActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button fb, btnRegister;
    private EditText etFirstName, etLastName, etEmail, etPassword;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

         /*
           Facebook Login integration.
           Email permissions asked to use the customer email.
           On successful facebook login, name and email is retrieved.
           API call for Social Login is done, to save details in the server database.
           Facebook Logout is done.
         */
        fb = (Button) findViewById(R.id.btnFacebookSignUp);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        fb.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("success", "Success");
                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        Log.i("LoginActivity",
                                                response.toString());
                                        try {
                                             /*
                                               Name and Email is retrieved from Facebook success response
                                            */
                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            String facebookId = object.getString("id");

                                            String firstName = null, lastName = null;

                                              /*
                                               Name is split to Firstname and Lastname
                                            */
                                            if (name != null) {
                                                String[] parts = name.split("\\s+");
                                                if (parts.length == 1) {
                                                    firstName = parts[0];
                                                    lastName = null;
                                                } else if (parts.length == 2) {
                                                    firstName = parts[0];
                                                    lastName = parts[1];
                                                }
                                            }

                                             /*
                                              Firstname and Lastname saved in Shared Preference
                                             */
                                            SharedPreferenceManager.getInstance(RegisterActivity.this).
                                                    saveData(Constants.USERNAME, name);
                                            SharedPreferenceManager.getInstance(RegisterActivity.this).
                                                    saveData(Constants.EMAIL, email);
                                            if (facebookId != null && !facebookId.isEmpty() && name != null
                                                    && !name.isEmpty() && email != null && !email.isEmpty()) {

                                                String qrCode = CommonUtils.generateQRCode(email);
                                                SharedPreferenceManager.getInstance(RegisterActivity.this).
                                                        saveData(Constants.QR_CODE, qrCode);
                                                 /*
                                                   API call for Social Login
                                                */
                                                if (CommonUtils.isConnectingToInternet(RegisterActivity.this)) {
                                                    makeApiCallForFacebookLogin(firstName, lastName,
                                                            email, "123445555");
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, getResources().
                                                                    getString(R.string.check_internet_connection),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                facebookLogOut();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        facebookLogOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.error_message),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        initialiseViews();
    }

    /*
      Method for Facebook Logout
    */
    private void facebookLogOut() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
      Method for Social Login API call
      Request parameters - firstname, lastname, email, tokenid
      Successful response - customerid
    */
    private void makeApiCallForFacebookLogin(final String firstName, final String lastName, String email, String tokenId) {
        String url = String.format(Constants.SOCIAL_LOGIN_URL, firstName, lastName, email, tokenId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                 /*
                                  On success response from API, customerid, name and email is stored
                                  in Shared Preference and taken to the Passcode Activity
                                */
                                BaseModel model = new Gson().fromJson
                                        (response.toString(), BaseModel.class);
                                String customerId = model.getCustomerID().toString();
                                SharedPreferenceManager.getInstance(RegisterActivity.this).
                                        saveData(Constants.CUSTOMER_ID, customerId);
                                SharedPreferenceManager.getInstance(RegisterActivity.this).
                                        saveData(Constants.FIRST_NAME, firstName);
                                SharedPreferenceManager.getInstance(RegisterActivity.this).
                                        saveData(Constants.LAST_NAME, lastName);

                                Bundle bundle = new Bundle();
                                bundle.putString("Type", "Facebook");
                                firebaseAnalytics.logEvent("Register", bundle);

                                goToNextActivity(PasscodeActivity.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result code received from Facebook
        if (resultCode == RESULT_OK) {
            if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /*
    Method to initialise views
   */
    private void initialiseViews() {
        Button btnGoogleSignUp = (Button) findViewById(R.id.btnGoogleSignUp);
        btnGoogleSignUp.setOnClickListener(this);
        etFirstName = (EditText) findViewById(R.id.etFirstname);
        etLastName = (EditText) findViewById(R.id.etLastname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    /*
      Method to validate firstname, lastname, email and password
     */
    private void validation() {
        if (!(etFirstName.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_first_name), Toast.LENGTH_SHORT).show();
        } else if (!(etLastName.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_last_name), Toast.LENGTH_SHORT).show();
        } else if (!(etEmail.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_email), Toast.LENGTH_SHORT).show();
        } else if (!(etPassword.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_password), Toast.LENGTH_SHORT).show();
        } else {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String tokenId = "1234567890";

            if (firstName.length() >= 4) {
                if (isValidEmail(email)) {
                    if (password.length() >= 6) {
                        if (CommonUtils.isConnectingToInternet(RegisterActivity.this)) {

                            String qrCode = CommonUtils.generateQRCode(email);
                            SharedPreferenceManager.getInstance(RegisterActivity.this).
                                    saveData(Constants.QR_CODE, qrCode);

                            //register api call
                            makeApiCall(firstName, lastName, email, password, tokenId);
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.min_password), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.min_first_name), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Method for email validation
    private final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    /*
      Method to add fragment
    */
    private void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        GoogleSignInFragment googleSignInFragment = new GoogleSignInFragment();
        fragmentTransaction.add(R.id.fragment_holder, googleSignInFragment, "GoogleSignIn");
        fragmentTransaction.commit();
    }

    /*
      Method to handle click listeners of views
   */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnGoogleSignUp:
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_layout);
                frameLayout.setVisibility(View.VISIBLE);
                addFragment();
                break;
            case R.id.btnFacebookSignUp:
                if (CommonUtils.isConnectingToInternet(RegisterActivity.this)) {
                    loginButton.performClick();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegister:
                validation();
                break;
        }
    }

    /*
     Method for Register API call.
     Request parameters - firstname, lastname, email, password and tokenid.
     Successful Response - customerid
   */
    private void makeApiCall(final String firstName, final String lastName, final String email, String password,
                             String tokenId) {
        String url = String.format(Constants.Register_URL, firstName, lastName, email, password, tokenId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                BaseModel model = new Gson().fromJson
                                        (response.toString(), BaseModel.class);
                                String customerId = model.getCustomerID().toString();
                                SharedPreferenceManager.getInstance(getApplicationContext()).
                                        saveData(Constants.FIRST_NAME, firstName);
                                SharedPreferenceManager.getInstance(RegisterActivity.this).
                                        saveData(Constants.LAST_NAME, lastName);
                                SharedPreferenceManager.getInstance(getApplicationContext()).
                                        saveData(Constants.EMAIL, email);
                                SharedPreferenceManager.getInstance(getApplicationContext()).
                                        saveData(Constants.CUSTOMER_ID, customerId);

                                Bundle bundle = new Bundle();
                                bundle.putString("Type", "Email");
                                firebaseAnalytics.logEvent("Register", bundle);

                                goToNextActivity(PasscodeActivity.class);
                            } else {
                                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.existing_user),
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

    /*
      Method to go to next activity
    */
    protected void goToNextActivity(Class nextActivity) {
        Intent intent = new Intent();
        intent.setClass(this, nextActivity);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_PASSCODE_ACTIVITY_BUNDLE, Constants.VALUE_REGISTER_ACTIVITY);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}

