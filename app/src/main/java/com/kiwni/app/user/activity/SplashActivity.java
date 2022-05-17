package com.kiwni.app.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{
    boolean hasLoggedIn = false;

    String TAG = this.getClass().getSimpleName();
    String mobile = "";
    private static final int REQUEST_CHECK_SETTINGS = 100;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    ConstraintLayout constraintLayoutNetwork;
    ImageView imgSplash;
    AppCompatButton btnGoToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        constraintLayoutNetwork = findViewById(R.id.constraintLayoutNetwork);
        imgSplash = findViewById(R.id.imgSplash);
        btnGoToSettings = findViewById(R.id.btnGoToSettings);

        /* session handling */
        hasLoggedIn = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.hasLoggedIn, false);
        Log.d(TAG, "logged in status = " + hasLoggedIn);

        //call for location enable check
        if (checkLocationPermission()) {
            displayLocationSettingsRequest(this);
        }

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

    }
    // app location permission

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        displayLocationSettingsRequest(this);
                    }

                } else {
                    imgSplash.setVisibility(View.GONE);
                    constraintLayoutNetwork.setVisibility(View.VISIBLE);

                }

            }

        }
    }

    //enable device location mobile
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run()
                            {
                                if(hasLoggedIn)
                                {
                                    /* already login */
                                    mobile = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");
                                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                    PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, mobile);
                                    startActivity(i);
                                }
                                else
                                {
                                    /* call home activity */
                                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(i);
                                }
                                finish();
                            }
                        }, AppConstants.SPLASH_SCREEN_TIME_OUT);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    Log.i("TAG", "onActivityResult: GPS Enabled by user");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            if(hasLoggedIn)
                            {
                                /* already login */
                                mobile = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, mobile);
                                startActivity(i);
                            }
                            else
                            {
                                /* call home activity */
                                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(i);
                            }
                            finish();
                        }
                    }, AppConstants.SPLASH_SCREEN_TIME_OUT);
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    finish();
                    Log.i("TAG", "onActivityResult: User rejected GPS request");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checkLocationPermission()) {
            displayLocationSettingsRequest(SplashActivity.this);
            imgSplash.setVisibility(View.VISIBLE);
            constraintLayoutNetwork.setVisibility(View.GONE);
        }
    }
}
