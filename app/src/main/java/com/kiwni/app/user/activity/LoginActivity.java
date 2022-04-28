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
import com.kiwni.app.user.datamodels.ErrorDialog;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements ConnectivityHelper.NetworkStateReceiverListener
{
    Button btnConfirm;
    EditText edtPhoneNumber;
    ImageView imageBack;

    String mobile = "";
    Boolean isPhoneNoValid = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    boolean isNetworkAvailable = false;

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationId;

    String TAG = this.getClass().getSimpleName();
    Dialog lovelyProgressDialog;

    private ConnectivityHelper connectivityHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(SharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnConfirm = findViewById(R.id.btnConfirm);
        imageBack = findViewById(R.id.imageBack);

        startNetworkBroadcastReceiver(this);

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
                /*Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
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

    public void setValidation()
    {
        //Phone no validation
        if (edtPhoneNumber.getText().toString().isEmpty())
        {
            //edtPhoneNumber.setError(getResources().getString(R.string.phoneno_error));
            //edtPhoneNo.requestFocus();
            ErrorDialog errorDialog = new ErrorDialog(getApplicationContext(), getResources().getString(R.string.phoneno_error));
            errorDialog.show();
            isPhoneNoValid = false;
        } else if (edtPhoneNumber.getText().toString().length() > 10)
        {
            //edtPhoneNumber.setError("Enter 10 digit mobile no");
            //edtPhoneNo.requestFocus();
            ErrorDialog errorDialog = new ErrorDialog(getApplicationContext(), "Enter 10 digit mobile no");
            errorDialog.show();
            isPhoneNoValid = false;
        } else {
            edtPhoneNumber.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isPhoneNoValid = true;
        }
    }

    private void sendVerificationCode(String mobile)
    {
        // Set up progress before call
        lovelyProgressDialog = new LovelyProgressDialog(LoginActivity.this)
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle("Loading..")
                .setMessage("Please wait...")
                .setTopColorRes(R.color.teal_200)
                .show();

        // OnVerificationStateChangedCallbacks
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,            // Phone number to verify
                60,         // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,        // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        lovelyProgressDialog.dismiss();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        lovelyProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"verification failed.!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        lovelyProgressDialog.dismiss();

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void networkAvailable()
    {
        //Toast.makeText(getActivity(), "internet back", Toast.LENGTH_SHORT).show();

        if(isNetworkAvailable){
            Snackbar.make(findViewById(android.R.id.content), R.string.internet_msg, Snackbar.LENGTH_LONG)
                    .setTextColor(Color.WHITE)
                    .setBackgroundTint(Color.GREEN)
                    .setDuration(5000)
                    .show();

        }

    }

    @Override
    public void networkUnavailable() {
        // Toast.makeText(getActivity(), "please check your Internet", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(Color.RED)
                .setDuration(5000)
                .show();

        isNetworkAvailable = true;
    }

    public void startNetworkBroadcastReceiver(Context currentContext) {
        connectivityHelper = new ConnectivityHelper();
        connectivityHelper.addListener(this);
        registerNetworkBroadcastReceiver(currentContext);
    }


    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(connectivityHelper,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(connectivityHelper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkBroadcastReceiver(this);
    }

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