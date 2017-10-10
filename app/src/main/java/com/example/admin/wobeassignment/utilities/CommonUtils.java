package com.example.admin.wobeassignment.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;

import com.example.admin.wobeassignment.ApplicationLoader;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;

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

    public static void firebaseAnalytics(String param, String action, String event) {
        FirebaseAnalytics firebaseAnalytics = ApplicationLoader.getFirebaseInstance();
        Bundle bundle = new Bundle();
        bundle.putString(param, action);
        firebaseAnalytics.logEvent(event, bundle);
    }

    //method to generate QR Code and convert it to base64, so that it can be saved in Shared Prefs
    public static String generateQRCode(String email) {
        Bitmap bitmap = QRCode.from(email).bitmap();

        //convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        //encode base64 from byte array
        String qrCode = Base64.encodeToString(b, Base64.DEFAULT);
        return qrCode;
    }

}
