# Wobe

The app is used to send credits to users of the app.

User has to Register(Google/Facebook/Email) with the app in order to send credits.

On Registration with Wobe, user is given 10,000 Credits by Wobe.

On successful Login/Registration, user is landed on the Dashboard where Credits balance and previous Transactions are shown.

A QR code is generated of the user(on Login/Register) which is shown in the user's Profile screen.

User can Send Credits to other Wobe users Manually or by scanning a QR code. A successful transaction of Credits takes place if the user enters a valid email address/scans a valid QR code and has sufficient Credit balance.


In Order to get the app running,

# GoogleSignIn Login:

Follow https://developers.google.com/identity/sign-in/android/start-integrating to get a google-services.json file and place the file in app/ directory of the project.


# Facebook SignIn:

Get facebook_app_id and fb_login_protocol_scheme and add add them in the AndroidManifest.xml file.


      <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="----facebook_app_id here----" />

        <activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" />
        <activity
            android:name="com.facebook.CustomTabActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="----fb_login_protocol_scheme here----" />
            </intent-filter>
        </activity>
     
     
Follow https://developers.facebook.com/docs/facebook-login/android/ to get facebook_app_id and fb_login_protocol_scheme.

# Crashlytics:

Follow https://fabric.io/kits/android/crashlytics/install to install Crashlytics and get the api-key.
Add the api-key in the fabric.properties file in the app/ directory.

# Firebase Analytics:

Follow https://firebase.google.com/docs/analytics/android/start/ to install Firebase in order to use Analytics.

