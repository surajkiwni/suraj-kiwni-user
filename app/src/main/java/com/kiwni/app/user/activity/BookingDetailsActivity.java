package com.kiwni.app.user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.BookingAdapter;
import com.kiwni.app.user.adapter.NestedAdapter;
import com.kiwni.app.user.adapter.TitleItemAdapter;
import com.kiwni.app.user.datamodels.BookingModel2;
import com.kiwni.app.user.models.ScheduleMapResp;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookingDetailsActivity extends AppCompatActivity
{
    RecyclerView recyclerView1 ,recyclerView2;
    List<BookingModel2> bookingModelList;
    List<BookingModel2> bookingModelList1;
    List<ScheduleMapResp> selectedVehicleDataList = new ArrayList<>();

    ImageView imageBack,imgCallBookingAct, imgVehicleImg;
    String TAG = this.getClass().getSimpleName();
    AppCompatButton proceedButton;
    TextView txtTitle, txtStartTime, txtStartEndDate, txtEstimatedKm, txtTitleType, txtDropAddress,
            txtPickupAddress, txtProviderName, txtVehicleName, txtVehicleClassType;

    String direction = "", startDate = "", endDate = "", startTime = "", serviceType = "",
            distanceInKm = "", mobile = "", pickupAddress = "", dropAddress = "";

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        imageBack =findViewById(R.id.imageBack);
        imgVehicleImg =findViewById(R.id.imgVehicleImg);
        imgCallBookingAct = findViewById(R.id.imgCallBookingAct);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        txtTitle = findViewById(R.id.txtTitle);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);
        txtTitleType = findViewById(R.id.txtTitleType);
        proceedButton =findViewById(R.id.proceedButton);
        txtPickupAddress =findViewById(R.id.txtPickupAddress);
        txtDropAddress =findViewById(R.id.txtDropAddress);
        txtVehicleName =findViewById(R.id.txtVehicleName);
        txtProviderName =findViewById(R.id.txtProviderName);
        txtVehicleClassType =findViewById(R.id.txtVehicleClassType);

        Gson gson = new Gson();
        String stringData = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SELECTED_VEHICLE_OBJECT, "");
        //String stringData = getIntent().getStringExtra(SharedPref.SELECTED_VEHICLE_OBJECT);
        Log.d(TAG, "string data = " + stringData);

        if(stringData != null)
        {
            Type type = new TypeToken<List<ScheduleMapResp>>() {
            }.getType();
            selectedVehicleDataList = gson.fromJson(stringData, type);
            Log.d("TAG", "scheduleMapRespList size = " + selectedVehicleDataList.size());
        }

        /* get pref data and stored to variables*/
        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");
        pickupAddress = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_ADDRESS, "");
        dropAddress = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_ADDRESS, "");

        Log.d("TAG","data from previous screen - " + direction + " , " + serviceType);

        /* set data to ui */
        txtTitle.setText("Booking Details");
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);
        txtTitleType.setText(serviceType + " ( " + direction + " ) ");
        txtPickupAddress.setText(pickupAddress);
        txtDropAddress.setText(dropAddress);

        txtVehicleName.setText(selectedVehicleDataList.get(0).getVehicle().getModel());
        txtProviderName.setText(selectedVehicleDataList.get(0).getVehicle().getProvider().getName());
        txtVehicleClassType.setText(selectedVehicleDataList.get(0).getVehicle().getClassType());

        if(!selectedVehicleDataList.get(0).getVehicle().getImagePath().equals(""))
        {
            Glide.with(getApplicationContext())
                    .load(AppConstants.IMAGE_PATH + selectedVehicleDataList.get(0).getVehicle().getImagePath())
                    .into(imgVehicleImg);
        }

        bookingModelList = new ArrayList<>();
        bookingModelList1 = new ArrayList<>();

        bookingModelList.add(new BookingModel2(R.drawable.gps,"GPS Tracking"));
        bookingModelList.add(new BookingModel2(R.drawable.otp,"OTP Pickup & Drops"));
        bookingModelList.add(new BookingModel2(R.drawable.tube_less_tier,"Tube Less Tier"));
        bookingModelList.add(new BookingModel2(R.drawable.sos_button,"SOS Button"));
        bookingModelList.add(new BookingModel2(R.drawable.group_6313,"Vaccination Driver"));
        bookingModelList.add(new BookingModel2(R.drawable.first_aid_box,"First Aid Box"));
        bookingModelList.add(new BookingModel2(R.drawable.break_down,"Break Down"));
        bookingModelList.add(new BookingModel2(R.drawable.group_6320,"Fire Extinguisher"));
        bookingModelList.add(new BookingModel2(R.drawable.experience_driver,"Experience driver"));
        bookingModelList.add(new BookingModel2(R.drawable.sharing_location,"Sharing Location"));
        bookingModelList.add(new BookingModel2(R.drawable.gps_speed_limit,"GPS Speed Limit"));

        bookingModelList1.add(new BookingModel2(R.drawable.umbrella,"Umbrella"));
        bookingModelList1.add(new BookingModel2(R.drawable.wifi,"WIFI"));
        bookingModelList1.add(new BookingModel2(R.drawable.news_paper,"News Paper"));
        bookingModelList1.add(new BookingModel2(R.drawable.water_bottel,"Water Bottle"));
        bookingModelList1.add(new BookingModel2(R.drawable.sanitizer,"Sanitizer"));
        bookingModelList1.add(new BookingModel2(R.drawable.chocolate,"Chocolate"));
        bookingModelList1.add(new BookingModel2(R.drawable.shoes_cleaner,"Shoes Cleaner"));
        bookingModelList1.add(new BookingModel2(R.drawable.mask_group_26,"Eyes Mask"));
        bookingModelList1.add(new BookingModel2(R.drawable.notebook_pencil,"Notebook Pencil"));
        bookingModelList1.add(new BookingModel2(R.drawable.laptop_charger,"Laptop Charger"));
        bookingModelList1.add(new BookingModel2(R.drawable.mobile_charger,"Mobile Charger"));

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(),3,GridLayoutManager.HORIZONTAL,false);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(),3,GridLayoutManager.HORIZONTAL,false);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        BookingAdapter bookingAdapter = new BookingAdapter(getApplicationContext(),bookingModelList);
        recyclerView1.setAdapter(bookingAdapter);

        BookingAdapter bookingAdapter1 = new BookingAdapter(getApplicationContext(),bookingModelList1);
        recyclerView2.setAdapter(bookingAdapter1);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //startActivity(new Intent(BookingDetailsActivity.this, CarListTypeActivity.class));
                finish();
            }
        });

        imgCallBookingAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mobile = "7057052508";
                    Uri call = Uri.parse("tel:" + mobile);
                    Intent intent = new Intent(Intent.ACTION_CALL, call);

                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(BookingDetailsActivity.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(BookingDetailsActivity.this,
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

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // navController.navigate(R.id.action_bookingDetailsFragment_to_bookingDetailsSecondFragment);
                Intent intent = new Intent(BookingDetailsActivity.this, ConfirmBookingActivity.class);
                startActivity(intent);
            }
        });
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

        //startActivity(new Intent(BookingDetailsActivity.this, CarListTypeActivity.class));
        finish();

    }
}