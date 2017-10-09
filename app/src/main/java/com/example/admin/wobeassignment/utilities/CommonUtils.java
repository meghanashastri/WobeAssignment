package com.example.admin.wobeassignment.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.admin.wobeassignment.ApplicationLoader;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Admin on 19-09-2017.
 */

public class CommonUtils {

    /**
     * Check internet is connected or not
     *
     * @param context activity context
     * @return boolean
     */
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void firebaseAnalytics(String event, String action) {
        FirebaseAnalytics firebaseAnalytics = ApplicationLoader.getFirebaseInstance();
        Bundle bundle = new Bundle();
        bundle.putString(event, action);
        firebaseAnalytics.logEvent(event, bundle);
    }
}
