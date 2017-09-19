package com.example.admin.wobeassignment.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.activities.DashboardActivity;
import com.example.admin.wobeassignment.utilities.CommonUtils;
import com.example.admin.wobeassignment.utilities.Constants;
import com.example.admin.wobeassignment.utilities.SharedPreferenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Admin on 19-09-2017.
 */

public class GoogleSignInFragment extends android.support.v4.app.Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static String TAG = GoogleSignInFragment.class.toString();
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        context = getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_google_sign_in,
                container, false);
        initialiseViews(rootView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        if (CommonUtils.isConnectingToInternet(getActivity())) {
            googleSignIn();
        } else {
            Toast.makeText(context, getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }


    private void googleSignIn() {
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                    DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            if (null == mGoogleApiClient) {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() /* FragmentActivity */,
                                this/* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialiseViews(View view) {
        SignInButton signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sign_in_button:
                if (CommonUtils.isConnectingToInternet(getActivity())) {
                    signIn();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String userName = acct.getDisplayName();
            String email = acct.getEmail();
            String googleId = acct.getId();
            SharedPreferenceManager.getInstance(context).saveData(Constants.USERNAME, userName);
            SharedPreferenceManager.getInstance(context).saveData(Constants.EMAIL, email);
            goToNextActivity(DashboardActivity.class);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getActivity(), getResources().getString(R.string.authentication_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void goToNextActivity(Class nextActivity) {
        SharedPreferenceManager.getInstance(getContext()).setFirstTimeLaunch(true);
        Intent intent = new Intent();
        intent.setClass(getContext(), nextActivity);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
}

