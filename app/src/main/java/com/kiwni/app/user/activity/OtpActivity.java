package com.kiwni.app.user.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class OtpActivity extends AppCompatActivity implements ConnectivityHelper.NetworkStateReceiverListener
{
    Button btnLogin;
    TextView txtPhoneNo;
    PinView pinView;
    ImageView imgBack;

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    // string for storing our verification ID
    String verificationId;
    String mobileno = "";
    String otp_code = "",
            firebaseEmail = "", firebasePhoneNumber = "",
            firebaseName = "", firebaseUid = "";
    String partyId = "", refreshToken = "", idToken = "", roles = "";

    String TAG = this.getClass().getSimpleName();
    Dialog lovelyProgressDialog;
    boolean isNetworkAvailable = false;

    private ConnectivityHelper connectivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        pinView = findViewById(R.id.pinView);
        imgBack = findViewById(R.id.imgBack);
        btnLogin = findViewById(R.id.btnLogin);

        startNetworkBroadcastReceiver(this);

        mobileno = getIntent().getStringExtra("mobile");
        if (mobileno != null) {
            if (!mobileno.contains("+91")) {
                mobileno = "+91" + mobileno;
            }
            txtPhoneNo.setText(mobileno);
        }

        verificationId = getIntent().getStringExtra("verificationId");
        Log.d(TAG, "verificationId == " + verificationId);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();*/

                if(ConnectivityHelper.isConnected)
                {
                    otp_code = pinView.getText().toString();
                    Log.d(TAG, "otp_code = " + otp_code);

                    if (otp_code.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please Enter valid code", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        verifyCode(otp_code);
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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // below method is use to verify code from Firebase.
    private void verifyCode(String code)
    {
        Log.d(TAG, "verificationId = " + verificationId);
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        lovelyProgressDialog = new LovelyProgressDialog(OtpActivity.this)
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle("Loading..")
                .setMessage("Please wait...")
                .setTopColorRes(R.color.teal_200)
                .show();

        // after getting credential we are
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        lovelyProgressDialog.dismiss();

                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUser user = task.getResult().getUser();

                            if (user.getEmail() != null
                                    && user.getPhoneNumber() != null
                                    && user.getDisplayName() != null
                                    && user.getUid() != null) {
                                firebaseEmail = user.getEmail();
                                firebasePhoneNumber = user.getPhoneNumber();
                                firebaseName = user.getDisplayName();
                                firebaseUid = user.getUid();
                            }

                            System.out.println("firebaseEmail = " + firebaseEmail);
                            System.out.println("firebasePhoneNumber = " + firebasePhoneNumber);
                            System.out.println("firebaseName = " + firebaseName);
                            System.out.println("firebaseUid = " + firebaseUid);

                            user.getIdToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {
                                                lovelyProgressDialog.dismiss();

                                                System.out.println("task = " + task.getResult().toString());

                                                if (task.getResult().getClaims().get("partyId") != null) {
                                                    partyId = String.valueOf(task.getResult().getClaims().get("partyId"));
                                                    Log.d(TAG, "partyId = " + partyId);
                                                }

                                                if (task.getResult().getClaims().get("Roles") != null)
                                                {
                                                    Log.d(TAG, "roles = " + task.getResult().getClaims().get("Roles").toString());

                                                    roles = task.getResult().getClaims().get("Roles").toString();
                                                    Log.d(TAG, "roles = " + roles);
                                                    roles = roles.replaceAll("[\\[\\]]", "");

                                                    // Declare HashSet using Set Interface
                                                    Set<String> hashSet = new HashSet<String>();
                                                    hashSet.add(roles);
                                                    Log.d("TAG", "Contents of Set :: " + hashSet);

                                                    String[] myArray = new String[hashSet.size()];
                                                    hashSet.toArray(myArray);

                                                    for (int i = 0; i < myArray.length; i++) {
                                                        //Log.d("TAG","Element at the index "+(i+1)+" is :: "+myArray[i]);
                                                        roles = myArray[i];
                                                        Log.d("TAG", "roles from arr = " + roles);
                                                    }

                                                    idToken = "Bearer " + task.getResult().getToken();
                                                    Log.d(TAG, "forceRefresh true = idToken = " + idToken);

                                                    if (roles.equals("USER")) {
                                                        // if the code is correct and the task is successful
                                                        // we are sending our user to new activity.
                                                        Intent i = new Intent(OtpActivity.this, MainActivity.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_USERNAME, firebaseName);
                                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_EMAIL, firebaseEmail);
                                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_UID, firebaseUid);
                                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, firebasePhoneNumber);
                                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_TOKEN, idToken);

                                                        if (partyId != null) {
                                                            PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.partyId, Integer.parseInt(partyId));
                                                        }

                                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.hasLoggedIn, true);

                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        lovelyProgressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "You are not register as user..!", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } else {
                                                    lovelyProgressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Not Register user", Toast.LENGTH_SHORT).show();

                                                    /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();*/
                                                }
                                            } else {
                                                lovelyProgressDialog.dismiss();
                                                // Handle error -> task.getException();
                                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                                switch (errorCode) {
                                                    case "ERROR_INVALID_CUSTOM_TOKEN":
                                                        Toast.makeText(OtpActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                                        Toast.makeText(OtpActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_INVALID_CREDENTIAL":
                                                        Toast.makeText(OtpActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_INVALID_EMAIL":
                                                        Toast.makeText(OtpActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_WRONG_PASSWORD":
                                                        Toast.makeText(OtpActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_USER_MISMATCH":
                                                        Toast.makeText(OtpActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_REQUIRES_RECENT_LOGIN":
                                                        Toast.makeText(OtpActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                                        Toast.makeText(OtpActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                                        Toast.makeText(OtpActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                                        Toast.makeText(OtpActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_USER_DISABLED":
                                                        Toast.makeText(OtpActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_USER_TOKEN_EXPIRED":

                                                    case "ERROR_INVALID_USER_TOKEN":
                                                        Toast.makeText(OtpActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_USER_NOT_FOUND":
                                                        Toast.makeText(OtpActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                                        Toast.makeText(OtpActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                                        break;

                                                    case "ERROR_WEAK_PASSWORD":
                                                        Toast.makeText(OtpActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                                        break;

                                                }
                                            }
                                        }
                                    });
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            lovelyProgressDialog.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            pinView.setText("");
                            Toast.makeText(OtpActivity.this, "The verification code entered was invalid.", Toast.LENGTH_LONG).show();
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
}