package com.example.admin.wobeassignment;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Admin on 21-09-2017.
 */

public class ApplicationLoader extends Application {

    public static final String TAG = ApplicationLoader.class.getSimpleName();
    private static ApplicationLoader mInstance;
    private static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ApplicationLoader getInstance() {
        return mInstance;
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ApplicationLoader.getInstance());
        }

        return mRequestQueue;
    }
}
