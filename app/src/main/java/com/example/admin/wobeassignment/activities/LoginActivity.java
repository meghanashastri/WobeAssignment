package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 19-09-2017.
 */

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        Button fb = (Button) findViewById(R.id.btnFacebookSignIn);
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

                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            String facebookId = object.getString("id");

                                            String firstName = null, lastName = null;

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
                                            SharedPreferenceManager.getInstance(LoginActivity.this).
                                                    saveData(Constants.USERNAME, name);
                                            SharedPreferenceManager.getInstance(LoginActivity.this).
                                                    saveData(Constants.EMAIL, email);
                                            if (facebookId != null && !facebookId.isEmpty() && name != null
                                                    && !name.isEmpty() && email != null && !email.isEmpty()) {
                                                if (CommonUtils.isConnectingToInternet(LoginActivity.this)) {
                                                    makeApiCallForFacebookLogin(firstName, lastName,
                                                            email, "123445555");
                                                } else {
                                                    Toast.makeText(LoginActivity.this, getResources().
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
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_message),
                                Toast.LENGTH_SHORT).show();
                    }
                });


        initialiseViews();
    }

    private void facebookLogOut() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void makeApiCallForFacebookLogin(String firstName, String lastName, String email, String tokenId) {
        String url = String.format(Constants.SOCIAL_LOGIN_URL, firstName, lastName, email, tokenId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                BaseModel model = new Gson().fromJson
                                        (response.toString(), BaseModel.class);
                                String customerId = model.getCustomerID().toString();
                                SharedPreferenceManager.getInstance(LoginActivity.this).
                                        saveData(Constants.CUSTOMER_ID, customerId);
                                goToNextActivity(DashboardActivity.class);
                                Toast.makeText(LoginActivity.this, customerId, Toast.LENGTH_SHORT).show();
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

        if (resultCode == RESULT_OK) {
            if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    private void initialiseViews() {
        Button btnGoogleSignIn = (Button) findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(this);
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        Button btnLogin = (Button) findViewById(R.id.btnUserLogin);
        btnLogin.setOnClickListener(this);
    }


    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GoogleSignInFragment googleSignInFragment = new GoogleSignInFragment();
        fragmentTransaction.add(R.id.fragment_holder, googleSignInFragment, "GoogleSignIn");
        fragmentTransaction.commit();
    }

    private void validation() {
        if (!(etEmail.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_email), Toast.LENGTH_SHORT).show();
        } else if (!(etPassword.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_password), Toast.LENGTH_SHORT).show();
        } else {
            //login api call
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (CommonUtils.isConnectingToInternet(LoginActivity.this)) {
                makeApiCallForLogin(email, password);
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnGoogleSignIn:
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_layout);
                frameLayout.setVisibility(View.VISIBLE);
                addFragment();
                break;
            case R.id.btnFacebookSignIn:
                if (CommonUtils.isConnectingToInternet(LoginActivity.this)) {
                    loginButton.performClick();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnUserLogin:
                validation();
                break;

        }
    }

    private void makeApiCallForLogin(String email, String password) {
        String url = String.format(Constants.LOGIN_URL, email, password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.getString("returnStatus").equalsIgnoreCase("SUCCESS")) {
                                BaseModel model = new Gson().fromJson
                                        (response.toString(), BaseModel.class);
                                String customerId = model.getCustomerID().toString();
                                SharedPreferenceManager.getInstance(getApplicationContext()).saveData(Constants.CUSTOMER_ID, customerId);
                                goToNextActivity(DashboardActivity.class);
                                Toast.makeText(LoginActivity.this, customerId, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_credentials),
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

    protected void goToNextActivity(Class nextActivity) {
        SharedPreferenceManager.getInstance(this).setFirstTimeLaunch(true);
        Intent intent = new Intent();
        intent.setClass(this, nextActivity);
        startActivity(intent);
        finish();
    }

}