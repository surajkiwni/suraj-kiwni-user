package com.kiwni.app.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.classes.LoadingDialog;
import com.kiwni.app.user.global.PermissionRequestConstant;
import com.kiwni.app.user.interfaces.ErrorDialogInterface;
import com.kiwni.app.user.models.bookride.BookRideErrorResp;
import com.kiwni.app.user.models.bookride.ChannelReq;
import com.kiwni.app.user.models.bookride.FromLocationCoordinates;
import com.kiwni.app.user.models.bookride.RateReq;
import com.kiwni.app.user.models.bookride.RideReq;
import com.kiwni.app.user.models.bookride.RideReservationReq;
import com.kiwni.app.user.models.bookride.RideReservationResp;
import com.kiwni.app.user.models.bookride.RideStatusReq;
import com.kiwni.app.user.models.bookride.ServiceTypeReq;
import com.kiwni.app.user.models.bookride.StatusReq;
import com.kiwni.app.user.models.bookride.ToLocationCoordinates;
import com.kiwni.app.user.models.socket.SocketReservationResp;
import com.kiwni.app.user.models.vehicle_details.ScheduleMapResp;
import com.kiwni.app.user.network.ApiClient;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;


import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingActivity extends AppCompatActivity implements ErrorDialogInterface,ConnectivityHelper.NetworkStateReceiverListener
{
    RadioButton radioBusiness, radioPersonal;
    AppCompatButton btnConfirmBooking;
    String TAG = this.getClass().getSimpleName();
    ApiInterface apiInterface;
    CheckBox chkPhone, chkEmail, chkWhatsApp;
    RadioGroup radioGroup;
    ErrorDialogInterface errorDialogInterface;

    List<ScheduleMapResp> selectedVehicleDataList = new ArrayList<>();

    ConstraintLayout constraintBusinessInput;
    ImageView imgBack,imgCall, imgVehicleImg;
    TextView txtTitle, txtStartTime, txtStartEndDate, txtEstimatedKm, txtTitleType, txtDropAddress,
            txtPickupAddress, txtProviderName, txtVehicleName, txtVehicleClassType, txtRideFare,
            txtExtraFareKm, txtExtraFarePerKm, txtTotalFareBase, txtApplyCoupon, txtGst,
            txtTotalFare, txtThirtyPercDiscount, txtFiftyPercDiscount, txtHundredPercDiscount;

    /* business dialog widgets */
    EditText edtCompanyName, edtCompanyEmail, edtCompanyPhoneNo;
    TextView txtCompanyPhone, txtCompanyEmail, txtCompanyName;
    AppCompatButton doneButton;
    boolean isCompanyNameValid = false, isCompanyEmailValid = false, isCompanyPhoneNoValid = false,
            isNetworkAvailable = false;

    String direction = "", startDate = "", endDate = "", startTime = "", serviceType = "",
            distanceInKm = "", mobile = "", pickupAddress = "", dropAddress = "",
            bCompanyName = "", bCompanyEmail = "", bComapnyPhone = "", pickup_city = "",
            drop_city = "", journeyEndTime = "", journeyTime = "", customerName = "",
            customerPhone = "", customerEmail = "", idToken = "", driverName = "",
            driverLicense = "", driverPhone = "", firstName = "", lastName = "", providerId = "",
            providerName = "", createdDateForApi = "", vehicle_no = "", vehicle_price = "",
            refreshToken = "", companyName = "", companyEmail = "",
            companyPhone = "", tripType = "", notificationType = "", pickupLocation = "",
            dropLocation = "", notification_type = "";
    int partyId = 0, service_type_id = 0, vehicleId = 0, driverId = 0;
    double pickupLocationLat = 0.0, pickupLocationLng = 0.0, dropLocationLat = 0.0, dropLocationLng = 0.0;
    Long scheduleId;

    ChannelReq channelReq;
    RideReq rideReq;
    List<RateReq> rateReq = new ArrayList<>();
    ServiceTypeReq serviceTypeReq;
    StatusReq statusReq;

    /* socket */
    Socket mSocket;
    SocketReservationResp reservationResp = new SocketReservationResp();
    List<SocketReservationResp> reservationRespList = new ArrayList<>();
    Dialog dialogPayment, dialogThankYou;

    private ConnectivityHelper connectivityHelper;
    LoadingDialog loadingDialog;

    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        startNetworkBroadcastReceiver(this);

        radioGroup = findViewById(R.id.radioGroup);
        radioBusiness = findViewById(R.id.radioBusiness);
        radioPersonal = findViewById(R.id.radioPersonal);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        constraintBusinessInput = findViewById(R.id.constraintBusinessInput);
        imgBack = findViewById(R.id.imgBack);
        imgCall = findViewById(R.id.imgCall);
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
        chkEmail = findViewById(R.id.chkEmail);
        chkPhone = findViewById(R.id.chkPhone);
        chkWhatsApp = findViewById(R.id.chkWhatsApp);
        txtThirtyPercDiscount = findViewById(R.id.txtThirtyPercDiscount);
        txtFiftyPercDiscount = findViewById(R.id.txtFiftyPercDiscount);
        txtHundredPercDiscount = findViewById(R.id.txtHundredPercDiscount);

        errorDialogInterface = this::onClick;

        imgCall.setVisibility(View.VISIBLE);

        loadingDialog = new LoadingDialog(this);

        Gson gson = new Gson();
        String stringData = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SELECTED_VEHICLE_OBJECT, "");
        Log.d(TAG, "string data = " + stringData);

        if(stringData != null)
        {
            Type type = new TypeToken<List<ScheduleMapResp>>() {
            }.getType();
            selectedVehicleDataList = gson.fromJson(stringData, type);
            Log.d(TAG, "selectedVehicleDataList size = " + selectedVehicleDataList.size());
        }

        /* get data from pref */
        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");
        pickup_city = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        drop_city = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");
        idToken = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_TOKEN, "");
        customerName = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_USERNAME, "");
        customerEmail = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_EMAIL, "");
        customerPhone = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");
        journeyEndTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_TIME_FOR_API, "");
        journeyTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_FOR_API, "");

        partyId = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.partyId, 0);
        if(partyId != 0)
        {
            Log.d(TAG, "partyId = " + partyId);
        }

        if(selectedVehicleDataList.get(0).getDriverId() != null)
        {
            driverId = selectedVehicleDataList.get(0).getDriverId();
            Log.d(TAG, "driverId = " + driverId);
        }
        else
        {
            driverId = 0;
        }

        providerId = selectedVehicleDataList.get(0).getVehicle().getProvider().getId();
        if(!providerId.equals(""))
        {
            Log.d(TAG, "providerId = " + providerId);
        }

        distanceInKm = distanceInKm.replaceAll("[^0-9]", "").trim();

        vehicle_price = String.valueOf(selectedVehicleDataList.get(0).getPrice());
        vehicle_no = selectedVehicleDataList.get(0).getVehicle().getRegNo();
        scheduleId = selectedVehicleDataList.get(0).getVehicle().getId();
        vehicleId = selectedVehicleDataList.get(0).getVehicleId();
        providerName = selectedVehicleDataList.get(0).getVehicle().getProvider().getName();

        driverName = String.valueOf(selectedVehicleDataList.get(0).getDriverName());
        if(driverName.equals("null"))
        {
            driverName = "";
        }
        driverLicense = "";
        driverPhone = String.valueOf(selectedVehicleDataList.get(0).getDriverMobileNo());
        if(driverPhone.equals("null"))
        {
            driverPhone = "";
        }
        pickupAddress = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_ADDRESS, "");
        dropAddress = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_ADDRESS, "");

        Log.d(TAG,"data from previous screen - " + direction + " , " + serviceType);

        /* pickup and drop location latlng */
        pickupLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_LOCATION, "");
        dropLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_LOCATION, "");
        Log.d(TAG,"pickupLocation and dropLocation - " + pickupLocation + " , " + dropLocation);

        GetSeparatedCoordinatesFromPickupString(pickupLocation);
        GetSeparatedCoordinatesFromDropString(dropLocation);

        /* set data on ui */
        txtTitle.setText("Confirm Booking");
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm + " km");
        txtTitleType.setText(serviceType + " ( " + direction + " ) ");
        txtPickupAddress.setText(pickupAddress);
        txtDropAddress.setText(dropAddress);

        txtVehicleName.setText(selectedVehicleDataList.get(0).getVehicle().getModel());
        txtProviderName.setText(selectedVehicleDataList.get(0).getVehicle().getProvider().getName());
        txtVehicleClassType.setText(selectedVehicleDataList.get(0).getVehicle().getClassType());

        /* calculate gst (5%) fro ride fare and set to total fare */
        double rideFare = Double.parseDouble(String.valueOf(selectedVehicleDataList.get(0).getPrice()));
        double calculatedGst = (rideFare / 100.0f) * 5;
        txtGst.setText("" + Math.round(calculatedGst));

        txtApplyCoupon.setText("" + 0);
        txtRideFare.setText(Math.round(selectedVehicleDataList.get(0).getPrice()) + "/-");
        double totalFare = calculatedGst + rideFare;
        txtTotalFare.setText(Math.round(totalFare) + "/-");

        /* 30% advance */
        double calculateThirtyAdv = (totalFare / 100.0f) * 30;
        Log.d(TAG, "calculateThirtyAdv = " + calculateThirtyAdv);
        txtThirtyPercDiscount.setText(Math.round(calculateThirtyAdv) + "/-");

        /* 50% advance */
        double calculateFiftyAdv = (totalFare / 100.0f) * 50;
        Log.d(TAG, "calculateFiftyAdv = " + calculateFiftyAdv);
        txtFiftyPercDiscount.setText(Math.round(calculateFiftyAdv) + "/-");

        /* 100% advance */
        txtHundredPercDiscount.setText(Math.round(totalFare) + "/-");

        if(!selectedVehicleDataList.get(0).getVehicle().getImagePath().equals(""))
        {
            Glide.with(getApplicationContext())
                    .load(AppConstants.IMAGE_PATH + selectedVehicleDataList.get(0).getVehicle().getImagePath())
                    .into(imgVehicleImg);
        }

        /* default radio button value */
        if(radioPersonal.isChecked())
            tripType = radioPersonal.getText().toString();

        /* checkbox functionality */
        CheckboxSelectedData();

        chkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckboxSelectedData();
            }
        });

        chkPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckboxSelectedData();
            }
        });

        chkWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckboxSelectedData();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {
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
                            PermissionRequestConstant.MY_PERMISSIONS_REQUEST_CALL_PHONE);

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

        /* get rates from rates array */
        direction = direction.replace(" ", "-");
        String contactStr = direction + "-" + serviceType.toLowerCase();
        Log.d(TAG, "contactStr = " + contactStr);

        String concatClassType = contactStr + "-" + selectedVehicleDataList.get(0).getVehicle().getClassType().toLowerCase();
        Log.d(TAG, "concatClassType = " + concatClassType);

        /* set value to service_type id */
        switch (concatClassType)
        {
            case "one-way-outstation-premium":
                service_type_id = 1;
                break;
            case "one-way-outstation-luxury":
                service_type_id = 2;
                break;
            case "one-way-outstation-ultra-luxury":
                service_type_id = 3;
                break;
            case "two-way-outstation-premium":
                service_type_id = 4;
                break;
            case "two-way-outstation-luxury":
                service_type_id = 5;
                break;
            case "two-way-outstation-ultra-luxury":
                service_type_id = 6;
                break;
            default:
                Log.d(TAG, "does not match concatenated serviceType.");
                break;
        }

        /* separate Name from string */
        GetNameFromString(customerName);

        /* get current time */
        getCurrentTimeToSendApi();

        /* call socket */
        SocketConnect();

        /* payload for create reservation api */
        /* set static data and send to api call*/
        channelReq = new ChannelReq();
        channelReq.setId(1);

        rideReq = new RideReq();
        rideReq.setCreatedTime("");
        rideReq.setCreatedUser(customerName);
        rideReq.setDistance(distanceInKm);
        rideReq.setFromLocation(pickup_city);
        rideReq.setJourneyEndTime(journeyEndTime);
        rideReq.setJourneyTime(journeyTime);

        //rateReq.add(new RateReq(1));
        rateReq.add(new RateReq(1));
        rideReq.setRates(rateReq);

        rideReq.setStatus(new RideStatusReq(2));

        rideReq.setToLocation(drop_city);

        rideReq.setFromLocationCoordinates(new FromLocationCoordinates(pickupLocationLat, pickupLocationLng));
        rideReq.setToLocationCoordinates(new ToLocationCoordinates(dropLocationLat, dropLocationLng));

        rideReq.setUpdatedTime("");
        rideReq.setUpdatedUser("");

        serviceTypeReq = new ServiceTypeReq();
        serviceTypeReq.setId(service_type_id);

        statusReq = new StatusReq();
        statusReq.setId(1);

        btnConfirmBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(TAG, "idToken = " + idToken);

                /* create reservation api call */
                if(tripType.equals(radioBusiness.getText().toString()))
                {
                    companyName = txtCompanyName.getText().toString();
                    companyEmail = txtCompanyEmail.getText().toString();
                    companyPhone = txtCompanyPhone.getText().toString();
                }
                else
                {
                    companyName = "";
                    companyPhone = "";
                    companyEmail = "";
                }

                if(ConnectivityHelper.isConnected)
                {
                    BookRide(channelReq, "", firstName, customerEmail, partyId,
                            customerName, customerPhone, driverId, driverLicense, driverName,
                            driverPhone, Integer.parseInt(providerId), providerName,
                            createdDateForApi, rideReq, scheduleId, serviceTypeReq, statusReq,
                            "", "", vehicleId, tripType, notification_type,
                            companyEmail, companyPhone, companyName, idToken);
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
    }

    /* radio button functionality*/
    @SuppressLint("NonConstantResourceId")
    public void onRadioClick(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId())
        {
            case R.id.radioPersonal:
                if (checked)
                    tripType = radioPersonal.getText().toString();
                    constraintBusinessInput.setVisibility(View.GONE);
                break;
            case R.id.radioBusiness:
                if (checked)
                    tripType = radioBusiness.getText().toString();

                Dialog dialog = new Dialog(ConfirmBookingActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.business_view);

                edtCompanyName = dialog.findViewById(R.id.edtCompanyName);
                edtCompanyEmail = dialog.findViewById(R.id.edtCompanyEmail);
                edtCompanyPhoneNo = dialog.findViewById(R.id.edtCompanyPhoneNo);
                doneButton = dialog.findViewById(R.id.doneButton);

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


                            dialog.dismiss();
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

                dialog.show();
                break;
        }
    }

    /* validation for business details */
    public void setValidation()
    {
        //Email validation
        if (edtCompanyEmail.getText().toString().isEmpty()) {
            edtCompanyEmail.setError(Html.fromHtml("<font color='black'>Please enter valid email address</font>"));
            isCompanyEmailValid = false;
        } else if (!edtCompanyEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            edtCompanyEmail.setError(Html.fromHtml("<font color='black'>Invalid email address</font>"));
            isCompanyEmailValid = false;
        } else {
            edtCompanyEmail.setError(null);
            isCompanyEmailValid = true;
        }

        //Phone no validation
        if (edtCompanyPhoneNo.getText().toString().isEmpty())
        {
            edtCompanyPhoneNo.setError(Html.fromHtml("<font color='black'>Please enter valid phone no</font>"));
            isCompanyPhoneNoValid = false;
        } else if (edtCompanyPhoneNo.getText().toString().length() > 10) {
            edtCompanyPhoneNo.setError(Html.fromHtml("<font color='black'>Enter 10 digit mobile no</font>"));
            isCompanyPhoneNoValid = false;
        } else {
            edtCompanyPhoneNo.setError(null);
            isCompanyPhoneNoValid = true;
        }

        //Last name validation
        if (edtCompanyName.getText().toString().isEmpty()) {
            edtCompanyName.setError(Html.fromHtml("<font color='black'>Please enter valid Name</font>"));
            isCompanyNameValid = false;
        }
        else {
            edtCompanyName.setError(null);
            isCompanyNameValid = true;
        }
    }

    public void GetNameFromString(String username)
    {
        username = username.trim();
        String[] newStr = username.split("\\s+");
        for (int i = 0; i < newStr.length; i++) {
            System.out.println(newStr[i]);
        }

        firstName = newStr[0];
        lastName = newStr[2];
    }

    /* get current date time */
    @SuppressLint("SimpleDateFormat")
    public void getCurrentTimeToSendApi()
    {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //Date date = Calendar.getInstance().getTime();
        createdDateForApi = inputDateFormat.format(Calendar.getInstance().getTime());
        System.out.println("createdDateForApi = " + createdDateForApi);
    }

    /* split pickup location coordinates from string */
    public void GetSeparatedCoordinatesFromPickupString(String coordinates)
    {
        String[] str = coordinates.split(" ");
        for (String locations : str)
        {
            pickupLocationLat = Double.parseDouble(str[0]);
            pickupLocationLng = Double.parseDouble(str[1]);
        }

        Log.d(TAG, "coordinates = " + pickupLocationLat + ", " + pickupLocationLng);
    }

    /* split drop location coordinates from string */
    public void GetSeparatedCoordinatesFromDropString(String coordinates)
    {
        String[] str = coordinates.split(" ");
        for (String locations : str)
        {
            dropLocationLat = Double.parseDouble(str[0]);
            dropLocationLng = Double.parseDouble(str[1]);
        }

        Log.d(TAG, "coordinates = " + dropLocationLat + ", " + dropLocationLng);
    }

    /* Checkbox selection */
    public void CheckboxSelectedData()
    {
        StringBuilder result = new StringBuilder();
        if(chkEmail.isChecked())
        {
            notificationType = "Email";
            result.append(notificationType);
        }
        if(chkPhone.isChecked())
        {
            notificationType = "SMS";
            result.append(" ").append(notificationType);
        }
        if(chkWhatsApp.isChecked())
        {
            notificationType = "WhatsApp";
            result.append(" ").append(notificationType);
        }
        //Log.d(TAG, "result = " + result.toString().trim());
        notification_type = result.toString().trim();
        notification_type = notification_type.replace(" ",",");
        //Log.d(TAG, "type = " + notification_type);
    }

    /* create reservation api call method*/
    public void BookRide(ChannelReq channel, String createdTime, String createdUser,
                         String customerEmail, Integer customerId, String customerName,
                         String customerPhone, Integer driverId, String driverLicense,
                         String driverName, String driverPhone, Integer providerId,
                         String providerName, String reservationTime, RideReq ride,
                         Long scheduleId, ServiceTypeReq serviceType, StatusReq status,
                         String updatedTime, String updatedUser, Integer vehicleId, String tripType,
                         String notificationType, String companyName, String companyEmail,
                         String companyPhone, String idToken)
    {

        apiInterface = ApiClient.getClient(AppConstants.BASE_URL).create(ApiInterface.class);

        double estimatedPrice = Math.round(Double.parseDouble(vehicle_price));
        RideReservationReq reservationReq = new RideReservationReq(channel, createdTime,
                createdUser, customerEmail, customerId, customerName, customerPhone, driverId,
                driverLicense, driverName, driverPhone, providerId, providerName, reservationTime,
                ride, scheduleId, serviceType, status, updatedTime, updatedUser, vehicleId,
                vehicle_no, estimatedPrice, notificationType, tripType,
                companyName, companyEmail, companyPhone);

        Log.d(TAG, "req = " + reservationReq);

        Call<RideReservationResp> call = apiInterface.createRide(reservationReq, idToken);

        // Set up progress before call
        loadingDialog.showLoadingDialog("Your request is processing");

        call.enqueue(new Callback<RideReservationResp>() {
            @Override
            public void onResponse(Call<RideReservationResp> call, Response<RideReservationResp> response) {
                loadingDialog.hideDialog();
                int statusCode = response.code();
                Log.d(TAG, "statusCode: " + statusCode);
                Log.d(TAG, "Response = " + response);

                if(statusCode == 201)
                {
                    //reservationRespList = response.body();
                    if(response.body().equals(null))
                    {
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        /*payment dialog */
                        dialogPayment = new Dialog(ConfirmBookingActivity.this, android.R.style.Theme_Light);
                        dialogPayment.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogPayment.setContentView(R.layout.dialog_payment_screen);

                        final AppCompatButton btnPay = dialogPayment.findViewById(R.id.btnPay);

                        btnPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Log.d(TAG, "click");
                                DisplayReservationRespDialog(reservationRespList);
                            }
                        });

                        dialogPayment.show();
                    }
                }
                else if(statusCode == 401)
                {
                   // Toast.makeText(getApplicationContext(), "Due to poor network, your session has expired. Please wait... ", Toast.LENGTH_LONG).show();

                    Toast.makeText(getApplicationContext(), "Please wait your request is processing...", Toast.LENGTH_LONG).show();

                    FirebaseAuth.getInstance().getCurrentUser()
                            .getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        refreshToken = "Bearer " + task.getResult().getToken();
                                        Log.d(TAG,"forceRefresh true = refreshToken = " + refreshToken);

                                        //new generated token set to pref data
                                        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.FIREBASE_TOKEN, refreshToken);

                                        if(ConnectivityHelper.isConnected)
                                        {
                                            BookRide(channelReq, "", firstName, customerEmail, partyId,
                                                    customerName, customerPhone, driverId, driverLicense, driverName,
                                                    driverPhone, providerId, providerName,
                                                    createdDateForApi, rideReq, scheduleId, serviceTypeReq, statusReq,
                                                    "", "", vehicleId, tripType, notification_type,
                                                    companyEmail, companyPhone, companyName, refreshToken);
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
                                }
                            });
                }
                else
                {
                    Gson gson = new GsonBuilder().create();
                    BookRideErrorResp mError = new BookRideErrorResp();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), BookRideErrorResp.class);
                        if(!mError.getError().equals(""))
                        {
                            Toast.makeText(getApplicationContext(), mError.getError(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        e.printStackTrace();
                    }
                    //Toast.makeText(getActivity(), "Error code = " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RideReservationResp> call, Throwable t) {
                loadingDialog.hideDialog();
                Log.d(TAG, "error: " + t);
            }
        });
    }

    /* socket reservation message */
    public void SocketConnect()
    {
        //Socket Listen
        try {
            mSocket = IO.socket(AppConstants.SOCKET_BASE_URL);
            mSocket.connect();

            mSocket.on(Socket.EVENT_CONNECT,onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);

            EmitData();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /* emit party id for join in web socket */
    public void EmitData()
    {
        //{"partyId": 274}
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("partyId", partyId);
            mSocket.emit("join", obj);
            Log.d("join", obj.toString());

            ListenReservationMessage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("IsConnected", String.valueOf(mSocket.connected()));
            Log.d(TAG, "connected to the server");
        }
    };

    private final Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "connection error");
        }
    };

    private final Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "disconnected from the server");
        }
    };

    public void ListenReservationMessage()
    {
        mSocket.on(AppConstants.WEBSOCKET_RESERVATION_EVENT, onNewMessage);
    }

    /* reservation success web socket response */
    private final Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    Log.d(TAG, "in new msg");
                    Log.d(TAG, "data = " + Arrays.toString(args));

                    if (args.length == 0)
                    {
                        //Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = (JSONObject) args[0];

                            Gson gson = new Gson();
                            reservationResp = gson.fromJson(jsonObject.toString(), SocketReservationResp.class);

                            Log.d(TAG, "reservationResp = " + reservationResp.toString());
                            Log.d(TAG, "reservationResp id = " + reservationResp.getId());

                            reservationRespList.clear();
                            reservationRespList.add(reservationResp);
                            Log.d(TAG, "reservationRespList = " + reservationRespList.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    @SuppressLint("SetTextI18n")
    public void DisplayReservationRespDialog(List<SocketReservationResp> reservationRespList)
    {
        Log.d(TAG, "list size in dialog = " + reservationRespList.size());

        dialogThankYou = new Dialog(ConfirmBookingActivity.this, android.R.style.Theme_Light);
        dialogThankYou.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogThankYou.setContentView(R.layout.dialog_thank_you_screen);

        AppCompatButton btnOk = dialogThankYou.findViewById(R.id.btnOk);
        TextView txtKrnNo = dialogThankYou.findViewById(R.id.txtKrnNo);
        TextView txtBookingNo = dialogThankYou.findViewById(R.id.txtBookingNo);

        if(reservationRespList.size() == 0)
        {
            txtKrnNo.setText("-");
            txtBookingNo.setText("-");
        }
        else
        {
            txtKrnNo.setText("" + reservationRespList.get(0).getReservationId());
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /* send list to main activity for display data in dialog */
                Gson gson = new Gson();
                Type type = new TypeToken<List<SocketReservationResp>>() {}.getType();
                String jsonForData = gson.toJson(reservationRespList, type);

                Log.d(TAG, "jsonForData = " + jsonForData);

                Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(SharedPref.SOCKET_RESP_OBJECT, jsonForData);

                dialogPayment.dismiss();
                dialogThankYou.dismiss();

                startActivity(intent);
                finish();
            }
        });

        dialogThankYou.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionRequestConstant
                    .MY_PERMISSIONS_REQUEST_CALL_PHONE: {
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

    @Override
    public void networkUnavailable() {
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
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkBroadcastReceiver(this);
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        mSocket.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterNetworkBroadcastReceiver(this);
    }

    @Override
    public void onClick(Context context) {
        finish();
    }
}

