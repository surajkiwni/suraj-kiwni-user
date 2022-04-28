package com.kiwni.app.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.kiwni.app.user.R;

public class RegisterActivity extends AppCompatActivity {

    AppCompatButton btnRegister;
    ImageView imageBack;
    TextInputEditText txtInputPassword;
    boolean isEmailValid = false,isPhoneNoValid = false,isPasswordValid = false,isLastNameValid = false,isFirstNameValid = false;
    EditText edtEmailId ,edtPhoneNo,edtLastName, edtFirstName;

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

        txtInputPassword = findViewById(R.id.txtPasswordInput);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setValidation();

                if(isFirstNameValid && isLastNameValid && isPhoneNoValid && isEmailValid && isPasswordValid ) {

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

            }
        });
    }

    public void setValidation()
    {
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

        //Last name validation
        if (edtLastName.getText().toString().isEmpty()) {
            edtLastName.setError(getResources().getString(R.string.last_name_error));
            //edtLastName.requestFocus();
            isLastNameValid = false;
        }
        else {
            edtLastName.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isLastNameValid = true;
        }

        //First name validation
        if (edtFirstName.getText().toString().isEmpty()) {
            edtFirstName.setError(getResources().getString(R.string.first_name_error));
            //edtLastName.requestFocus();
            isFirstNameValid = false;
        }
        else {
            edtFirstName.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isFirstNameValid = true;
        }
    }
}