package com.kiwni.app.user.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kiwni.app.user.R;
import com.kiwni.app.user.classes.LoadingDialog;
import com.kiwni.app.user.datamodels.ErrorDialog;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements ConnectivityHelper.NetworkStateReceiverListener
{
    Button btnConfirm;
    EditText edtPhoneNumber;
    ImageView imageBack;

    String mobile = "";
    Boolean isPhoneNoValid = false, isNetworkAvailable = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /* variable for FirebaseAuth class */
    FirebaseAuth mAuth;
    String verificationId;

    String TAG = this.getClass().getSimpleName();
    LoadingDialog loadingDialog;

    private ConnectivityHelper connectivityHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* getting instance of our FirebaseAuth. */
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(SharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnConfirm = findViewById(R.id.btnConfirm);
        imageBack = findViewById(R.id.imageBack);

        // loading dialog
        loadingDialog = new LoadingDialog(this);

        /* broadcast internet connection msg */
        startNetworkBroadcastReceiver(this);

        /* change button color on click */
        edtPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnConfirm.setBackgroundResource(R.drawable.black_rounded_button);
                return false;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ConnectivityHelper.isConnected)
                {
                    mobile = edtPhoneNumber.getText().toString();
                    Log.d("TAG", "mobile = " + mobile);

                    setValidation();

                    if(isPhoneNoValid)
                    {
                        if(!mobile.contains("+91"))
                        {
                            mobile = "+91" + mobile;
                        }

                        sendVerificationCode(mobile);
                    }
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                            .setTextColor(Color.WHITE)
                            .setBackgroundTint(Color.RED)
                            .setDuration(5000)
                            .show();
                }
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    /* validation for empty fields and correct format of string */
    public void setValidation()
    {
        /* Phone no validation */
        if (edtPhoneNumber.getText().toString().isEmpty())
        {
            edtPhoneNumber.setError(Html.fromHtml("<font color='white'>Please enter valid phone no</font>"));
            isPhoneNoValid = false;
        }
        else if (edtPhoneNumber.getText().toString().length() > 10)
        {
            edtPhoneNumber.setError(Html.fromHtml("<font color='white'>Enter 10 digit mobile no</font>"));
            isPhoneNoValid = false;
        }
        else
        {
            edtPhoneNumber.setError(null);
            isPhoneNoValid = true;
        }
    }

    private void sendVerificationCode(String mobile)
    {
        Log.d(TAG, "mobile = " + mobile);
        /*Set up progress before call*/
        loadingDialog.showLoadingDialog("Your request is processing");

        /*OnVerificationStateChangedCallbacks*/
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,            // Phone number to verify
                60,         // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,        // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        loadingDialog.hideDialog();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        loadingDialog.hideDialog();
                        //Toast.makeText(LoginActivity.this,"verification failed.!",Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        loadingDialog.hideDialog();

                        verificationId = s;
                        Toast.makeText(LoginActivity.this,"Code sent",Toast.LENGTH_SHORT).show();

                        if(!s.equals(null))
                        {
                            Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("verificationId",verificationId);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);
                            //finish();
                        }
                    }
                });
    }

    /* internet connection available callback method */
    @SuppressLint("ResourceAsColor")
    @Override
    public void networkAvailable()
    {
        if(isNetworkAvailable)
        {
            Snackbar.make(findViewById(android.R.id.content), R.string.internet_msg, Snackbar.LENGTH_LONG)
                    .setTextColor(Color.WHITE)
                    .setBackgroundTint(Color.GREEN)
                    .setDuration(5000)
                    .show();
        }
    }

    /* internet connection not available callback method */
    @Override
    public void networkUnavailable()
    {
        Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(Color.RED)
                .setDuration(5000)
                .show();

        isNetworkAvailable = true;
    }

    /* internet connection broadcast receiver */
    public void startNetworkBroadcastReceiver(Context currentContext) {
        connectivityHelper = new ConnectivityHelper();
        connectivityHelper.addListener(this);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /* register receiver */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(connectivityHelper,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    /* unregister receiver */
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(connectivityHelper);
    }

    /* call register receiver here */
    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkBroadcastReceiver(this);
    }

    /* call unregister receiver here */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterNetworkBroadcastReceiver(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            // Toast.makeText(getApplicationContext(), "back button pressed", Toast.LENGTH_LONG).show(); //doesn't work here
            finish();
            return true;
        }

        if (keyCode==KeyEvent.KEYCODE_HOME)
        {
            //Toast.makeText(getApplicationContext(), "home button pressed", Toast.LENGTH_LONG).show(); //doesn't work here
            finish();
            return true;
        }

        if(keyCode==KeyEvent.KEYCODE_ESCAPE)
        {
            //Toast.makeText(getApplicationContext(), "Escape button pressed", Toast.LENGTH_LONG).show(); //doesn't work here
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}