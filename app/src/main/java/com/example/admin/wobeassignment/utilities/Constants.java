package com.example.admin.wobeassignment.utilities;

/**
 * Created by Admin on 19-09-2017.
 */

public class Constants {

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String CUSTOMER_ID = "customerId";

    private static String BASE_URL = "https://www.axisshared.com:8443/MainPage?AXIS_API_KEY=i50c988cb_8895_44e7_ad6c_a74189f202a6&AXIS_API=";

    public static String Register_URL = BASE_URL + "WobeCustomerAdd&" +
            "firstName=%s" + "&lastName=%s" + "&emailAddress=%s" + "&userPassword=%s" + "&TOKEN_ID=%s";

    public static String LOGIN_URL = BASE_URL + "WobeSignIn&" + "emailAddress=%s" + "&userPassword=%s";
}
