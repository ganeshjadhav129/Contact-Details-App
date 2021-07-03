package com.example.realapp;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class RealApplication extends Application {
    public static final String APPLICATION_ID = "C5E1C068-252C-8151-FF4C-F6D9E074FA00";
    public static final String API_KEY = "CA972DF6-2BB1-410E-BF42-A420745D53D1";
    public static final String SERVER_URL = "https://api.backendless.com";
    public static BackendlessUser user;
    public static List<Contact> userlistt;

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),APPLICATION_ID, API_KEY );
    }
}
