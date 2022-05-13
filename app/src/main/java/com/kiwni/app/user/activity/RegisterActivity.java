package com.kiwni.app.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.ImageFilterButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.kiwni.app.user.R;

public class RegisterActivity extends AppCompatActivity {
    AppCompatButton btnRegister;
    boolean isEmailValid = false, isPhoneNoValid = false, isPasswordValid = false, isLastNameValid = false, isFirstNameValid = false;
    EditText edtEmailId, edtPhoneNo, edtLastName, edtFirstName, edtPassword;
    ImageView imgEyeOpen, imgEyeClose, imgBack;
    TextView txtTitle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);
        edtEmailId = findViewById(R.id.edtEmail);
        edtPhoneNo = findViewById(R.id.edtMobile);
        edtLastName = findViewById(R.id.edtLastName);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtPassword = findViewById(R.id.edtPassword);
        imgEyeClose = findViewById(R.id.imgEyeClose);
        imgEyeOpen = findViewById(R.id.imgEyeOpen);

        txtTitle.setText("SIGN UP");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setValidation();

                if (isFirstNameValid && isLastNameValid && isPhoneNoValid && isEmailValid && isPasswordValid) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        imgEyeOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgEyeClose.setVisibility(View.VISIBLE);
                imgEyeOpen.setVisibility(View.GONE);
                //for textview show
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        imgEyeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgEyeClose.setVisibility(View.GONE);
                imgEyeOpen.setVisibility(View.VISIBLE);
                //for textview hide
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
    }

    public void setValidation() {
        //Email validation
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

        //Phone no validation
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

        //password validation
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError(getResources().getString(R.string.password_error));
            //edtPassword.requestFocus();
            isPasswordValid = false;
        } else if (edtPassword.getText().toString().length() < 8) {
            edtPassword.setError("Enter valid password.");
            //edtPassword.requestFocus();
            isPasswordValid = false;
        } else {
            edtPassword.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isPasswordValid = true;
        }

        //Last name validation
        if (edtLastName.getText().toString().isEmpty()) {
            edtLastName.setError(getResources().getString(R.string.last_name_error));
            //edtLastName.requestFocus();
            isLastNameValid = false;
        } else {
            edtLastName.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isLastNameValid = true;
        }

        //First name validation
        if (edtFirstName.getText().toString().isEmpty()) {
            edtFirstName.setError(getResources().getString(R.string.first_name_error));
            //edtLastName.requestFocus();
            isFirstNameValid = false;
        } else {
            edtFirstName.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isFirstNameValid = true;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}