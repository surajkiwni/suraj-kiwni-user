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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.DialogReviewAdapter;
import com.kiwni.app.user.adapter.FilterAdapter;
import com.kiwni.app.user.adapter.GridLayoutWrapper;
import com.kiwni.app.user.adapter.TitleItemAdapter;
import com.kiwni.app.user.global.PermissionRequestConstant;
import com.kiwni.app.user.interfaces.BookBtnClickListener;
import com.kiwni.app.user.interfaces.FilterItemClickListener;
import com.kiwni.app.user.interfaces.ReviewBtnClickListener;
import com.kiwni.app.user.models.Filter;
import com.kiwni.app.user.models.ReviewResponse;
import com.kiwni.app.user.models.vehicle_details.ScheduleMapResp;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class VehicleListByTypeActivity extends AppCompatActivity implements BookBtnClickListener,
        ReviewBtnClickListener, ConnectivityHelper.NetworkStateReceiverListener {
    RecyclerView recyclerView;
    TitleItemAdapter adapter;
    String TAG = this.getClass().getSimpleName();
    String direction = "", serviceType = "", fromLocation = "", endLocation = "",
            startDate = "", endDate = "", startTime = "", distanceInKm = "", mobile = "",
            vehicleTypeForDisplay = "", vehicleSeatCapacityForDisplay = "";

    ImageView imgBack,imgCall;
    TextView txtTitle, txtFromTo, txtStartEndDate, txtEstimatedKm, txtStartTime, txtVehicleType,
            txtSeatCapacity;

    ConstraintLayout sortLayout, mapLayout, filterLayout, constraintLayoutForRentalPackage;
    String strBrand = "", strSegment = "", strYear = "", strSpecialReq = "";

    boolean isSelected = false, isNetworkAvailable = false;

    List<ScheduleMapResp> mList = new ArrayList<>();
    List<ScheduleMapResp> remainingList = new ArrayList<>();

    private ConnectivityHelper connectivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list_by_type);

        constraintLayoutForRentalPackage = findViewById(R.id.constraintLayoutForRentalPackage);
        txtTitle = findViewById(R.id.txtTitle);
        txtFromTo = findViewById(R.id.txtFromTo);
        recyclerView = findViewById(R.id.main_recyclerview);
        imgBack = findViewById(R.id.imgBack);
        imgCall = findViewById(R.id.imgCall);
        mapLayout = findViewById(R.id.mapLayout);
        sortLayout = findViewById(R.id.sortLayout);
        filterLayout = findViewById(R.id.filterLayout);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);
        txtVehicleType = findViewById(R.id.txtVehicleType);
        txtSeatCapacity = findViewById(R.id.txtSeatCapacity);

        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        fromLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        endLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");
        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");
        vehicleTypeForDisplay = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.VEHICLE_TYPE_FOR_DISPLAY, "");
        vehicleSeatCapacityForDisplay = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.VEHICLE_SEAT_CAPACITY_FOR_DISPLAY, "");

        //Log.d(TAG,"data from previous screen - " + direction + " , " + serviceType);

        txtTitle.setText(serviceType + " ( " + direction + " ) ");
        txtFromTo.setText(fromLocation + " To " + endLocation);
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);
        txtVehicleType.setText(vehicleTypeForDisplay);
        txtSeatCapacity.setText(vehicleSeatCapacityForDisplay);

        //visibility

        imgCall.setVisibility(View.VISIBLE);

        /* start receiver for network state */
        startNetworkBroadcastReceiver(this);

        switch (serviceType)
        {
            case "Outstation":
                if (direction.equals("two-way"))
                {
                    txtTitle.setText(serviceType + "(Round Trip)");
                    txtStartEndDate.setText(startDate + " - " + endDate);
                }
                else
                {
                    txtTitle.setText(serviceType + "(One Way)");
                }
                break;
            case "Airport":
                if (direction.equals("airport-pickup"))
                {
                    txtTitle.setText(serviceType + " Pickup");
                }
                else
                {
                    txtTitle.setText(serviceType + " Drop");
                }
                break;
            case "Rental":
                if (direction.equals("current-booking"))
                {
                    txtTitle.setText(serviceType + " ( Current Booking ) ");
                    constraintLayoutForRentalPackage.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtTitle.setText(serviceType + " ( Schedule Trip ) ");
                    constraintLayoutForRentalPackage.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "No Options", Toast.LENGTH_SHORT).show();
                break;
        }

        Gson gson = new Gson();
        String stringData = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SELECTED_VEHICLE_TYPE_OBJECT, "");
        String stringDuplicateData = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DUPLICATE_VEHICLE_OBJECT, "");

        if(stringData != null)
        {
            Type type = new TypeToken<List<ScheduleMapResp>>() {
            }.getType();
            mList = gson.fromJson(stringData, type);
            remainingList = gson.fromJson(stringDuplicateData, type);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutWrapper(getApplicationContext(), 1));

            /* set data to recyclerview */
            setParentLayoutData(mList, remainingList);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(VehicleListByTypeActivity.this, VehicleTypeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = "7057052508";
                Uri call = Uri.parse("tel:" + mobile);
                Intent intent = new Intent(Intent.ACTION_CALL, call);

                if (ContextCompat.checkSelfPermission(VehicleListByTypeActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(VehicleListByTypeActivity.this,
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

        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleListByTypeActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        sortLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(VehicleListByTypeActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sort_layout);

                dialog.show();

                AppCompatButton btnLatest = dialog.findViewById(R.id.btnLatest);
                AppCompatButton btnPopularity = dialog.findViewById(R.id.btnPopularity);
                AppCompatButton btnLowToHigh = dialog.findViewById(R.id.btnLowToHigh);
                AppCompatButton btnHighToLow = dialog.findViewById(R.id.btnHighToLow);

                ImageView imageBack = dialog.findViewById(R.id.imageBack);

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnLatest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });
                btnPopularity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnLowToHigh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnHighToLow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(VehicleListByTypeActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_screen);

                RecyclerView filterRecyclerView;
                List<Filter> filterModelList;
                FilterItemClickListener filterItemClickListener;
                TextView premiumText, luxuryText, ultraLuxuryText;
                CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;

                dialog.show();
                int[] back_background;

                back_background = new int[]{R.drawable.spinner_background, R.drawable.white_background};

                premiumText = dialog.findViewById(R.id.premiumText);
                luxuryText = dialog.findViewById(R.id.luxuryText);
                ultraLuxuryText = dialog.findViewById(R.id.ultraLuxuryText);

                checkBox1 = dialog.findViewById(R.id.checkBox1);
                checkBox2 = dialog.findViewById(R.id.checkBox2);
                checkBox3 = dialog.findViewById(R.id.checkBox3);
                checkBox4 = dialog.findViewById(R.id.checkBox4);
                checkBox5 = dialog.findViewById(R.id.checkBox5);
                checkBox6 = dialog.findViewById(R.id.checkBox6);

                checkBox1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(checkBox1.isChecked())
                        {
                            strSpecialReq = checkBox1.getText().toString();
                        }
                        else
                        {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(checkBox2.isChecked())
                        {
                            strSpecialReq = checkBox2.getText().toString();
                        }
                        else
                        {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(checkBox3.isChecked())
                        {
                            strSpecialReq = checkBox3.getText().toString();
                        }
                        else
                        {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(checkBox4.isChecked())
                        {
                            strSpecialReq = checkBox4.getText().toString();
                        }
                        else
                        {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(checkBox5.isChecked())
                        {
                            strSpecialReq = checkBox5.getText().toString();
                        }
                        else
                        {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(checkBox6.isChecked())
                        {
                            strSpecialReq = checkBox6.getText().toString();
                        }
                        else
                        {
                            strSpecialReq = "";
                        }
                    }
                });

                premiumText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                       strSegment = " ";
                        if (strSegment.equals(" "))
                        {
                            strSegment = premiumText.getText().toString();
                            isSelected = true;
                        }
                        else
                        {
                            strSegment = "";
                            isSelected = false;
                        }

                        if(isSelected)
                        {
                            premiumText.setBackgroundResource(R.drawable.spinner_background);
                        }
                        else
                        {
                            premiumText.setBackgroundColor(Color.WHITE);
                        }

                        // fetching length of array
                        int array_length = back_background.length;
                        // object creation of random class
                        Random random = new Random();
                        // generation of random number
                        int random_number = random.nextInt(array_length);
                        // set background images on screenView
                        // using setBackground() method.
                        premiumText.setBackground(ContextCompat.getDrawable(getApplicationContext(), back_background[random_number]));
                    }

                });

                luxuryText.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // fetching length of array
                        int array_length = back_background.length;

                        // object creation of random class
                        Random random = new Random();

                        // generation of random number
                        int random_number = random.nextInt(array_length);

                        // set background images on screenView
                        // using setBackground() method.
                        luxuryText.setBackground(ContextCompat.getDrawable(getApplicationContext(), back_background[random_number]));

                        strSegment = luxuryText.getText().toString();
                        //luxuryText.setBackgroundResource(R.drawable.spinner_background);
                    }
                });

                ultraLuxuryText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        // fetching length of array
                        int array_length =back_background.length;

                        // object creation of random class
                        Random random = new Random();

                        // generation of random number
                        int random_number = random.nextInt(array_length);

                        // set background images on screenView
                        // using setBackground() method.
                        ultraLuxuryText.setBackground(ContextCompat.getDrawable(getApplicationContext(), back_background[random_number]));

                        strSegment = ultraLuxuryText.getText().toString();
                        //ultraLuxuryText.setBackgroundResource(R.drawable.spinner_background);
                        //Toast.makeText(getApplicationContext(), "Clicked On" + strSegment, Toast.LENGTH_SHORT).show();
                    }
                });

                filterRecyclerView = dialog.findViewById(R.id.filterRecyclerView);

                filterItemClickListener = new FilterItemClickListener() {
                    @Override
                    public void onFilterItemClick(View v, int position, List<Filter> filterModels) {
                        strBrand = filterModels.get(position).getName();
                        //Toast.makeText(getApplicationContext(), "Item Clicked - " + strBrand, Toast.LENGTH_SHORT).show();
                    }
                };


                Spinner spinner = dialog.findViewById(R.id.spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getContext(),
                        R.array.select_year, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        strYear = spinner.getSelectedItem().toString();
                        if(!strYear.equals("Select Year"))
                        {
                            Toast.makeText(getApplicationContext(), "Clicked On " + strYear, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                filterModelList = new ArrayList<>();

                filterModelList.add(new Filter(R.drawable.volkswagenlogo, "Volkswagen"));
                filterModelList.add(new Filter(R.drawable.hondalogo, "Honda"));
                filterModelList.add(new Filter(R.drawable.marutisuzukilogo, "Maruti Suzuki"));
                filterModelList.add(new Filter(R.drawable.mercedesbenzlogo, "Mercedes Benz"));
                filterModelList.add(new Filter(R.drawable.toyotalogo, "Toyota"));
                filterModelList.add(new Filter(R.drawable.audilogo, "Audi"));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                filterRecyclerView.setLayoutManager(linearLayoutManager);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(dialog.getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                filterRecyclerView.setLayoutManager(gridLayoutManager);

                FilterAdapter filterAdapter = new FilterAdapter(dialog.getContext(), filterModelList, filterItemClickListener);
                filterRecyclerView.setAdapter(filterAdapter);

                ImageView imageBack = dialog.findViewById(R.id.imageBack);
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setParentLayoutData(List<ScheduleMapResp> numberList, List<ScheduleMapResp> remainingList) {
        // pass all data to the title adapter
        adapter = new TitleItemAdapter(getApplicationContext(), numberList, remainingList, this,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PermissionRequestConstant.MY_PERMISSIONS_REQUEST_CALL_PHONE: {
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(VehicleListByTypeActivity.this, VehicleTypeListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBookBtnClick(View v, int position, List<ScheduleMapResp> scheduleMapRespList)
    {
        Log.d(TAG, "data get on click = " + scheduleMapRespList.get(position));

        List<ScheduleMapResp> selectedVehicleData = new ArrayList<>();
        selectedVehicleData.add(scheduleMapRespList.get(position));

        Log.d(TAG, "selectedVehicleData = " + selectedVehicleData);

        Gson gson = new Gson();
        Type type = new TypeToken<List<ScheduleMapResp>>() {}.getType();
        String jsonForData = gson.toJson(selectedVehicleData, type);

        Intent intent = new Intent(VehicleListByTypeActivity.this, ConfirmBookingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.SELECTED_VEHICLE_OBJECT, jsonForData);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onReviewBtnClick(View v, int position)
    {
        Dialog dialog = new Dialog(VehicleListByTypeActivity.this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_review);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        List<ReviewResponse> reviewResponseList;

        RecyclerView recyclerDigReview = dialog.findViewById(R.id.recyclerDigReview);

        reviewResponseList = new ArrayList<>();
        reviewResponseList.add(new ReviewResponse("Suraj Phadtare","I have booked cab one way from Mumbai to Pune It was great and very much comfortable journey Driver also very helpful.Cab proper clean and they are using all COVID protocols."));
        reviewResponseList.add(new ReviewResponse("Shubham Shinde","I have booked cab one way from Mumbai to Pune It was great and very much comfortable journey Driver also very helpful.Cab proper clean and they are using all COVID protocols."));
        reviewResponseList.add(new ReviewResponse("Damini Mistri","I have booked cab one way from Mumbai to Pune It was great and very much comfortable journey Driver also very helpful.Cab proper clean and they are using all COVID protocols."));
        reviewResponseList.add(new ReviewResponse("Nalin Giri","I have booked cab one way from Mumbai to Pune It was great and very much comfortable journey Driver also very helpful.Cab proper clean and they are using all COVID protocols."));
        reviewResponseList.add(new ReviewResponse("Drashti Patel","I have booked cab one way from Mumbai to Pune It was great and very much comfortable journey Driver also very helpful.Cab proper clean and they are using all COVID protocols."));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerDigReview.setLayoutManager(linearLayoutManager);

        DialogReviewAdapter dialogReviewAdapter = new DialogReviewAdapter(VehicleListByTypeActivity.this, reviewResponseList);
        recyclerDigReview.setAdapter(dialogReviewAdapter);
        dialogReviewAdapter.notifyDataSetChanged();

        dialog.show();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void networkAvailable()
    {
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
}

