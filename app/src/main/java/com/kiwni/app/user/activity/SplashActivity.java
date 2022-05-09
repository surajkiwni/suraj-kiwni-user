package com.kiwni.app.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{
    boolean hasLoggedIn = false;

    String TAG = this.getClass().getSimpleName();
    String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* session handling */
        hasLoggedIn = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.hasLoggedIn, false);
        Log.d(TAG, "logged in status = " + hasLoggedIn);

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
                    /* location permission */
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstants.GPS_REQUEST);

                    if(hasLoggedIn)
                    {
                        /* already login */
                        mobile = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, mobile);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        /* call home activity */
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

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
        }
        else
        {
            Log.d(TAG, "failed");
            finish();
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
}
