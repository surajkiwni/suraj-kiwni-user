package com.kiwni.app.user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

public class SplashActivity extends AppCompatActivity
{
    /*private static int SPLASH_SCREEN_TIME_OUT = 2000;
    private static int GPS_ON_CODE = 1001;*/
    boolean hasLoggedIn = false;

    FusedLocationProviderClient mFusedLocationClient;
    double currentLatitude = 0.0, currentLongitude = 0.0;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String TAG = this.getClass().getSimpleName();
    String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_splash);
        //this will bind your MainActivity.class file with activity_main.

        hasLoggedIn = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.hasLoggedIn, false);
        Log.d("TAG", "logged in status = " + hasLoggedIn);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SplashActivity.this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations())
                {
                    if (location != null)
                    {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        Log.d(TAG, currentLatitude + ", " + currentLongitude);
                        Log.d(TAG, "current location = " + currentLatitude + ", " + currentLongitude);

                        if (currentLatitude != 0.0 && currentLongitude != 0.0)
                        {
                            /*Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                            PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LAT, String.valueOf(currentLatitude));
                            PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LNG, String.valueOf(currentLongitude));

                            startActivity(i);
                            finish();*/
                            if(hasLoggedIn)
                            {
                                mobile = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");

                                Intent i = new Intent(SplashActivity.this, MainActivity.class);

                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, mobile);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                                PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LAT, String.valueOf(currentLatitude));
                                PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LNG, String.valueOf(currentLongitude));

                                startActivity(i);
                                finish();
                            }
                        }

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
                else
                {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstants.GPS_REQUEST);
                    GetCurrentLocation();
                }
            }
        }, AppConstants.SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstants.GPS_REQUEST);
                }
            }
            else
            {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.GPS_REQUEST)
        {
            //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ON_CODE);
            GetCurrentLocation();
        }
        else
        {
            Log.d("TAG", "failed");
            finish();
        }
    }

    public void GetCurrentLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(SplashActivity.this, location -> {
            if (location != null)
            {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                Log.d(TAG, "current location = " + currentLatitude + ", " + currentLongitude);

                if (currentLatitude != 0.0 && currentLongitude != 0.0)
                {
                    /*Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                    PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LAT, String.valueOf(currentLatitude));
                    PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LNG, String.valueOf(currentLongitude));

                    startActivity(i);
                    finish();*/
                    if(hasLoggedIn)
                    {
                        mobile = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");

                        Intent i = new Intent(SplashActivity.this, MainActivity.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, mobile);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                        PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LAT, String.valueOf(currentLatitude));
                        PreferencesUtils.putPreferences(SplashActivity.this, SharedPref.USER_CURRENT_LNG, String.valueOf(currentLongitude));

                        startActivity(i);
                        finish();
                    }
                }
                else {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            } else {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                //finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
