package com.kiwni.app.user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.models.ScheduleMapResp;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfirmBookingActivity extends AppCompatActivity
{
    AppCompatRadioButton radioBusiness, radioPersonal;
    AppCompatButton confirmButton;
    String TAG = this.getClass().getSimpleName();

    List<ScheduleMapResp> selectedVehicleDataList = new ArrayList<>();

    ConstraintLayout constraintBusinessInput;
    ImageView imageBack,imgCallConfirmAct, imgVehicleImg;
    TextView txtTitle, txtStartTime, txtStartEndDate, txtEstimatedKm, txtTitleType, txtDropAddress,
            txtPickupAddress, txtProviderName, txtVehicleName, txtVehicleClassType, txtRideFare,
            txtExtraFareKm, txtExtraFarePerKm, txtTotalFareBase, txtApplyCoupon, txtGst, txtTotalFare;

    /* business dialog widgets */
    EditText edtCompanyName, edtCompanyEmail, edtCompanyPhoneNo;
    TextView txtCompanyPhone, txtCompanyEmail, txtCompanyName;
    AppCompatButton doneButton;
    boolean isCompanyNameValid = false, isCompanyEmailValid = false, isCompanyPhoneNoValid = false;

    String direction = "", startDate = "", endDate = "", startTime = "", serviceType = "",
            distanceInKm = "", mobile = "", pickupAddress = "", dropAddress = "",
            bCompanyName = "", bCompanyEmail = "", bComapnyPhone = "";

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        radioBusiness = findViewById(R.id.radioBusiness);
        radioPersonal = findViewById(R.id.radioPersonal);
        confirmButton = findViewById(R.id.confirmButton);
        constraintBusinessInput = findViewById(R.id.constraintBusinessInput);
        imageBack = findViewById(R.id.imageBack);
        imgCallConfirmAct = findViewById(R.id.imgCallConfirmAct);
        txtTitle = findViewById(R.id.txtTitle);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);
        txtTitleType = findViewById(R.id.txtTitleType);
        txtPickupAddress = findViewById(R.id.txtPickupAddress);
        txtDropAddress = findViewById(R.id.txtDropAddress);
        txtVehicleName = findViewById(R.id.txtVehicleName);
        txtProviderName = findViewById(R.id.txtProviderName);
        txtVehicleClassType = findViewById(R.id.txtVehicleClassType);
        imgVehicleImg = findViewById(R.id.imgVehicleImg);
        txtExtraFareKm = findViewById(R.id.txtExtraFareKm);
        txtExtraFarePerKm = findViewById(R.id.txtExtraFarePerKm);
        txtTotalFareBase = findViewById(R.id.txtTotalFareBase);
        txtTotalFare = findViewById(R.id.txtTotalFare);
        txtRideFare = findViewById(R.id.txtRideFare);
        txtApplyCoupon = findViewById(R.id.txtApplyCoupon);
        txtGst = findViewById(R.id.txtGst);
        txtCompanyEmail = findViewById(R.id.txtCompanyEmail);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyPhone = findViewById(R.id.txtCompanyPhone);

        Gson gson = new Gson();
        String stringData = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SELECTED_VEHICLE_OBJECT, "");
        //String stringData = getIntent().getStringExtra(SharedPref.SELECTED_VEHICLE_OBJECT);
        Log.d(TAG, "string data = " + stringData);

        if(stringData != null)
        {
            Type type = new TypeToken<List<ScheduleMapResp>>() {
            }.getType();
            selectedVehicleDataList = gson.fromJson(stringData, type);
            Log.d("TAG", "selectedVehicleDataList size = " + selectedVehicleDataList.size());
        }

        //pref data
        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");
        pickupAddress = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_ADDRESS, "");
        dropAddress = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_ADDRESS, "");

        Log.d("TAG","data from previous screen - " + direction + " , " + serviceType);

        /* set data on ui */
        txtTitle.setText("Confirm Booking");
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);
        txtTitleType.setText(serviceType + " ( " + direction + " ) ");

        txtPickupAddress.setText(pickupAddress);
        txtDropAddress.setText(dropAddress);

        txtVehicleName.setText(selectedVehicleDataList.get(0).getVehicle().getModel());
        txtProviderName.setText(selectedVehicleDataList.get(0).getVehicle().getProvider().getName());
        txtVehicleClassType.setText(selectedVehicleDataList.get(0).getVehicle().getClassType());
        txtRideFare.setText(selectedVehicleDataList.get(0).getPrice() + " /-");

        if(!selectedVehicleDataList.get(0).getVehicle().getImagePath().equals(""))
        {
            Glide.with(getApplicationContext())
                    .load(AppConstants.IMAGE_PATH + selectedVehicleDataList.get(0).getVehicle().getImagePath())
                    .into(imgVehicleImg);
        }

        radioPersonal.setChecked(true);

        radioPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                radioBusiness.setChecked(false);
                constraintBusinessInput.setVisibility(View.GONE);
            }
        });

        radioBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                radioPersonal.setChecked(false);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.business_view, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                edtCompanyName = (EditText) dialogView.findViewById(R.id.edtCompanyName);
                edtCompanyEmail = (EditText) dialogView.findViewById(R.id.edtCompanyEmail);
                edtCompanyPhoneNo = (EditText) dialogView.findViewById(R.id.edtCompanyPhoneNo);
                doneButton = (AppCompatButton) dialogView.findViewById(R.id.doneButton);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        setValidation();

                        if(isCompanyNameValid && isCompanyPhoneNoValid && isCompanyEmailValid)
                        {
                            bCompanyName = edtCompanyName.getText().toString();
                            bCompanyEmail = edtCompanyEmail.getText().toString();
                            bComapnyPhone = edtCompanyPhoneNo.getText().toString();

                            PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.BUSINESS_COMPANY_NAME, bCompanyName);
                            PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.BUSINESS_COMPANY_EMAIL, bCompanyEmail);
                            PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.BUSINESS_COMPANY_PHONE, bComapnyPhone);

                            b.dismiss();
                            constraintBusinessInput.setVisibility(View.VISIBLE);

                            txtCompanyName.setText(bCompanyName);
                            txtCompanyEmail.setText(bCompanyEmail);
                            txtCompanyPhone.setText(bComapnyPhone);
                        }
                        else
                        {
                            Log.d(TAG, "Invalid data.!");
                        }
                    }
                });

                b.show();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ConfirmBookingActivity.this, BookingDetailsActivity.class));
                finish();
            }
        });

        imgCallConfirmAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = "7057052508";
                Uri call = Uri.parse("tel:" + mobile);
                Intent intent = new Intent(Intent.ACTION_CALL, call);

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(ConfirmBookingActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ConfirmBookingActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(intent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_screen, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                AppCompatButton btnPay = dialogView.findViewById(R.id.btnPay);

                btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);

                        LayoutInflater inflater = LayoutInflater.from(ConfirmBookingActivity.this);
                        View dialogView = inflater.inflate(R.layout.thank_you_screen, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setCancelable(false);
                        AlertDialog b1 = dialogBuilder.create();

                        Button okButton = dialogView.findViewById(R.id.okButton);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                        b1.show();
                    }
                });

                b.show();
            }
        });
    }

    /* validation for business details */
    public void setValidation()
    {
        //Email validation
        if (edtCompanyEmail.getText().toString().isEmpty()) {
            edtCompanyEmail.setError(getResources().getString(R.string.email_error));
            //edtEmailId.requestFocus();
            isCompanyEmailValid = false;
        } else if (!edtCompanyEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            edtCompanyEmail.setError("Invalid email address");
            //edtEmailId.requestFocus();
            isCompanyEmailValid = false;
        } else {
            edtCompanyEmail.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isCompanyEmailValid = true;
        }

        //Phone no validation
        if (edtCompanyPhoneNo.getText().toString().isEmpty())
        {
            edtCompanyPhoneNo.setError(getResources().getString(R.string.phoneno_error));
            //edtPhoneNo.requestFocus();
            isCompanyPhoneNoValid = false;
        } else if (edtCompanyPhoneNo.getText().toString().length() > 10) {
            edtCompanyPhoneNo.setError("Enter 10 digit mobile no");
            //edtPhoneNo.requestFocus();
            isCompanyPhoneNoValid = false;
        } else {
            edtCompanyPhoneNo.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isCompanyPhoneNoValid = true;
        }

        //Last name validation
        if (edtCompanyName.getText().toString().isEmpty()) {
            edtCompanyName.setError(getResources().getString(R.string.first_name_error));
            //edtLastName.requestFocus();
            isCompanyNameValid = false;
        }
        else {
            edtCompanyName.setError(null);
            //edtEmailId.setErrorEnabled(false);
            isCompanyNameValid = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ConfirmBookingActivity.this, BookingDetailsActivity.class));
        finish();
    }
}

