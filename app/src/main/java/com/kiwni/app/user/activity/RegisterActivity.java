package com.kiwni.app.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.kiwni.app.user.R;
import com.kiwni.app.user.network.ConnectivityHelper;

public class RegisterActivity extends AppCompatActivity implements ConnectivityHelper.NetworkStateReceiverListener
{

    AppCompatButton btnRegister;
    ImageView imageBack;
    TextInputEditText txtInputPassword;
    boolean isEmailValid = false, isPhoneNoValid = false, isPasswordValid = false,
            isLastNameValid = false, isFirstNameValid = false, isNetworkAvailable = false;
    EditText edtEmailId , edtPhoneNo, edtLastName, edtFirstName;
    String email = "", phone_no = "", last_name = "", first_name = "";
    private ConnectivityHelper connectivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        imageBack = findViewById(R.id.imageBack);

        edtEmailId = findViewById(R.id.edtEmail);
        edtPhoneNo = findViewById(R.id.edtMobile);
        edtLastName = findViewById(R.id.edtLastName);
        edtFirstName = findViewById(R.id.edtFirstName);

        /* broadcast internet connection msg */
        startNetworkBroadcastReceiver(this);

        txtInputPassword = findViewById(R.id.txtPasswordInput);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* add validation for email, phone no, name fields */
                setValidation();

                if(isFirstNameValid && isLastNameValid && isPhoneNoValid && isEmailValid && isPasswordValid)
                {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
            }
        });
    }

    /* validation for empty fields and correct format of string */
    public void setValidation()
    {
        /* Email validation */
        if (edtEmailId.getText().toString().isEmpty()) {
            edtEmailId.setError(getResources().getString(R.string.email_error));
            //edtEmailId.requestFocus();
            isEmailValid = false;
        } else if (!edtEmailId.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            edtEmailId.setError("Invalid email address");
            //edtEmailId.requestFocus();
            isEmailValid = false;
        } else {
            edtEmailId.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isEmailValid = true;
        }

        /* Phone no validation */
        if (edtPhoneNo.getText().toString().isEmpty()) {
            edtPhoneNo.setError(getResources().getString(R.string.phoneno_error));
            //edtPhoneNo.requestFocus();
            isPhoneNoValid = false;
        } else if (edtPhoneNo.getText().toString().length() > 10) {
            edtPhoneNo.setError("Enter 10 digit mobile no");
            //edtPhoneNo.requestFocus();
            isPhoneNoValid = false;
        } else {
            edtPhoneNo.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isPhoneNoValid = true;
        }

        /* password validation */
        if (txtInputPassword.getText().toString().isEmpty()) {
            txtInputPassword.setError(getResources().getString(R.string.password_error));
            //edtPassword.requestFocus();
            isPasswordValid = false;
        } else if (txtInputPassword.getText().toString().length() < 8) {
            txtInputPassword.setError("Enter valid password.");
            //edtPassword.requestFocus();
            isPasswordValid = false;
        } else {
            txtInputPassword.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isPasswordValid = true;
        }

        /*Last name validation*/
        if (edtLastName.getText().toString().isEmpty()) {
            edtLastName.setError(getResources().getString(R.string.last_name_error));
            isLastNameValid = false;
        }
        else {
            edtLastName.setError(null);
            isLastNameValid = true;
        }

        /*First name validation*/
        if (edtFirstName.getText().toString().isEmpty()) {
            edtFirstName.setError(getResources().getString(R.string.first_name_error));
            isFirstNameValid = false;
        }
        else {
            edtFirstName.setError(null);
            isFirstNameValid = true;
        }
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

    @Override
    public void networkAvailable() {
        if(isNetworkAvailable)
        {
            Snackbar.make(findViewById(android.R.id.content), R.string.internet_msg, Snackbar.LENGTH_LONG)
                    .setTextColor(Color.WHITE)
                    .setBackgroundTint(Color.GREEN)
                    .setDuration(5000)
                    .show();
        }
    }

    @Override
    public void networkUnavailable() {
        Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(Color.RED)
                .setDuration(5000)
                .show();

        isNetworkAvailable = true;
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
}